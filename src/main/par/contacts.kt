package pen.par

import kotlinx.serialization.Serializable
import pen.Crypto
import pen.PasswordProvider
import pen.NoPasswordProvider

interface Contact
class NoContact : Contact
interface Me

/** Contact information. */
@Serializable
open class KContact (var contactId : Long = 0L, var name : String = "", var publicKey : ByteArray = ByteArray( 0 ),
var group : String = "", var icon : String = "") : Contact {}

@Serializable
class KMe () : Me, KContact ()
{
   var salt = ByteArray( 0 )

   /** @param passwordProvider A valid PasswordProvider is necessary to create a new public key.
     * @return Stored key if one exists, otherwise tries to create a new one. */
   fun publicKey (passwordProvider : PasswordProvider) : ByteArray
   {
      if (publicKey.size != Crypto.publicSigningKeySize() && passwordProvider !is NoPasswordProvider)
         publicKey = Crypto.getKey( passwordProvider, this.salt(), Crypto::publicKey )

      return publicKey
   }

   /** Salt for use when generating keys.
     * @return Stored salt if it exists, otherwise generates new salt. */
   fun salt () : ByteArray
   {
      if (salt.size != Crypto.saltSize())
         salt = Crypto.randomBytes( Crypto.saltSize() )

      return salt
   }
}
