package pen.par

import java.io.Serializable
import pen.Log
import pen.Crypto
import pen.PasswordProvider
import pen.NoPasswordProvider

/** Containing contact and authentication information. */
class Me (val contact : Contact = Contact()) : Serializable
{
   private var skcSalt                         = ByteArray( 0 )
   private var pkcSalt                         = ByteArray( 0 )

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
      if (skcSalt.size != Crypto.saltSize())
         skcSalt = Crypto.randomBytes( Crypto.saltSize() )

      return skcSalt
   }

   /** Salt for use when generating a asymetric keypair.
     * @return Stored salt if it exists, otherwise generates new salt. */
   fun pkcSalt () : ByteArray
   {
      if (pkcSalt.size != Crypto.saltSize())
         pkcSalt = Crypto.randomBytes( Crypto.saltSize() )

      return pkcSalt
   }
}
