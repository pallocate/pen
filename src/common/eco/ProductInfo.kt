package pen.eco

import kotlinx.serialization.Serializable
import pen.toLong
import pen.toInt

interface ProductInfo
{
   val id : Long
   val name : String
   val desc : String
   val amount : Float
   val prefix : Prefix
   val unit : Units
   val change : Int
   val price : Long
   val sensitive : Boolean
   val analogue : Boolean

   fun apu () : String
   {
      val stringBuilder = StringBuilder()

      if (prefix > Prefix.NONE)
         stringBuilder.append( prefix.tag() )

      if (unit > Units.NONE)
         stringBuilder.append( unit.tag() )

      if (!stringBuilder.isEmpty())
         if (amount > 0F)
            stringBuilder.insert( 0, amount )
         else
            stringBuilder.insert( 0, 1 )

      return stringBuilder.toString()
   }
}
class NoProductInfo : ProductInfo
{override val id = 0L; override val name = ""; override val desc = ""; override val amount = 0F;
override val prefix = Prefix.NONE; override val unit = Units.NONE; override val change = 0; override val price = 0L;
override val sensitive = false; override val analogue = false}

@Serializable
open class KProductInfo (override val id : Long = 0L, override val name : String = "", override val desc : String = "",
override val amount : Float = 0F, override val prefix : Prefix = Prefix.NONE, override val unit : Units = Units.NONE,
override val change : Int = 0, override val price : Long = 0L, override val sensitive : Boolean = false,
override val analogue : Boolean = false) : ProductInfo
{
   /** @constructor Does some parameter checking/conversion and calls primary constructor. */
   constructor (id : String, name : String, desc : String, amount : String, prefix : String, unit : String,
   change : String, price : String, sensitive : String, analogue : String) : this
   (
      id.toLong(),
      name,
      desc,
      amount.toFloat(),
      prefix.toPrefix(),
      unit.toUnit(),
      change.toInt(),
      price.toLong(),
      sensitive.toBool(),
      analogue.toBool()
   )

   override fun toString () = name
}
