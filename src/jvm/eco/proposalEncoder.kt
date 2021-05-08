package pen.eco

import java.io.InputStream
import java.io.OutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeEncoder

/** Encodes a proposal to compact binary format. */
class KProposalEncoder (outputStream : OutputStream) : AbstractEncoder()
{
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

/** Decodes a proposal to compact binary format. */
class KProposalDecoder (inputStream : InputStream) : AbstractDecoder()
{
   val input = DataInputStream( inputStream )

   override fun decodeInt () = input.readInt()
   override fun decodeLong () = input.readLong()
   override fun decodeEnum (enumDescriptor : SerialDescriptor) = input.readInt()
   override fun decodeCollectionSize (descriptor : SerialDescriptor) = decodeInt()

   override fun decodeSequentially () = true
   override fun decodeElementIndex (descriptor : SerialDescriptor) = 0
}
