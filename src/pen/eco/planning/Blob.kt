package pen.eco.planning

import java.io.InputStream
import java.io.ByteArrayInputStream
import pen.eco.common.Log
import pen.eco.common.BlockNode
import pen.eco.common.Config
import pen.eco.planning.codecs.CodecUtils

/** A binary, non parsed block.
  * @constructor Reads content from an InputStream. */
class Blob (inputStream : InputStream) : BlockNode
{
   val signature : ByteArray
   val hash : ByteArray
   val size : Long
   val publicKey : ByteArray
   var content = ByteArray( 0 )
      private set

   init
   {
      Log.debug( "Reading blob" )
      if (inputStream.available() >= Constants.META_BYTES)
      {
         val headerRest = Constants.HEADER_BYTES - Constants.SIZE_BYTES
         val signHashHeaderSize = Config.SIGN_BYTES + Config.HASH_BYTES + Constants.HEADER_BYTES

         signature = ByteArray( Config.SIGN_BYTES )
         hash = ByteArray( Config.HASH_BYTES )
         val restOfHeader = ByteArray( headerRest )
         publicKey = ByteArray( Constants.PUBKEY_BYTES )

         inputStream.read( signature )
         inputStream.read( hash )
         size = CodecUtils.decodeLong( Constants.SIZE_BYTES, inputStream )
         inputStream.read( restOfHeader )
         inputStream.read( publicKey )

         content = signature + hash + CodecUtils.encodeLong( Constants.SIZE_BYTES, size ) + restOfHeader + publicKey
         val dataSize = size.toInt() - Constants.META_BYTES

         if (inputStream.available() >= dataSize)
         {
            val data = ByteArray( dataSize )
            inputStream.read( data )
            content = content + data
         }
      }
      else
      {
         signature = ByteArray( 0 )
         hash = ByteArray( 0 )
         size = 0L
         publicKey = ByteArray( 0 )
         Log.warn( "Blob read failed! (insufficient data)" )
      }
   }

   constructor (content : ByteArray) : this( content.inputStream() ) {}

   fun isEmpty () : Boolean
   { return content.size == 0 }
}
