package pen.net.kad.messages

import pen.eco.Loggable

import pen.eco.Config
import pen.net.kad.dht.KGetParameter
import pen.net.kad.node.KNode

/** Messages used to send to another node requesting content */
class KFindValueMessage () : Message, Loggable
{
   var origin                    = KNode()
   var params                    = KGetParameter()

   init
   { log( {"<FIND_VALUE>"}, Config.trigger( "KAD_MSG_CREATE" )) }

   constructor (origin : KNode, params : KGetParameter) : this()
   {
      this.origin = origin
      this.params = params
   }

   override fun code () = Codes.FIND_VALUE
   override fun originName () = "KFindValueMessage"
}
