package pen.eco

import kotlinx.serialization.Serializable
import pen.toLong

@Serializable
data class KProduct (val id : Long = 0L, val qty : Long = 0L)
{
   /** @constructor Does parameter checking/conversion and calls primary constructor. */
   constructor (id : String, qty : String) : this( id.toLong(), qty.toLong()) {}
}
