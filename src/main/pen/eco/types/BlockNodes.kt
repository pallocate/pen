package pen.eco.types

import java.io.InputStream
import pen.eco.Log
import pen.eco.Constants
import pen.eco.types.PasswordProvider
import pen.eco.common.Utils as CommonUtils

sealed class BlockNode ()

/** Buliding block of the planning process. */
open class Block : BlockNode()
{
   /** Signature of the hash. */
   var signature                                = ByteArray( Constants.SIGN_BYTES )
   /** The hash of the block. */
   var hash                                     = ByteArray( Constants.HASH_BYTES )
   /** The block header. */
   var header                                   = Header()
   /** Public key, used when buying stuff. */
   var publicKey                                = ByteArray( Constants.PUBKEY_BYTES )
   /** The block child nodes. */
   val children                                 = ArrayList<BlockNode>()

   /** Resets block to a vanilla state.  */
   fun vanilla ()
   {
      children.clear()
      header.version = Constants.VERSION
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
      var version                                  = Constants.VERSION
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


class Proposal : Block()


/** A binary, non parsed block.
  * @constructor Reads content from an InputStream. */
class Blob () : BlockNode()
{
   var signature                 = ByteArray( 0 )
   var hash                      = ByteArray( 0 )
   var size                      = 0L
   var publicKey                 = ByteArray( 0 )
   var content                   = ByteArray( 0 )

//   constructor (content : ByteArray) : this( content.inputStream() ) {}

   fun isEmpty () : Boolean
   { return content.size == 0 }
}


/** This is the most basic representation of a product. */
open class Product ( /** Product id */
                     var id : Long = 0L,
                     /** Quantity */
                     var qty : Long = 0L
                   ) : BlockNode() {}

/** A more comprehensive product with additional information.
  * @constructor Primary constructor */
class VerseProduct (id : Long = 0L, qty : Long = 0L, var name : String = "", var desc : String = "", var amount : Float = 0F,
var prefix : String = "", var unit : String = "", var down : Int = 0, var up : Int = 0, var absolute : Long = 0L,
var price : Long = 0L, var sensetive : String = "", var analogue : String = "") : Product( id, qty )
{
   /** @constructor Does parameter checking/conversion and calls primary constructor. */
   constructor (id : String, qty : String, name : String, desc : String, amount : String, prefix : String, unit : String,
   down : String, up : String, absolute : String, price : String, sensetive : String, analogue : String) : this
   (
      CommonUtils.stringToLong( id ),
      CommonUtils.stringToLong( qty ),
      name,
      desc,
      CommonUtils.stringToFloat( amount ),
      prefix,
      unit,
      CommonUtils.stringToInt( down ),
      CommonUtils.stringToInt( up ),
      CommonUtils.stringToLong( absolute ),
      CommonUtils.stringToLong( price ),
      sensetive,
      analogue
   ) {}

   constructor (product : Product) : this ()
   {
      id = product.id
      qty = product.qty
   }

   override fun toString () : String = name

   fun copy () : VerseProduct
   { return VerseProduct( id, qty, name, desc, amount, prefix, unit, down, up, absolute, price, sensetive, analogue ) }
}
