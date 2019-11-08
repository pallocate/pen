package pen.net.kad.messages

import pen.eco.Log
import pen.eco.Config
import pen.net.kad.node.KNode

/** A message sent to another node requesting to connect to them */
class KConnectMessage () : Message
{
   var origin : KNode = KNode()

   init
   { Log.debug( {"<CONNECT>"}, Config.flag( "KAD_MSG_CREATE" )) }

   constructor (origin : KNode) : this()
   { this.origin = origin }

   override fun code () = Codes.CONNECT
}
