package pen.net.kad.dht

import com.beust.klaxon.Converter
import pen.eco.Convertable
import pen.net.kad.StorageEntry

/** A StorageEntry class that is used to store a content on the DHT */
class KStorageEntry () : StorageEntry, Convertable
{
   var content                                    = KContent()
   var contentMetadata                            = KStorageEntryMetadata()

   constructor (content : KContent, contentMetadata : KStorageEntryMetadata = KStorageEntryMetadata( content )) : this()
   {
      this.content = content
      this.contentMetadata = contentMetadata
   }
}
