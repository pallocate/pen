package pen.net.kad.operations

import pen.net.kad.KServer
import pen.net.kad.KKademliaNode
import pen.net.kad.dht.KDHT
import pen.net.kad.node.KNode
import pen.net.kad.routing.KRoutingTable

/** An operation that handles refreshing the entire Kademlia Systems including buckets and content */
class KKadRefreshOperation (private val server : KServer, private val node : KNode, private val routingTable : KRoutingTable, private val dht : KDHT) : Operation
{
   override fun execute ()
   {
      /* Run our BucketRefreshOperation to refresh buckets */
      KBucketRefreshOperation( server, node, routingTable, dht ).execute()

      /* After buckets have been refreshed, we refresh content */
      KContentRefreshOperation( server, node, routingTable, dht ).execute()
   }
}
