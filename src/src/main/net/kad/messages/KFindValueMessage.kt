package pen.net.kad.messages

import pen.eco.Log

import pen.eco.Config
import pen.net.kad.dht.KGetParameter
import pen.net.kad.node.KNode

/** Messages used to send to another node requesting content */
class KFindValueMessage () : Message
{
   var origin                    = KNode()
   var params                    = KGetParameter()

   init
   { Log.debug( {"<FIND_VALUE>"}, Config.flag( "MESSAGE_CREATE" )) }

   constructor (origin : KNode, params : KGetParameter) : this()
   {
      this.origin = origin
      this.params = params
   }

   override fun code () = Codes.FIND_VALUE
}
