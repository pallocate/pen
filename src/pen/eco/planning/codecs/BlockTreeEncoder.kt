package pen.eco.planning.codecs

import pen.eco.common.Log
import pen.eco.common.Utils
import pen.eco.common.Config
import pen.eco.common.Crypto
import pen.eco.planning.Constants
import pen.eco.planning.Block
import pen.eco.planning.Blob
import pen.eco.planning.Product

/** Encodes a whole tree of blocks and blobs. Useful in testing, and after a tree edit.*/
class BlockTreeEncoder (val block : Block) : Encoder
{
   companion object
   {
      /** Recursivly updates sizes and hashes. */
      fun sizesAndHashes (block : Block)
      {
         Log.debug( "Updating block tree sizes and hashes" )
         var productData = ByteArray( 0 )
         var childrensHash = ByteArray( 0 )
         var sizeOfChildren = 0L

         if (block.isProposal())
         {
            for (child in block.children)
               if (child is Product)
                  productData = productData +
                  CodecUtils.encodeLong( Constants.PRODUCT_ID_BYTES, child.id ) +
                  CodecUtils.encodeLong( Constants.PRODUCT_QTY_BYTES, child.qty )
         }
         else
            for (child in block.children)
            {
               if (child is Block)
               {
                  sizesAndHashes( child )                                       // Child recursion
                  childrensHash += child.hash                                   // Add child hash
                  sizeOfChildren += child.header.size                           // Add child size
               }
               else
                  if (child is Blob)
                  {
                     childrensHash += child.hash                                // Add child hash
                     sizeOfChildren += child.size                               // Add child size
                  }
            }
         block.header.size = (Config.SIGN_BYTES + Config.HASH_BYTES + Constants.HEADER_BYTES + Constants.PUBKEY_BYTES + productData.size + sizeOfChildren).toLong() // Calculate size
         val headerData = CodecUtils.encodeHeader( block.header, block.children.size )
         block.hash = Crypto.digest( headerData + block.publicKey + productData + childrensHash ) // Hash
      }
   }

   /** Recursivly encodes a block tree.
     * @param block The block to convert. */
   override fun encode () : ByteArray
   {
      Log.debug( "Encodes block tree" )
      val binaryData = ArrayList<Byte>()

      sizesAndHashes( block )
      encodeBlock( block, binaryData )

      return binaryData.toByteArray()
   }

   private fun encodeBlock (block : Block, binaryData : ArrayList<Byte>)
   {
      binaryData.addAll( block.signature.toList() )                             // Signature
      binaryData.addAll( block.hash.toList() )                                  // Hash
      val headerData = CodecUtils.encodeHeader( block.header, block.children.size )
      binaryData.addAll( headerData.toList() )                                  // Header
      binaryData.addAll( block.publicKey.toList() )                             // Public key

      if (block.isProposal())
      {
         for (child in block.children)
            if (child is Product)
            {
               val binaryProduct = CodecUtils.encodeLong( Constants.PRODUCT_ID_BYTES, child.id ) +
               CodecUtils.encodeLong( Constants.PRODUCT_QTY_BYTES, child.qty )
               binaryData.addAll( binaryProduct.toList() )                      // Products
            }
      }
      else
         for (child in block.children)
            if (child is Block)
               encodeBlock( child, binaryData )                                 // Recurse down the tree
            else
               if (child is Blob && !child.isEmpty())
                  binaryData.addAll( child.content.toList() )
   }
}
