package pen.net.kad.messages

import pen.eco.common.Log
import pen.eco.common.DebugValue
import pen.eco.common.Config.getSettings
import pen.net.kad.dht.KStorageEntry
import pen.net.kad.node.KNode

/** A Message used to send content between nodes */
class KContentMessage (): Message
{
   var origin                    = KNode()
   var content                   = KStorageEntry()

   init
   { Log.debug( {"<CONTENT>"}, getSettings().getValue( DebugValue.MESSAGE_CREATE )) }

   constructor (origin : KNode, content : KStorageEntry) : this()
   {
      this.origin = origin
      this.content = content
   }

   override fun code () = Codes.CONTENT
}
