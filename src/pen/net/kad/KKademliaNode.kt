package pen.net.kad

import java.io.File
import java.io.FileWriter
import java.io.FileReader
import java.io.IOException
import java.net.InetAddress
import java.util.NoSuchElementException
import java.util.Timer
import java.util.TimerTask
import com.beust.klaxon.Converter
import pen.eco.common.Log
import pen.eco.common.Log.Level.INFO
import pen.eco.common.Log.Level.WARN
import pen.eco.common.Log.Level.ERROR
import pen.eco.common.Loggable
import pen.eco.common.KSerializer
import pen.eco.common.Directory
import pen.eco.common.KSettings
import pen.eco.common.DebugValue
import pen.eco.common.NoConverter
import pen.eco.common.Convertable
import pen.eco.common.Config
import pen.eco.common.Config.getSettings
import pen.net.kad.dht.KDHT
import pen.net.kad.dht.KContent
import pen.net.kad.dht.KStorageEntry
import pen.net.kad.dht.KGetParameter
import pen.net.kad.messages.receivers.ReceiverFactory
import pen.net.kad.node.KNode
import pen.net.kad.node.KNodeId
import pen.net.kad.operations.KConnectOperation
import pen.net.kad.operations.KFindValueOperation
import pen.net.kad.operations.KKadRefreshOperation
import pen.net.kad.operations.KStoreOperation
import pen.net.kad.routing.KRoutingTable
import pen.net.kad.utils.KSerializableRoutingInfo

/** Primary constructor. */
class KKademliaNode () : Convertable, Loggable
{
   companion object
   {
      /** Loads  file. */
      fun loadFromFile (ownerName : String) : KKademliaNode?
      {
         Log.debug({"$ownerName- loading KKademliaNode"}, getSettings().getValue( DebugValue.MAIN_SAVE_LOAD ))
         var ret : KKademliaNode? = null

         try
         {
            val dir = storageDir( ownerName ) + File.separator

            /* Read basic  */
            var fileReader = FileReader( dir + Constants.SERVICE_NODE_FILE )
            val kademliaNode = KSerializer.read<KKademliaNode>( fileReader )

            if (kademliaNode is KKademliaNode)
            {
               /* Read routing table info */
               fileReader = FileReader( dir + Constants.ROUTING_TABLE_FILE )
               val rtInfo = KSerializer.read<KSerializableRoutingInfo>( fileReader )

               /* Read node */
               fileReader = FileReader( dir + Constants.NODE_FILE )
               val node = KSerializer.read<KNode>( fileReader )

               /* Read DHT */
               fileReader = FileReader( dir + Constants.DHT_FILE )
               val dht = KSerializer.read<KDHT>( fileReader )

               if (rtInfo is KSerializableRoutingInfo && node is KNode && dht is KDHT)
               {
                  kademliaNode.initialize( node, rtInfo.toRoutingTable(), dht )
                  ret = kademliaNode
               }
            }
         }
         catch (e : Exception)
         { Log.err( "${ownerName}- KKademliaNode load failed! ${e.message}" ) }

         return ret
      }

      /** @return The name of the content storage folder. */
      private fun storageDir (ownerName : String) = Directory.create( Config.nodeDir( ownerName ) + File.separator + "nodeState" )
   }

   var ownerName                                  = ""
   var port                                       = 0                           // 49152-65535 are private ports
   private var node                               = KNode()
   private var routingTable                       = KRoutingTable()
   private var dht                                = KDHT()

   private var server                             = KServer()
   private var refreshTimer : Timer?              = null
   private var refreshTask                        = RefreshTimerTask()

   init
   {Log.info( "KKademliaNode- created" )}

   /** Secondary constructor. */
   constructor (name : String, id : String, port : Int) : this ()
   {
      ownerName = name
      this.port = port
      node = KNode( KNodeId( id ), InetAddress.getLocalHost(), port )

      initialize()
   }

   internal fun initialize (node : KNode, routingTable : KRoutingTable, dht : KDHT)
   {
      this.routingTable = routingTable
      this.node = node
      this.dht = dht

      initialize()
   }

   private fun initialize ()
   {
      log("initializing", getSettings().getValue( DebugValue.MAIN_INITIALIZE ))

      routingTable.initialize( node )
      dht.initialize( ownerName )
      server.initialize( this, port )

      startRefreshing()
   }

   @Synchronized
   fun bootstrap (otherNode : KNode)
   {
      log("bootstrapping to (${otherNode})", getSettings().getValue( DebugValue.MAIN_BOOTSTRAP ))
      val startTime = System.nanoTime()*1000
      val op = KConnectOperation( server, node, routingTable, dht, otherNode )

      try
      {
         op.execute()
         log("bootstrap complete", Config.getSettings().getValue( DebugValue.MAIN_BOOTSTRAP ))

         val endTime = System.nanoTime()*1000
         Stats.setBootstrapTime( endTime - startTime )
      }
      catch (e: Exception)
      {
         log("connection failed, ${e.message}", getSettings().getValue( DebugValue.MAIN_BOOTSTRAP ))
      }
   }

   fun put(content : KContent) = put(KStorageEntry( content ))
   fun put (entry : KStorageEntry) : Int
   {
      log("storing entry [${entry.content.key.shortName()}]", getSettings().getValue( DebugValue.CONTENT_PUT_GET ))
      val storeOperation = KStoreOperation( server, node, routingTable, dht, entry )
      storeOperation.execute()

      /* Return how many nodes the content was stored on */
      return storeOperation.numNodesStoredAt()
   }

   fun putLocally (content : KContent)
   {
      log("storing entry [${content.key.shortName()}] locally", getSettings().getValue( DebugValue.CONTENT_PUT_GET ))
      dht.store(KStorageEntry( content ))
   }

   fun get (kGetParameter : KGetParameter) : StorageEntry
   {
      log("retrieving entry [${kGetParameter.key.shortName()}]", getSettings().getValue( DebugValue.CONTENT_PUT_GET ))
      if (dht.contains( kGetParameter ))
      {
         /* If the content exist in our own KDHT, then return it. */
         return dht.get( kGetParameter )
      }

      /* Seems like it doesn't exist in our KDHT, get it from other Nodes */
      val startTime = System.nanoTime()
      val kFindValueOperation = KFindValueOperation( server, node, routingTable, kGetParameter )
      kFindValueOperation.execute()
      val endTime = System.nanoTime()
      Stats.addContentLookup( endTime - startTime, kFindValueOperation.routeLength(), kFindValueOperation.isContentFound )

      return kFindValueOperation.getContentFound()
   }

   fun refresh ()
   {KKadRefreshOperation( server, node, routingTable, dht ).execute()}

  /* @param saveState If this  should be saved. */
   fun shutdown (saveState : Boolean)
   {
      Log.info( "${ownerName}- shutting down!")
      server.shutdown()
      stopRefreshing()

      if (saveState)
         saveState()
   }

   private fun saveState ()
   {
      log("saving", getSettings().getValue( DebugValue.MAIN_SAVE_LOAD ))

      /* Store Basic  data. */
      var fileWriter = FileWriter(storageDir( ownerName ) + File.separator + Constants.SERVICE_NODE_FILE)
      KSerializer.write( this, fileWriter )
      fileWriter.close()

      /* Save the node state. */
      fileWriter = FileWriter(storageDir( ownerName ) + File.separator + Constants.NODE_FILE)
      KSerializer.write( node, fileWriter )
      fileWriter.close()

      /* Save the routing table. */
      fileWriter = FileWriter(storageDir( ownerName ) + File.separator + Constants.ROUTING_TABLE_FILE)
      KSerializer.write( KSerializableRoutingInfo( routingTable ), fileWriter )
      fileWriter.close()

      /* Save the DHT. */
      fileWriter = FileWriter(storageDir( ownerName ) + File.separator + Constants.DHT_FILE)
      KSerializer.write( dht, fileWriter )
      fileWriter.close()
   }

   private fun startRefreshing ()
   {
      log("start refreshing", getSettings().getValue( DebugValue.SERVICE_NODE_INTERNAL ))
      refreshTimer = Timer( true )
      refreshTask = RefreshTimerTask()
      refreshTimer?.schedule( refreshTask, Constants.RESTORE_INTERVAL, Constants.RESTORE_INTERVAL )
   }

   private fun stopRefreshing ()
   {
      log("stop refreshing", getSettings().getValue( DebugValue.SERVICE_NODE_INTERNAL ))
      refreshTask.cancel()
      refreshTimer?.cancel()
      refreshTimer?.purge()
   }

   override fun toString () : String
   {
      val sb = StringBuilder( "\n\n owner: $ownerName \n\n\n" )

      sb.append("Local node: ${ node.nodeId }\n\n")
      sb.append( "Routing Table: $routingTable\n\n" )
      sb.append( "KDHT: $dht\n\n" )

      return sb.toString()
   }

   override fun loggingName () = "KKademliaNode(${node})"

   fun getRoutingTable () = routingTable
   fun getNode () = node
   fun getDHT () = dht
   fun getServer () = server
   override fun getConverters () = Array<Converter>( 0, {NoConverter()} )

   inner class RefreshTimerTask () : TimerTask()
   {
      override fun run ()
      {
         try
         { refresh() }
         catch (e : IOException)
         { log("refresh failed!", getSettings().getValue( DebugValue.SERVICE_NODE_INTERNAL ), WARN) }
      }

      override fun cancel () : Boolean
      {
         log("refresh canceled", getSettings().getValue( DebugValue.SERVICE_NODE_INTERNAL ))
         return false
      }
   }
}
