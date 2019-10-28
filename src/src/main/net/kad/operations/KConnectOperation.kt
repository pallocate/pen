/** Operation that handles connecting to an existing Kademlia network using a bootstrap node */
package pen.net.kad.operations

import pen.net.kad.messages.receivers.Receiver
import pen.eco.Loggable
import pen.eco.LogLevel.WARN
import pen.eco.Config
import pen.net.kad.KServer
import pen.net.kad.Constants
import pen.net.kad.KKademliaNode
import pen.net.kad.RoutingException
import pen.net.kad.dht.KDHT
import pen.net.kad.messages.Message
import pen.net.kad.messages.KAcknowledgeMessage
import pen.net.kad.messages.KConnectMessage
import pen.net.kad.node.KNode
import pen.net.kad.routing.KRoutingTable

/** @param server The message server used to send/receive messages
  * @param local The local node
  * @param bootstrap Node to use to bootstrap the local node onto the network */
class KConnectOperation (private val server : KServer, private val node : KNode, private val routingTable : KRoutingTable, private val dht : KDHT, private val otherNode : KNode) : Operation, Receiver, Loggable
{
   val MAX_CONNECT_ATTEMPTS = 5                                                 // Try 5 times to connect to a node

   private var error: Boolean = false
   private var attempts: Int = 0

   @Synchronized
   override fun execute ()
   {
      try
      {
         /* Contact the bootstrap node */
         this.error = true
         this.attempts = 0
         val kConnectMessage = KConnectMessage( node )

         /* Send a connect message to the bootstrap node */
         server.sendMessage( otherNode, kConnectMessage, this )

         /* If we haven't finished as yet, wait for a maximum of Constants.OPERATION_TIMEOUT time */
         var totalTimeWaited = 0
         val timeInterval = 50                                                  // We re-check every 300 milliseconds

         while (totalTimeWaited < Constants.OPERATION_TIMEOUT)
         {
            if (error)
            {
              (this as Object).wait( timeInterval.toLong() )
              totalTimeWaited += timeInterval
            }
            else
               break
         }
         check( !error, {"no responce"} ) // If we still haven't received any responses by then, do a routing timeout */

         // This code seems not to be reached:

         /* Perform lookup for our own ID to get nodes close to us */
         val kFindNodeOperation = KFindNodeOperation( server, node, routingTable, node.nodeId )
         kFindNodeOperation.execute()

         /** Refresh buckets to get a good routing table
           * After the above lookup operation, K nodes will be in our routing table,
           * Now we try to populate all of our buckets. */
         KBucketRefreshOperation( server, node, routingTable, dht ).execute()
      }
      catch (e : InterruptedException)
      { log("interrupted", Config.flag( "KAD_BOOTSTRAP" ), WARN) }
   }

   /** Receives an AcknowledgeMessage from the bootstrap node. */
   @Synchronized
   override fun receive (message : Message, conversationId : Int)
   {
      log("received message", Config.flag( "CONTACT_CONNECT" ))

      /* The bootstrap node has responded, insert it into our space */
      routingTable.insert( otherNode )

      /* We got a response, so the error is false */
      error = false

      /* Wake up any waiting thread */
      (this as Object).notify()
   }

   /** Resends a ConnectMessage to the boot strap node a maximum of MAX_ATTEMPTS times */
   @Synchronized
   override fun timeout (conversationId : Int)
   {
      if (++this.attempts < MAX_CONNECT_ATTEMPTS)
         server.sendMessage(otherNode, KConnectMessage( node ), this)
      else
         (this as Object).notify()                                              // We just exit, so notify all other threads that are possibly waiting
   }

   override fun originName () = "KConnectOperation(${node})"
}
