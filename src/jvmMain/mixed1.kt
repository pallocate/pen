package pen

import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineName
import kotlinx.serialization.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import org.apache.commons.codec.binary.Hex
import org.bouncycastle.jcajce.provider.digest.SHA3

/* TODO: A more collision safer algorithm. */
fun generateId () = now()

fun slash () = File.separator

fun String.parseAsHex () = Hex.decodeHex( this )

fun sha3Digest (bytes : ByteArray) = SHA3.Digest256().digest( bytes )

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

fun newScope () = CoroutineScope(CoroutineName( name = (now()-1616336000000L).toString() ))
