package pen.par

import java.io.DataInputStream
import java.io.ByteArrayInputStream
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import pen.eco.KProductQuantities

class ProposalDecoder (val dataInputStream : DataInputStream) : AbstractDecoder()
{
   companion object
   {
      /** Decodes a productQuantities from the compact binary form. */
      fun decode (input : ByteArray) : KProductQuantities
      { 
         val dataInputStream = DataInputStream(ByteArrayInputStream( input ))
         val productQuantities = ProposalDecoder( dataInputStream ).decodeSerializableValue( KProductQuantities.serializer() )

         return productQuantities 
      }
   }

   override fun decodeInt () = dataInputStream.readInt()
   override fun decodeLong () = dataInputStream.readLong()
   override fun decodeEnum (enumDescriptor : SerialDescriptor) = dataInputStream.readInt()
   override fun decodeCollectionSize (descriptor : SerialDescriptor) = decodeInt()

   override fun decodeSequentially () = true
   override fun decodeElementIndex (descriptor : SerialDescriptor) = 0
}
