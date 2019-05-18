package pen.net.kad.utils

import pen.eco.common.Convertable
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

      for (contact in contactsInfo)
         kRoutingTable.insert( contact )

      return kRoutingTable
   }

   override fun getConverters () = arrayOf( KNodeIdConverter(), KInetAddressConverter() )
}
