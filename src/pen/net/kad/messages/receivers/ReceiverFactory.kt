package pen.net.kad.messages.receivers

import pen.eco.common.Config
import pen.net.kad.KKademliaNode
import pen.net.kad.messages.Codes

object ReceiverFactory
{
   fun createReceiver (code : Byte, localNode : KKademliaNode) : Receiver
   {
      var ret : Receiver = NoReceiver()

      val server = localNode.getServer()
      val node = localNode.getNode()
      val routingTable = localNode.getRoutingTable()
      val dht = localNode.getDHT()

      ret = when (code)
      {
         Codes.CONNECT                    -> KConnectReceiver( server, node, routingTable )
         Codes.FIND_VALUE                 -> KFindValueReceiver( server, node, routingTable, dht )
         Codes.FIND_NODE                  -> KFindNodeReceiver( server, node, routingTable )
         Codes.STORE                      -> KStoreReceiver( server, routingTable, dht )
         else                             -> KSimpleReceiver()
      }

      return ret
   }
}
