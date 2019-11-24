package pen.net.kad.messages

import pen.eco.Loggable

import pen.eco.Config
import pen.net.kad.dht.KStorageEntry
import pen.net.kad.node.KNode

/** A Message used to send content between nodes */
class KContentMessage (): Message, Loggable
{
   var origin                    = KNode()
   var content                   = KStorageEntry()

   init
   { log( {"<CONTENT>"}, Config.trigger( "KAD_MSG_CREATE" )) }

   constructor (origin : KNode, content : KStorageEntry) : this()
   {
      this.origin = origin
      this.content = content
   }

   override fun code () = Codes.CONTENT
   override fun originName () = "KContentMessage"
}
