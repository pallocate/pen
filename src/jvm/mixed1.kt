package pen

import kotlinx.serialization.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.descriptors.*
import org.apache.commons.codec.binary.Hex
import org.bouncycastle.jcajce.provider.digest.SHA3

fun String.parseAsHex () = Hex.decodeHex( this )

fun sha3Digest (bytes : ByteArray) = SHA3.Digest256().digest( bytes )

/* TODO: A more collision safer algorithm. */
fun generateId () = now()

@Serializer( forClass = ByteArray::class )
object ByteArraySerialiser : KSerializer<ByteArray>
{
   override val descriptor : SerialDescriptor = PrimitiveSerialDescriptor( "ByteArraySerialiser", PrimitiveKind.STRING )

   override fun serialize (encoder : Encoder, value : ByteArray)
   {
      encoder.encodeString( value.toHex() )
   }

   override fun deserialize (decoder : Decoder) : ByteArray
   {
      return decoder.decodeString().parseAsHex()
   }
}
