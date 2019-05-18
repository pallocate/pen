package pen.net.kad.messages

import pen.eco.common.Log
import pen.eco.common.DebugValue
import pen.eco.common.Config.getSettings
import pen.net.kad.StorageEntry
import pen.net.kad.dht.KStorageEntry
import pen.net.kad.node.KNode

/** A KStoreMessage used to send a store message to a node */
class KStoreMessage () : Message
{
   var origin                    = KNode()
   var payload                   = KStorageEntry()

   init
   { Log.debug( {"<STORE>"}, getSettings().getValue( DebugValue.MESSAGE_CREATE )) }

   constructor (origin : KNode, payload : KStorageEntry) : this()
   {
      this.origin = origin
      this.payload = payload
   }

   override fun code () = Codes.STORE
}
