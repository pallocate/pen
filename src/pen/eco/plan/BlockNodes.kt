package pen.eco.plan

import java.io.InputStream
import pen.eco.Log
import pen.eco.common.Crypto
import pen.eco.Config
import pen.eco.common.PasswordProvider

interface BlockNode

/** Buliding block of the planning process. */
open class Block () : BlockNode
{
   /** Signature of the hash. */
   var signature                                = ByteArray( Config.SIGN_BYTES )
   /** The hash of the block. */
   var hash                                     = ByteArray( Config.HASH_BYTES )
   /** The block header. */
   var header                                   = Header()
   /** Public key, used when buying stuff. */
   var publicKey                                = ByteArray( Constants.PUBKEY_BYTES )
   /** The block child nodes. */
   val children                                 = ArrayList<BlockNode>()

   /** @constructor Parses a Blob into this Block. */
   constructor (blob : Blob) : this()
   {
      Log.debug( "Converting blob to block" )
// TODO:
//      BlockDeserializer.deserialize( this, 0, ByteArrayInputStream( blob.content ))
   }

   /* Encodes and signes the block.
   open fun signed (passwordProvider : PasswordProvider, salt : ByteArray) : ByteArray
   {
      Log.debug( "Encoding and signing block" )
      val binaryBlock = BlockEncoder( this ).encode()
      return Crypto.signatureOf( hash, passwordProvider, salt ) + binaryBlock
      return ByteArray( 0 )
   }*/

   /** Resets block to a vanilla state.  */
   fun vanilla ()
   {
      children.clear()
      header.version = Config.VERSION
      header.timestamp = 0L
   }

   /** If this is a proposal (only has Product´s as children). */
   fun isProposal () : Boolean
   { return header.isFlagSet( Constants.IS_PROPOSAL ) }
   /** If this is a production block, otherwise consumption. */
   fun isProduction () : Boolean
   { return header.isFlagSet( Constants.IS_PRODUCTION ) }

   /** A string representing the state of the planning process. */
   fun progression () : String = header.year.toString() + ":" + header.iteration.toString()
   /** Returns a string that can be used to identify the block header in a log file. */
   fun idString () : String = progression() + " (" + header.id + ")"

   /** The header of the block. */
   class Header ()
   {
      /** Total size of the block. */
      var size                                     = 0L
      /** Federative level in the economy. */
      var level                                    = 0
      /** Flags */
      var flags                                    = 0
      /** User id */
      var id                                       = 0L
      /** Link to an account by the user, stored externally. */
      var link                                     = 0L
      /** The year of the planning process. */
      var year                                     = 0
      /** Iteration number of the planning process. */
      var iteration                                = 0
      /** Spec version. */
      var version                                  = Config.VERSION
      /** Epoch seconds timestamp. */
      var timestamp                                = 0L

      /** Makes a copy of the header. */
      fun copy () : Header
      {
         val ret = Header()
         ret.apply(
         {
            level = level
            flags = flags
            id = id
            link = link
            year = year
            iteration = iteration
            version = version
            timestamp = timestamp
         })

         return ret
      }

      /** Sets the specified flag. */
      fun setFlag (flag : Int, state : Boolean = true)
      {
         if (state)
            flags = flags or flag
         else
            flags = flags and flag.inv()
      }
      /** Tests status of the specified flag.
        * @return True if the flag is set. */
      fun isFlagSet (flag : Int) : Boolean = (flags and flag > 0)
   }
}


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
         size = Utils.deserializeLong( Constants.SIZE_BYTES, inputStream )
         inputStream.read( restOfHeader )
         inputStream.read( publicKey )

         content = signature + hash + Utils.encodeLong( Constants.SIZE_BYTES, size ) + restOfHeader + publicKey
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



/** This is the most basic representation of a product. */
open class Product (
                     /** Product id */
                     var id : Long = 0L,
                     /** Quantity */
                     var qty : Long = 0L
                   ) : BlockNode {}
