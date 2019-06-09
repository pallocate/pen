package pen.net.kad.operations

import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.HashMap
import java.util.SortedMap
import java.util.TreeMap
import pen.eco.Loggable
import pen.eco.LogLevel.WARN

import pen.eco.Config
import pen.net.kad.Constants
import pen.net.kad.RoutingException
import pen.net.kad.ContentNotFoundException
import pen.net.kad.KServer
import pen.net.kad.NoStorageEntry
import pen.net.kad.StorageEntry
import pen.net.kad.KKademliaNode
import pen.net.kad.dht.KGetParameter
import pen.net.kad.messages.Message
import pen.net.kad.messages.KFindValueMessage
import pen.net.kad.messages.KContentMessage
import pen.net.kad.messages.KFindNodeReply
import pen.net.kad.messages.receivers.Receiver
import pen.net.kad.node.KKeyComparator
import pen.net.kad.node.KNode
import pen.net.kad.utils.KRouteLengthChecker
import pen.net.kad.routing.KRoutingTable

/** Looks up a specified identifier and returns the value associated with it */
class KFindValueOperation
/** @param params The parameters to search for the content which we need to find */
(private val server : KServer, private val node : KNode, private val routingTable : KRoutingTable, params : KGetParameter) : Operation, Receiver, Loggable
{
   /* Constants */
   companion object
   {
      private val UNASKED = 0x00.toByte()
      private val AWAITING = 0x01.toByte()
      private val ASKED = 0x02.toByte()
      private val FAILED = 0x03.toByte()
   }

   private var contentFound : StorageEntry = NoStorageEntry()
   private val lookupMessage: KFindValueMessage

   /** Whether the content was found or not. */
   var isContentFound = false
      private set
   private val nodes : SortedMap<KNode, Byte>

   /* Tracks messages in transit and awaiting reply */
   private val messagesTransiting = HashMap<Int, KNode>()

   /* Used to sort nodes */
   private val comparator : Comparator<KNode>

   /* Statistical information */
   private val routeLengthChecker = KRouteLengthChecker()

   init
   {
//      routeLengthChecker = KRouteLengthChecker()

      /* Construct our lookup message */
      this.lookupMessage = KFindValueMessage( node, params )

      /**
      * We initialize a TreeMap to store nodes.
      * This map will be sorted by which nodes are closest to the lookupId
      */
      this.comparator = KKeyComparator( params.key )
      this.nodes = TreeMap(this.comparator)
   }

   @Synchronized
   override fun execute()
   {
      try
      {
         /* Set the local node as already asked */
         nodes.put( node, ASKED )

         /** We add all nodes here instead of the K-Closest because there may be the case that the K-Closest are offline
           * - The operation takes care of looking at the K-Closest. */
         val allNodes = routingTable.allNodes()
         addNodes( allNodes )

         /* Also add the initial set of nodes to the routeLengthChecker */
         routeLengthChecker.addInitialNodes( allNodes )

         /** If we haven't found the requested amount of content as yet,
           * keey trying until Constants.OPERATION_TIMEOUT time has expired */
         var totalTimeWaited = 0
         val timeInterval = 10                                                  // We re-check every n milliseconds

         while (totalTimeWaited < Constants.OPERATION_TIMEOUT)
         {
            if (!askNodesorFinish() && !isContentFound)
            {
               (this as Object).wait( timeInterval.toLong() )
               totalTimeWaited += timeInterval
            }
            else
               break
         }
      }
      catch (e : Exception)
      { log("interrupted!", Config.flag( "CONTENT_FIND" ), WARN) }
   }

   /** Add nodes from this list to the set of nodes to lookup
     * @param list The list from which to add nodes */
   fun addNodes (list : List<KNode>)
   {
      for (o in list)
      {
         /* If this node is not in the list, add the node */
         if (!nodes.containsKey( o ))
            nodes.put( o, UNASKED )
      }
   }

   /** Asks some of the K closest nodes seen but not yet queried.
     * Assures that no more than DefaultConfig.CONCURRENCY messages are in transit at a time
     *
     * This method should be called every time a reply is received or a timeout occurs.
     * If all K closest nodes have been asked and there are no messages in transit,
     * the algorithm is finished.
     * @return `true` if finished OR `false` otherwise */
   private fun askNodesorFinish () : Boolean
   {
      /* If >= CONCURRENCY nodes are in transit, don't do anything */
      if (Constants.MAX_CONCURRENT_MESSAGES_TRANSITING <= messagesTransiting.size)
         return false

      /* Get unqueried nodes among the K closest seen that have not FAILED */
      val unasked = closestNodesNotFailed( UNASKED )

      if (unasked.isEmpty() && messagesTransiting.isEmpty())
         return true                                                            // We have no unasked nodes nor any messages in transit, we're finished!

      /* Sort nodes according to criteria */
      Collections.sort( unasked, comparator )

      /** Send messages to nodes in the list;
        * making sure than no more than CONCURRENCY messsages are in transit */
      var i = 0

      while (messagesTransiting.size < Constants.MAX_CONCURRENT_MESSAGES_TRANSITING && i < unasked.size)
      {
         val n = unasked[i]
         val comm = server.sendMessage( n, lookupMessage, this )

         nodes.put( n, AWAITING )
         messagesTransiting.put( comm, n )
         i++
      }

      /* We're not finished as yet, return false */
      return false
   }

   /** Find The K closest nodes to the target lookupId given that have not FAILED.
     * From those K, get those that have the specified status
     * @param status The status of the nodes to return
     * @return A List of the closest nodes */
   private fun closestNodesNotFailed (status : Byte) : List<KNode>
   {
      val closestNodes = ArrayList<KNode>(Constants.K)
      var remainingSpaces = Constants.K

      for (e in nodes.entries)
      {
         if (!FAILED.equals( e.value ))
         {
            if (status.equals( e.value ))
               closestNodes.add( e.key as KNode )                                // We got one with the required status, now add it

            if (--remainingSpaces == 0)
               break
         }
      }

      return closestNodes
   }

   @Synchronized
   override fun receive (message : Message, conversationId : Int)
   {
      if (!isContentFound)
      {
         if (message is KContentMessage)
         {
            log("received content message", Config.flag( "CONTENT_FIND" ))
            /* The reply received is a content message with the required content, take it in */

            /* Add the origin node to our routing table */
            routingTable.insert( message.origin )

            /* Get the ContentMessage and check if it satisfies the required parameters */
            val content = message.content
            contentFound = content
            isContentFound = true
         }
         else
            if (message is KFindNodeReply)                                      // A KFindNodeReply with nodes closest to the content
            {
               log("received content reply", Config.flag( "CONTENT_FIND" ))

               /* Add the origin node to our routing table */
               val origin = message.origin
               routingTable.insert( origin )

               /* Set that we've completed ASKing the origin node */
               nodes.put( origin, ASKED )

               /* Remove this message from messagesTransiting since it's completed now */
               messagesTransiting.remove( conversationId )

               /* Add the received nodes to the routeLengthChecker */
               routeLengthChecker.addNodes(message.nodes, origin)

               /* Add the received nodes to our nodes list to query */
               addNodes( message.nodes )
               askNodesorFinish()
            }
      }
   }

   /** A node does not respond or a packet was lost, we set this node as failed */
   @Synchronized
   override fun timeout (conversationId : Int)
   {
      /* Get the node associated with this communication */
      val inTransit = messagesTransiting.get( conversationId )

      if (inTransit == null)
         log("invalid conversation id!", Config.flag( "RECEIVER_TIMEOUT" ), WARN)
      else
      {
         /* Mark this node as failed and inform the routing table that it's unresponsive */
         nodes.put( inTransit, FAILED )
         routingTable.setUnresponsiveContact( inTransit )
         messagesTransiting.remove( conversationId )

         askNodesorFinish()
      }
   }

   /** @return The list of all content found during the lookup operation */
   fun getContentFound () : StorageEntry
   {
      var ret : StorageEntry = NoStorageEntry()

      if (isContentFound)
         ret = contentFound
      else
         log("no value found", Config.flag( "CONTENT_PUT_GET" ))

      return ret
   }

   /** @return How many hops it took in order to get to the content. */
   fun routeLength () = routeLengthChecker.routeLength

   override fun originName () = "KFindValueOperation(${node})"
}
