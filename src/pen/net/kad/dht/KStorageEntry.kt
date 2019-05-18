package pen.net.kad.dht

import com.beust.klaxon.Converter
import pen.eco.common.Convertable
import pen.net.kad.StorageEntry
import pen.net.kad.utils.KNodeIdConverter

/** A StorageEntry class that is used to store a content on the DHT */
class KStorageEntry () : StorageEntry, Convertable
{
   companion object
   { val converters = arrayOf( KNodeIdConverter() as Converter ) }

   var content                                    = KContent()
   var contentMetadata                            = KStorageEntryMetadata()

   constructor (content : KContent, contentMetadata : KStorageEntryMetadata = KStorageEntryMetadata( content )) : this()
   {
      this.content = content
      this.contentMetadata = contentMetadata
   }

   override fun getConverters () = converters
}
