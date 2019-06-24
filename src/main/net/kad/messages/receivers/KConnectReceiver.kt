package pen.net.kad.messages.receivers

import pen.net.kad.KServer
import pen.net.kad.messages.Message
import pen.net.kad.messages.KConnectMessage
import pen.net.kad.messages.KAcknowledgeMessage
import pen.net.kad.node.KNode
import pen.net.kad.routing.KRoutingTable

/** Receives a ConnectMessage and sends an AcknowledgeMessage as reply. */
class KConnectReceiver (private val server : KServer, private val kNode : KNode, private val kRoutingTable : KRoutingTable) : Receiver
{
   /** Handle receiving a ConnectMessage */
   override fun receive (message : Message, conversationId : Int)
   {
      if (message is KConnectMessage)
      {
         /* Update the local space by inserting the origin node. */
         kRoutingTable.insert( message.origin )

         /* Respond to the connect request */
         val msg = KAcknowledgeMessage( kNode )
         server.reply( message.origin, msg, conversationId )
      }
   }
}
