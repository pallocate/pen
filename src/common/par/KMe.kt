@file:kotlinx.serialization.UseSerializers( ByteArraySerialiser::class )

package pen.par

import pen.ByteArraySerialiser
import kotlinx.serialization.Serializable
import pen.PasswordProvider
import pen.Constants
import pen.randomBytes

expect fun ed25519Sha3 (seed : ByteArray) : IrohaCrypto

interface IrohaCrypto
{
   fun sign (input : ByteArray) : ByteArray
   fun publicKey () : ByteArray
}

@Serializable
class KMe (val id : Long, val info : KContactInfo, private val salt : ByteArray = randomBytes( Constants.SALT_SIZE ))
{
   fun crypto (passwordProvider : PasswordProvider) =
      KCrypto( passwordProvider, salt )

   fun irohaCrypto (passwordProvider : PasswordProvider) : IrohaCrypto
   {
      val seed = crypto( passwordProvider ).deriveKey()
      return KEd25519Sha3( seed )
   }
}
