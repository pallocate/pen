package pen
/* This file should be common, but most of this is´nt supported in kotlin native */

import kotlinx.serialization.*

@Serializer( forClass = ByteArray::class )
object ByteArraySerialiser : KSerializer<ByteArray>
{
   override val descriptor : SerialDescriptor = PrimitiveDescriptor( "ByteArraySerialiser", PrimitiveKind.STRING )

   override fun serialize (encoder : Encoder, value : ByteArray)
   {
      encoder.encodeString(Utils.encodeB64( value ))
   }

   override fun deserialize (decoder : Decoder) : ByteArray
   {
      return Utils.decodeB64( decoder.decodeString().toByteArray() )
   }
}
