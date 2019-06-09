package pen.net.kad.messages

import pen.eco.Log

import pen.eco.Config
import pen.net.kad.StorageEntry
import pen.net.kad.dht.KStorageEntry
import pen.net.kad.node.KNode

/** A KStoreMessage used to send a store message to a node */
class KStoreMessage () : Message
{
   var origin                    = KNode()
   var payload                   = KStorageEntry()

   init
   { Log.debug( {"<STORE>"}, Config.flag( "MESSAGE_CREATE" )) }

   constructor (origin : KNode, payload : KStorageEntry) : this()
   {
      this.origin = origin
      this.payload = payload
   }

   override fun code () = Codes.STORE
}
