package pen.par

import java.io.Serializable
import pen.eco.Log
import pen.eco.Crypto
import pen.eco.types.PasswordProvider
import pen.eco.types.NoPasswordProvider

/** Containing contact and authentication information. */
class Me (val contact : Contact = Contact()) : Serializable
{
   private var skc_salt                         = ByteArray( 0 )
   private var pkc_salt                         = ByteArray( 0 )

   /** @param passwordProvider A valid PasswordProvider is necessary to create a new public key.
     * @return Stored key if one exists, otherwise tries to create a new one. */
   fun publicKey (passwordProvider : PasswordProvider = NoPasswordProvider()) : ByteArray
   {
      var ret : ByteArray

      if (contact.publicKey.size != Crypto.publicSigningKeySize())
         contact.publicKey = Crypto.getKey( passwordProvider, pkcSalt(), Crypto::publicKey )

      ret = contact.publicKey

      return ret
   }

   /** Salt for use when generating a symetric key.
     * @return Stored salt if it exists, otherwise generates new salt. */
   fun skcSalt () : ByteArray
   {
      if (skc_salt.size != Crypto.saltSize())
         skc_salt = Crypto.randomBytes( Crypto.saltSize() )

      return skc_salt
   }

   /** Salt for use when generating a asymetric keypair.
     * @return Stored salt if it exists, otherwise generates new salt. */
   fun pkcSalt () : ByteArray
   {
      if (pkc_salt.size != Crypto.saltSize())
         pkc_salt = Crypto.randomBytes( Crypto.saltSize() )

      return pkc_salt
   }
}