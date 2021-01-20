@file:kotlinx.serialization.UseSerializers( ByteArraySerialiser::class )

package pen.par

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import pen.ByteArraySerialiser
import pen.PasswordProvider
import pen.Constants
import pen.randomBytes

@Serializable
data class KContactInfo (
   val name : String = "",
   val publicKey : ByteArray = ByteArray( 0 ),
   val icon : String = "",
   val group : String = ""
) {}

@Serializable
data class KContact (val id : Long, val info : KContactInfo = KContactInfo())
{
   companion object
   {fun void () = KContact( 0L, KContactInfo() )}

   fun isVoid () = (id == 0L)
}

@Serializable
class KMe (val id : Long, val info : KContactInfo, private val salt : ByteArray = randomBytes( Constants.SALT_SIZE ))
{
   /** @param otherKey Public key of the recipient */
   fun crypto (passwordProvider : PasswordProvider, otherKey : ByteArray = ByteArray(0)) =
      KCrypto( passwordProvider, salt, otherKey )
}
//@Serializable (with=ByteArraySerialiser::class)
