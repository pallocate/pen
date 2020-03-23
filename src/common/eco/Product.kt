package pen.eco

import kotlinx.serialization.Serializable
import pen.Utils
import pen.toInt
import pen.toLong

interface Product
{
   val id : Long
   val qty : Long
}

@Serializable
class KProduct (override val id : Long = 0L, override val qty : Long = 0L) : Product
{
   /** @constructor Does parameter checking/conversion and calls primary constructor. */
   constructor (id : String, qty : String) : this( id.toLong(), qty.toLong()) {}

   fun copy () = KProduct( id, qty )
}
