package pen.eco.planning.codecs

import java.io.InputStream
import pen.eco.common.Log
import pen.eco.common.FileOutput
import pen.eco.common.NoOutputStream
import pen.eco.planning.codecs.XMLBlockEncoder
import pen.eco.planning.Block
import pen.eco.planning.Constants

interface Encoder
{ abstract fun encode () : ByteArray }
interface Decoder
{ abstract fun decode (inputStream : InputStream) }

object CodecUtils
{
   /** Encodes header binary. */
   fun encodeHeader (header : Block.Header, nrOfChildren : Int) : ByteArray
   {
      return encodeLong( Constants.SIZE_BYTES, header.size ) +
      encodeLong( Constants.ONE_BYTE, header.level.toLong() ) +
      encodeLong( Constants.ONE_BYTE, header.flags.toLong() ) +
      encodeLong( Constants.BLOCK_ID_BYTES, header.id ) +
      encodeLong( Constants.LINK_BYTES, header.link ) +
      encodeLong( Constants.YEAR_BYTES, header.year.toLong() ) +
      encodeLong( Constants.ONE_BYTE, header.iteration.toLong() ) +
      encodeLong( Constants.VERSION_BYTES, header.version.toLong() ) +
      encodeLong( Constants.NR_OF_CHILDREN_BYTES, nrOfChildren.toLong() ) +
      encodeLong( Constants.TIMESTAMP_BYTES, header.timestamp )
   }

   /** Reads the next arbitrary sized integer from InputStream.
     * @param nrOfBytes Nr of bytes to be read from the InputStream.
     * @return The arbitrary sized integer as a Long. */
   fun decodeLong (nrOfBytes : Int, inputStream : InputStream) : Long
   {
      var ret = 0L
      val array = ByteArray( nrOfBytes )
      inputStream.read( array )

      if (array.size > 8)
         Log.warn( "Array to large" )
      else
      {
         for (i in 0 until array.size)
         {
            var shiftBy = 8*((array.size - i) - 1)
            var value : Long

            if (array[i] < 7)
               value = (255 and array[i].toInt()).toLong()
            else
               value = array[i].toLong()

            ret += (value shl shiftBy)
         }
      }

      return ret
   }

   /** Convertes a arbitrary sized integer (Long type) to a ArrayList of Bytes.
     * @param nrOfBytes Nr of bytes in the resulting array.
     * @param integer The arbitrary sized integer as a Long.
     * @return The resulting ArrayList. */
   fun encodeLong (nrOfBytes : Int, integer : Long) : ByteArray
   {
      val BYTE_MASK = 0xFFL
      var ret = ByteArray( 0 )

      if (integer > Math.pow(2.toDouble(), (nrOfBytes.toDouble()*8) - 1))
         Log.warn( "Long value to large" )
      else
         for (i in (nrOfBytes - 1) downTo 0)
            ret += (integer shr (8*i) and BYTE_MASK).toByte()

      return ret
   }

   /** Writes a block as XML to file. */
   fun writeBlockXML (block : Block, filename : String)
   {
      Log.debug( "Writing block XML-file $filename" )
      val outputStream = FileOutput( filename ).getOutputStream()
      if (!(outputStream is NoOutputStream))
      {
         try
         {
            val xmlBlockEncoder = XMLBlockEncoder( block )
            val xmlBlock = xmlBlockEncoder.encode()
            outputStream.write( xmlBlock )
         }
         catch (e : Exception)
         { Log.warn( "Block XML-file write failed!" ) }
         finally
         { outputStream.close() }
      }
   }
}
