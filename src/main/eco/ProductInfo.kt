package pen.eco

import kotlinx.serialization.Serializable
import pen.toLong
import pen.toLong
import pen.toInt
import pen.toFloat

interface ProductInfo
{
   val id : Long
   val name : String
   val desc : String
   val amount : Float
   val prefix : String
   val unit : String
   val down : Int
   val up : Int
   val absolute : Long
   val price : Long
   val sensitive : String
   val analogue : String
}
class NoProductInfo : ProductInfo
{override val id = 0L; override val name = ""; override val desc = ""; override val amount = 0F; override val prefix = "";
override val unit = ""; override val down = 0; override val up = 0; override val absolute = 0L; override val price = 0L;
override val sensitive = ""; override val analogue = ""}

@Serializable
open class KProductInfo (override val id : Long = 0L, override val name : String = "", override val desc : String = "",
override val amount : Float = 0F, override val prefix : String = "", override val unit : String = "", override val down : Int = 0,
override val up : Int = 0, override val absolute : Long = 0L, override val price : Long = 0L, override val sensitive : String = "",
override val analogue : String = "") : ProductInfo
{
   /** @constructor Does some parameter checking/conversion and calls primary constructor. */
   constructor (id : String, name : String, desc : String, amount : String, prefix : String, unit : String,
   down : String, up : String, absolute : String, price : String, sensitive : String, analogue : String) : this
   (
      id.toLong(),
      name,
      desc,
      amount.toFloat(),
      prefix,
      unit,
      down.toInt(),
      up.toInt(),
      absolute.toLong(),
      price.toLong(),
      sensitive,
      analogue
   )

   override fun toString () = name
}

class KQuantableProductInfo (id : Long = 0L, name : String = "", desc : String = "", amount : Float = 0F, prefix : String = "",
unit : String = "", down : Int = 0, up : Int = 0, absolute : Long = 0L, price : Long = 0L, sensitive : String = "", analogue : String = ""
) : KProductInfo( id, name, desc, amount, prefix, unit, down, up, absolute, price, sensitive, analogue )
{
   var qty = 0L

   constructor (productInfo : KProductInfo, qty : Long = 0L) : this (
      productInfo.id,
      productInfo.name,
      productInfo.desc,
      productInfo.amount,
      productInfo.prefix,
      productInfo.unit,
      productInfo.down,
      productInfo.up,
      productInfo.absolute,
      productInfo.price,
      productInfo.sensitive,
      productInfo.analogue
   )
   { this.qty = qty }
}
