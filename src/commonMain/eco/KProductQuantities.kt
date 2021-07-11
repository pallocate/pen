package pen.eco

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import pen.Voidable

/** @deprecated KProposal has been deprecated. It is replaced with the more general "KProductQuatities" */
typealias KProposal=KProductQuantities

/** Contains a mapping between a product id and a quantity. */
class KProductQuantity (val id : Long, var qty : Long = 0L)
{
   fun toPair () = Pair( id, qty )
   override fun toString () = qty.toString()
}

@Serializable
@SerialName("ProductQuantities")
class KProductQuantities (val meta : Meta = KMeta()) : Voidable
{
   /** Mappings of product id:s to quantities. */
   val products = HashMap<Long,Long>()

   companion object
   { fun void () = KProductQuantities() }

   fun plus (id : Long, qty : Long)
   {
      var productSaldo = products.getOrElse( id, {0L} )

      productSaldo += qty
      products.put( id, productSaldo )
   }

   fun toList () = products.toList()
   override fun isVoid () = (meta.year == 0)
}

fun Pair<Long,Long>.toProductQuantity () = KProductQuantity( first, second )
