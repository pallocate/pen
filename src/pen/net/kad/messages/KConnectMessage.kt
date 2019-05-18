package pen.net.kad.messages

import pen.eco.common.Log
import pen.eco.common.DebugValue
import pen.eco.common.Config.getSettings
import pen.net.kad.node.KNode

/** A message sent to another node requesting to connect to them */
class KConnectMessage () : Message
{
   var origin : KNode = KNode()

   init
   { Log.debug( {"<CONNECT>"}, getSettings().getValue( DebugValue.MESSAGE_CREATE )) }

   constructor (origin : KNode) : this()
   { this.origin = origin }

   override fun code () = Codes.CONNECT
}
