package pen.eco

import pen.Utils
import pen.toInt
import pen.toLong
import pen.toFloat

/** Giving a participant of the economy the necessary and updated information concerning a product.
  * @constructor Primary constructor */
open class KProduct  (
                        open val id : Long             = 0L,
                        open val name : String         = "",
                        open val desc : String         = "",
                        open val amount : Float        = 0F,
                        open val prefix : String       = "",
                        open val unit : String         = "",
                        open val down : Int            = 0,
                        open val up : Int              = 0,
                        open val absolute : Long       = 0L,
                        open val price : Long          = 0L,
                        open val sensetive : String    = "",
                        open val analogue : String     = ""
                     )
{
   /** @constructor Does parameter checking/conversion and calls primary constructor. */
   constructor (id : String, name : String, desc : String, amount : String, prefix : String, unit : String,
   down : String, up : String, absolute : String, price : String, sensetive : String, analogue : String) : this
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
      sensetive,
      analogue
   )

   constructor (item : KItem) : this ( item.id ) {}

   fun toKItem (quantity : Long) = KItem( id, quantity )

   fun copy () = KProduct( id, name, desc, amount, prefix, unit, down, up, absolute, price, sensetive, analogue )

   /** Encodes a product to String.
     * @return Returns The product in text form. */
   open fun encode () : String
   {
      var ret = "\n" + "[PRODUCT]\n"

      ret += "Id:         ${id}\n"
      ret += "Name:       ${name}\n"

      if (name.length > 0)
         ret += "Desc:       ${desc}\n"
      if (amount != 0F)
         ret += "Amount:     ${amount}\n"
      if (prefix != "" && prefix != "None")
         ret += "Prefix:     ${prefix}\n"
      if (unit != "" && unit != "None")
         ret += "Unit:       ${unit}\n"
      if (down != 0)
         ret += "Down:       ${down}\n"
      if (up != 0)
         ret += "Up:         ${up}\n"
      if (absolute != 0L)
         ret += "Absolute:   ${absolute}\n"

      ret += "Price:      ${price}\n"

      if (sensetive != "" && sensetive != "false")
         ret += "Sensetive:   ${sensetive}\n"

      if (analogue != "" && analogue != "false")
         ret += "Analogue:   ${analogue}\n"

      return ret
   }

   override fun toString () = name
}
