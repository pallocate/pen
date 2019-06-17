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
import pen.eco.Log
import pen.eco.LogLevel.INFO
import pen.eco.LogLevel.WARN
import pen.eco.LogLevel.ERROR
import pen.eco.Loggable
import pen.eco.Serializer
import pen.eco.Convertable
import pen.eco.create_dir
import pen.eco.Constants
import pen.eco.Config
import pen.net.kad.Constants as KadConstants
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
         Log.debug({"$ownerName- loading KKademliaNode"}, Config.flag( "KAD_SAVE_LOAD" ))
         var ret : KKademliaNode? = null

         try
         {
            val dir = storageDir( ownerName ) + Constants.SLASH

            /* Read basic  */
            var fileReader = FileReader(dir + KadConstants.KAD_FILE)
            val kademliaNode = Serializer.read<KKademliaNode>( fileReader )

            if (kademliaNode is KKademliaNode)
            {
               /* Read routing table info */
               fileReader = FileReader( dir + KadConstants.ROUTING_TABLE_FILE )
               val rtInfo = Serializer.read<KSerializableRoutingInfo>( fileReader )

               /* Read node */
               fileReader = FileReader( dir + KadConstants.NODE_FILE )
               val node = Serializer.read<KNode>( fileReader )

               /* Read DHT */
               fileReader = FileReader( dir + KadConstants.DHT_FILE )
               val dht = Serializer.read<KDHT>( fileReader )

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
      fun storageDir (nameDir : String, subDir : String = "nodeState" ) : String
      {
         val stringBuilder = StringBuilder()

         stringBuilder.apply {
            append( Constants.USER_HOME )
            append( Constants.SLASH )
            append( Constants.CONFIG_DIR )
            append( Constants.SLASH )
            append( "kademlia" )
            append( Constants.SLASH )
            append( nameDir )
            append( Constants.SLASH )
            append( subDir )
         }
         val dirName = stringBuilder.toString()
         create_dir( dirName )

         return stringBuilder.toString()
      }
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
      log("initializing", Config.flag( "KAD_INITIALIZE" ))

      routingTable.initialize( node )
      dht.initialize( ownerName )
      server.initialize( this, port )

      startRefreshing()
   }

   @Synchronized
   fun bootstrap (otherNode : KNode)
   {
      log("bootstrapping to (${otherNode})", Config.flag( "KAD_BOOTSTRAP" ))
      val startTime = System.nanoTime()*1000
      val op = KConnectOperation( server, node, routingTable, dht, otherNode )

      try
      {
         op.execute()
         log("bootstrap complete", Config.flag( "KAD_BOOTSTRAP" ))

         val endTime = System.nanoTime()*1000
         Stats.setBootstrapTime( endTime - startTime )
      }
      catch (e: Exception)
      {
         log("connection failed, ${e.message}", Config.flag( "KAD_BOOTSTRAP" ))
      }
   }

   fun put(content : KContent) = put(KStorageEntry( content ))
   fun put (entry : KStorageEntry) : Int
   {
      log("storing entry [${entry.content.key.shortName()}]", Config.flag( "CONTENT_PUT_GET" ))
      val storeOperation = KStoreOperation( server, node, routingTable, dht, entry )
      storeOperation.execute()

      /* Return how many nodes the content was stored on */
      return storeOperation.numNodesStoredAt()
   }

   fun putLocally (content : KContent)
   {
      log("storing entry [${content.key.shortName()}] locally", Config.flag( "CONTENT_PUT_GET" ))
      dht.store(KStorageEntry( content ))
   }

   fun get (kGetParameter : KGetParameter) : StorageEntry
   {
      log("retrieving entry [${kGetParameter.key.shortName()}]", Config.flag( "CONTENT_PUT_GET" ))
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
      log("saving", Config.flag( "KAD_SAVE_LOAD" ))
      val dir = storageDir( ownerName ) + Constants.SLASH

      /* Store Basic  data. */
      var fileWriter = FileWriter( dir + KadConstants.KAD_FILE )
      Serializer.write( this, fileWriter )
      fileWriter.close()

      /* Save the node state. */
      fileWriter = FileWriter( dir + KadConstants.NODE_FILE)
      Serializer.write( node, fileWriter )
      fileWriter.close()

      /* Save the routing table. */
      fileWriter = FileWriter( dir + KadConstants.ROUTING_TABLE_FILE)
      Serializer.write( KSerializableRoutingInfo( routingTable ), fileWriter )
      fileWriter.close()

      /* Save the DHT. */
      fileWriter = FileWriter( dir + KadConstants.DHT_FILE)
      Serializer.write( dht, fileWriter )
      fileWriter.close()
   }

   private fun startRefreshing ()
   {
      log("start refreshing", Config.flag( "KAD_INTERNAL" ))
      refreshTimer = Timer( true )
      refreshTask = RefreshTimerTask()
      refreshTimer?.schedule( refreshTask, KadConstants.RESTORE_INTERVAL, KadConstants.RESTORE_INTERVAL )
   }

   private fun stopRefreshing ()
   {
      log("stop refreshing", Config.flag( "KAD_INTERNAL" ))
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

   override fun originName () = "KKademliaNode(${node})"

   fun getRoutingTable () = routingTable
   fun getNode () = node
   fun getDHT () = dht
   fun getServer () = server

   inner class RefreshTimerTask () : TimerTask()
   {
      override fun run ()
      {
         try
         { refresh() }
         catch (e : IOException)
         { log("refresh failed!", Config.flag( "KAD_INTERNAL" ), WARN) }
      }

      override fun cancel () : Boolean
      {
         log("refresh canceled", Config.flag( "KAD_INTERNAL" ))
         return false
      }
   }
}
