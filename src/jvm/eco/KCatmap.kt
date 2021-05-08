@file:kotlinx.serialization.UseSerializers( ByteArraySerialiser::class )
package pen.eco

import pen.ByteArraySerialiser
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.*
import pen.KCrypto

/** Contains a map, mapping product category id´s to an amount.
  * This migt be used as an alternative way to represent a proposal,
  * or to keep track of spendings as per category.. */
@Serializable
@SerialName( "Catmap" )
class KCatmap (val signatoryId : Long)
{
   val map = HashMap<Long,Int>()
   
   fun add (categoryId : Long, amount : Int)
   {
      var saldo = map.getOrElse(categoryId, { 0 })

      saldo += amount
      map.put( categoryId, amount )
   }

   override fun toString () = Json.encodeToString( serializer(), this )
   
   fun sign (crypto : KCrypto = KCrypto.void()) : KSignedCatmap
   { 
      val signature = if (crypto.isVoid())
         ByteArray( 0 )
      else
         crypto.signDetached( toString().toByteArray() )

      return KSignedCatmap( this, signature )
   }
}

@Serializable
@SerialName( "SignedCatmap" )
class KSignedCatmap (val catmap : KCatmap, val signature : ByteArray) 
{ override fun toString () = Json.encodeToString(serializer(), this) }
