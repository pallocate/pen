package pen.eco.planning

import java.io.OutputStream
import java.security.MessageDigest
import pen.eco.common.Config
import pen.eco.common.Crypto
import pen.eco.common.Log
import pen.eco.planning.codecs.BlockEncoder
import pen.eco.planning.codecs.CodecUtils

/** Recursive checking. */
object BlockChecker
{
   /** Checks the block recursively.
     * @param level How many levels down to check.
     * @return True if it passed the checks. */
   fun check (block : Block, levels : Int) : Boolean
   {
      Log.debug( "Checking block ${block.idString()}" )
      val success = checkHelper( block, levels )
      if (!success)
         Log.warn( "Block check failed!" )

      return success
   }

   private fun checkHelper (block : Block, levels : Int) : Boolean
   {
      var success = true
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
            if (child is Block)
            {
               if (levels > 0)
                  success = checkHelper( child, levels - 1 )                    // Dive down the branches

               childrensHash += child.hash                                      // Get child hash
               sizeOfChildren += child.header.size                              // Get child size
            }
            else
               if (child is Blob)
               {
                  childrensHash += child.hash                                   // Get child hash
                  sizeOfChildren += child.size                                  // Get child size
               }

      val tmpSize = (Config.SIGN_BYTES + Config.HASH_BYTES + Constants.HEADER_BYTES + Constants.PUBKEY_BYTES + productData.size + sizeOfChildren).toLong() // Calculate size
      val headerData = CodecUtils.encodeHeader( block.header, block.children.size )
      val tmpHash = Crypto.digest( headerData + block.publicKey + productData + childrensHash ) // Hash

      success = success && (tmpSize == block.header.size) && (tmpHash contentEquals block.hash)
      return success
   }
}
