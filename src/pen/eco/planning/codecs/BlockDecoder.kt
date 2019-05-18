package pen.eco.planning.codecs

import java.io.InputStream
import pen.eco.common.Log
import pen.eco.common.Config.VERSION
import pen.eco.planning.Constants
import pen.eco.common.WrongVersionException
import pen.eco.planning.Block
import pen.eco.planning.Blob
import pen.eco.planning.Product

class BlockDecoder (val block : Block, /**To what depth the block should be decoded.*/var depth : Int) : Decoder
{
   /** Recursive decoding of a block in byte format. */
   override fun decode (inputStream : InputStream)
   {
      Log.debug( "Decoding block" )
      if (inputStream.available() >= Constants.META_BYTES)
      {
         inputStream.read( block.signature )                                    // Signature
         inputStream.read( block.hash )                                         // Hash
         val nrOfChildren = decodeHeader( inputStream )                         // Header
         inputStream.read( block.publicKey )                                    // Public key

         if (block.isProposal())
         {
            for (i in 0 until nrOfChildren)
            {
               if (inputStream.available() >= 10)
               {
                  val product = Product()
                  product.id = CodecUtils.decodeLong( Constants.PRODUCT_ID_BYTES, inputStream )
                  product.qty = CodecUtils.decodeLong( Constants.PRODUCT_QTY_BYTES, inputStream )
                  block.children.add( product )                                 // Products
               }
            }
         }
         else
            for (i in 0 until nrOfChildren)
               if (depth > 0)
               {
                  val block = Block()
                  BlockDecoder( block, depth - 1 ).decode( inputStream )        // Recursion
                  block.children.add( block )
               }
               else
               {
                  val blob = Blob( inputStream )
                  block.children.add( blob )
               }
      }
   }

   /** Decodes header from InputStream.
     * @return Nr of children. */
   private fun decodeHeader (inStream : InputStream) : Int
   {
      block.header.apply(
      {
         size = CodecUtils.decodeLong( Constants.SIZE_BYTES, inStream )
         level = (CodecUtils.decodeLong( Constants.ONE_BYTE, inStream )).toInt()
         flags = (CodecUtils.decodeLong( Constants.ONE_BYTE, inStream )).toInt()

         id = CodecUtils.decodeLong( Constants.BLOCK_ID_BYTES, inStream )
         link = CodecUtils.decodeLong( Constants.LINK_BYTES, inStream )
         year = CodecUtils.decodeLong( Constants.YEAR_BYTES, inStream ).toInt()
         iteration = CodecUtils.decodeLong( Constants.ONE_BYTE, inStream ).toInt()

         version = CodecUtils.decodeLong( Constants.VERSION_BYTES, inStream).toInt()
      })

      if (block.header.version != VERSION)
      {   Log.err( "Wrong version! " + block.header.version  )
         throw WrongVersionException() }

      val nrOfChildren = CodecUtils.decodeLong( Constants.NR_OF_CHILDREN_BYTES, inStream ).toInt()
      block.header.timestamp = CodecUtils.decodeLong( Constants.TIMESTAMP_BYTES, inStream )

      return nrOfChildren
   }
}
