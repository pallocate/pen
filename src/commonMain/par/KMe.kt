@file:kotlinx.serialization.UseSerializers( ByteArraySerialiser::class )

package pen.par

//import pen.ByteArraySerialiser
import kotlinx.serialization.Serializable
import pen.PasswordProvider
import pen.randomBytes
import pen.KCrypto
import pen.IrohaSigner
import pen.ed25519Sha3

@Serializable
class KMe (val contact : KContact, private val salt : ByteArray = randomBytes( KCrypto.SALT_SIZE ))
{
   fun crypto (passwordProvider : PasswordProvider) = KCrypto( passwordProvider, salt )

   fun irohaSigner (passwordProvider : PasswordProvider) : IrohaSigner
   {
      val seed = crypto( passwordProvider ).deriveKey()
      return ed25519Sha3( seed )
   }
}
