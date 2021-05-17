package pen.eco

import kotlinx.serialization.serializer
import kotlinx.serialization.json.Json
import pen.KCrypto

/** As an alternative to map products with quantities. */
typealias ProductMap = HashMap<Long,Long>
fun ProductMap.addProduct (productId : Long, qty : Long)
{
   var saldo = getOrElse( productId, {0L} )

   saldo += qty
   put( productId, saldo )
}
fun ProductMap.toJson () = Json.encodeToString( serializer(), this )
fun ProductMap.signature (crypto : KCrypto = KCrypto.void()) = if (crypto.isVoid())
      ByteArray( 0 )
   else
      crypto.signDetached( toJson().toByteArray() )
