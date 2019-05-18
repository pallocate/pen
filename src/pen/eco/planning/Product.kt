package pen.eco.planning

import java.io.InputStream
import pen.eco.common.BlockNode
import pen.eco.common.Utils

/** This is the most basic representation of a product. */
open class Product (/** Product id */ var id : Long = 0L, /** Quantity */ var qty : Long = 0L) : BlockNode {}

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
      Utils.stringToLong( id ),
      Utils.stringToLong( qty ),
      name,
      desc,
      Utils.stringToFloat( amount ),
      prefix,
      unit,
      Utils.stringToInt( down ),
      Utils.stringToInt( up ),
      Utils.stringToLong( absolute ),
      Utils.stringToLong( price ),
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
