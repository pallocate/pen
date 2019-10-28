package pen.net.kad.messages.receivers

import pen.net.kad.Constants
import pen.net.kad.KServer
import pen.net.kad.messages.Message
import pen.net.kad.messages.KFindNodeMessage
import pen.net.kad.messages.KFindNodeReply
import pen.net.kad.node.KNode
import pen.net.kad.routing.KRoutingTable

/** Receives a KFindNodeMessage and sends a KFindNodeReply as reply with the K-Closest nodes to the ID sent. */
class KFindNodeReceiver (private val server : KServer, private val kNode : KNode, private val kRoutingTable : KRoutingTable) : Receiver
{
   /** Handle receiving a KFindNodeMessage
     * Find the set of K nodes closest to the lookup ID and return them */
   override fun receive (message : Message, conversationId : Int)
   {
      if (message is KFindNodeMessage)
      {
         val origin = message.origin

         /* Update the local space by inserting the origin node. */
         kRoutingTable.insert( origin )

         /* Find nodes closest to the LookupId */
         val nodes = kRoutingTable.findClosest( message.lookupId, Constants.K )

         /* Respond to the KFindNodeMessage */
         val reply = KFindNodeReply( kNode, nodes )

         if (this.server.isRunning)
         {
            /* Let the Server send the reply */
            this.server.reply( origin, reply, conversationId )
         }
      }
   }
}
