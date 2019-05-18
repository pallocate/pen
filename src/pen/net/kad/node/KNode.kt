package pen.net.kad.node

import java.io.Serializable
import java.net.InetAddress
import java.net.InetSocketAddress
import pen.eco.common.Log
import pen.eco.common.Convertable
import pen.net.kad.utils.KNodeIdConverter
import pen.net.kad.utils.KInetAddressConverter

/** A Node in the Kademlia network - Contains basic node network information. */
class KNode () : Convertable
{
   var nodeId                                     = KNodeId()
   var inetAddress                                = InetAddress.getLocalHost()
   var port                                       = 0

   constructor (nodeId : KNodeId, inetAddress : InetAddress, port : Int) : this()
   {
      this.nodeId = nodeId
      this.inetAddress = inetAddress
      this.port = port
   }

   /** Creates a SocketAddress for this node */
   fun getSocketAddress () = InetSocketAddress( inetAddress, port )

   override fun equals (other : Any?) : Boolean
   {
      var ret = false
      if (other is KNode)
      {
         if (other === this)
            ret = true
         else
            ret = nodeId.equals( other.nodeId )
      }

      return ret
   }

   override fun hashCode() = nodeId.hashCode()
   override fun toString () = nodeId.shortName()

   override fun getConverters () = arrayOf( KNodeIdConverter(), KInetAddressConverter() )
}
