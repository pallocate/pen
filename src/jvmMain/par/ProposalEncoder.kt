package pen.par

import java.io.OutputStream
import java.io.DataOutputStream
import java.io.ByteArrayOutputStream
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import pen.eco.KProductQuantities

class ProposalEncoder (outputStream : OutputStream) : AbstractEncoder()
{
   companion object
   {
      /** Encodes a productQuantities into a compact binary form. */
      fun encode (productQuantities : KProductQuantities) : ByteArray
      {
         val byteArrayOutputStream = ByteArrayOutputStream()
         ProposalEncoder( byteArrayOutputStream ).encodeSerializableValue( KProductQuantities.serializer(), productQuantities )

         return byteArrayOutputStream.toByteArray()
      }
   }

   val output = DataOutputStream( outputStream )

   override fun encodeInt (value : Int) = output.writeInt( value )
   override fun encodeLong (value : Long) = output.writeLong( value )
   override fun encodeEnum (enumDescriptor: SerialDescriptor, index: Int) = output.writeInt( index )

   override fun beginCollection (descriptor : SerialDescriptor, collectionSize : Int) : CompositeEncoder {
      encodeInt( collectionSize )
      return this
   }

   override fun endStructure (descriptor: SerialDescriptor) {}
}
