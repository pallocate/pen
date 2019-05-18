package pen.eco.planning.codecs

import java.time.Instant
import pen.eco.common.Log
import pen.eco.common.Config
import pen.eco.common.Crypto
import pen.eco.planning.Constants
import pen.eco.planning.Block
import pen.eco.planning.Blob
import pen.eco.planning.Product

class BlockEncoder (val block : Block) : Encoder
{
   /** Encodes a block to bytes. */
   override fun encode () : ByteArray
   {
      var productData = ByteArray( 0 )
      var blobData = ByteArray( 0 )
      var blobHashes = ByteArray( 0 )
      var nrOfChildren = 0
      Log.debug( "Encoding block" )

      if (block.isProposal())
      {
         for (blockNode in block.children)
            if (blockNode is Product)
            {
               /* Product data */
               productData = productData +
               CodecUtils.encodeLong( Constants.PRODUCT_ID_BYTES, blockNode.id ) +
               CodecUtils.encodeLong( Constants.PRODUCT_QTY_BYTES, blockNode.qty )
               nrOfChildren += 1
            }
      }
      else
      {
         for (blockNode in block.children)
            if (blockNode is Blob)
            {
               /* Blob hashes */
               blobHashes += blockNode.hash
               /* Blob data */
               blobData += blockNode.content
               nrOfChildren += 1
            }
      }

      /* Size */
      block.header.size = (Config.SIGN_BYTES + Config.HASH_BYTES + Constants.PUBKEY_BYTES + Constants.HEADER_BYTES + productData.size + blobData.size).toLong()
      /* Timestamp */
      if (block.header.timestamp == 0L) block.header.timestamp = Instant.now().getEpochSecond()
      /* Header */
      val headerData = CodecUtils.encodeHeader( block.header, nrOfChildren )
      /* Hash */
      block.hash = Crypto.digest( headerData + block.publicKey + productData + blobHashes )

      return block.hash + headerData + block.publicKey + productData + blobData
   }
}
