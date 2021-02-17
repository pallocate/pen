@file:kotlinx.serialization.UseSerializers( ByteArraySerialiser::class )

package pen.par

import pen.ByteArraySerialiser
import kotlinx.serialization.Serializable
import pen.PasswordProvider
import pen.Constants
import pen.randomBytes

@Serializable
class KMe (val id : Long, val info : KContactInfo, private val salt : ByteArray = randomBytes( Constants.SALT_SIZE ))
{
   /** @param othersKey Public key of the other part of the communication. */
   fun crypto (passwordProvider : PasswordProvider, othersKey : ByteArray = ByteArray(0)) =
      KCrypto( passwordProvider, salt, othersKey )
}
