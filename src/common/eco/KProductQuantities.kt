package pen.eco

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import pen.Voidable

/** @deprecated KProposal has been deprecated. It is replaced with the more general "KProductQuatities" */
typealias KProposal=KProductQuantities

class KProductQuantity (val id : Long, var qty : Long = 0L)
{
   fun toPair () = Pair( id, qty )
   override fun toString () = qty.toString()
}

@Serializable
@SerialName("ProductQuantities")
class KProductQuantities (@SerialName("meta") val pqMeta : PqMeta = KPqMeta()) : Voidable
{
   /** Mapping of product id:s to quantities. */
   @SerialName("products")
   val hashMap = HashMap<Long,Long>()

   companion object
   { fun void () = KProductQuantities() }

   fun plus (id : Long, qty : Long)
   {
      var productSaldo = hashMap.getOrElse( id, {0L} )

      productSaldo += qty
      hashMap.put( id, productSaldo )
   }

   fun toList () = hashMap.toList()
   override fun isVoid () = (pqMeta.year == 0)
}

fun Pair<Long,Long>.toProductQuantity () = KProductQuantity( first, second )
