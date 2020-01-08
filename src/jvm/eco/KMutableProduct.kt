package pen.eco

import kotlinx.serialization.Serializable

@Serializable
class KMutableProduct (override var id : Long = 0L, override var qty : Long = 0L) : Product
{
   constructor (product : KProduct) : this( product.id, product.qty ) {}

   fun toKProduct () = KProduct( id, qty )
}
