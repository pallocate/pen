package pen.par

import pen.Crypto
import pen.PasswordProvider
import pen.NoPasswordProvider

interface Contact
class NoContact : Contact

interface Me

/** Contact information. */
open class KContact () : Contact
{
   protected var salt                                   = ByteArray( 0 )
   var contactId : Long                                 = 0L
   var name : String                                    = ""
   var publicKey : ByteArray                            = ByteArray( 0 )
   var group : String                                   = ""
   var icon : String                                    = ""

   constructor (contactId : Long, name : String, publicKey : ByteArray, group : String, icon : String) : this()
   {
      this.contactId = contactId
      this.name = name
      this.publicKey = publicKey
      this.group = group
      this.icon = icon
   }
   constructor (contactId : Long, name : String) : this()
   {
      this.contactId = contactId
      this.name = name
   }
}

class KMe () : Me, KContact ()
{
   constructor (contactId : Long, name : String) : this()
   {
      this.contactId = contactId
      this.name = name
   }

   /** @param passwordProvider A valid PasswordProvider is necessary to create a new public key.
     * @return Stored key if one exists, otherwise tries to create a new one. */
   fun publicKey (passwordProvider : PasswordProvider) =
      if (publicKey.size != Crypto.publicSigningKeySize() && passwordProvider !is NoPasswordProvider)
         Crypto.getKey( passwordProvider, this.salt(), Crypto::publicKey )
      else
         publicKey

   /** Salt for use when generating keys.
     * @return Stored salt if it exists, otherwise generates new salt. */
   fun salt () : ByteArray
   {
      if (salt.size != Crypto.saltSize())
         salt = Crypto.randomBytes( Crypto.saltSize() )

      return salt
   }
}
