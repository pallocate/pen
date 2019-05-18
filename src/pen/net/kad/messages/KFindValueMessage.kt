package pen.net.kad.messages

import pen.eco.common.Log
import pen.eco.common.DebugValue
import pen.eco.common.Config.getSettings
import pen.net.kad.dht.KGetParameter
import pen.net.kad.node.KNode

/** Messages used to send to another node requesting content */
class KFindValueMessage () : Message
{
   var origin                    = KNode()
   var params                    = KGetParameter()

   init
   { Log.debug( {"<FIND_VALUE>"}, getSettings().getValue( DebugValue.MESSAGE_CREATE )) }

   constructor (origin : KNode, params : KGetParameter) : this()
   {
      this.origin = origin
      this.params = params
   }

   override fun code () = Codes.FIND_VALUE
}
