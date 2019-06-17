package pen.net.kad.utils

import pen.eco.Convertable
import pen.net.kad.node.KNode
import pen.net.kad.routing.KRoutingTable
import pen.net.kad.routing.KContact

class KSerializableRoutingInfo () : Convertable
{
   var localNodeInfo                              = KNode()
   var contactsInfo                               = ArrayList<KContact>()

   constructor (routingTable : KRoutingTable) : this()
   {
      localNodeInfo = routingTable.node
      contactsInfo = routingTable.allContacts()
   }

   fun toRoutingTable () : KRoutingTable
   {
      val kRoutingTable = KRoutingTable()

      kRoutingTable.node = localNodeInfo
      for (contact in contactsInfo)
         kRoutingTable.insert( contact )

      return kRoutingTable
   }
}
