package pen.tests.eco

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.eco.Crypto
import pen.tests.Examples.Acme
import pen.tests.Examples.Alice
import pen.tests.Examples.Bob

class KCryptoTests
{
   val message = "\"Test message\"".toByteArray()

   @Test
   fun `Alice to Bob signed` ()
   {
      val signedMessage = Crypto.signText( message, Bob.pwd, Bob.me.pkcSalt() )
      val verifiedMessage = Crypto.verifyText(signedMessage, Bob.me.publicKey( Bob.pwd ))

      Assertions.assertArrayEquals( message, verifiedMessage )
   }

   @Test
   fun `Alice to Bob encrypted` ()
   {
      /* Alice signs message using her private key, and encrypts it using Bob´s public key. */
      val encryptedMessage = Crypto.pkcEncrypt(message, Alice.pwd, Alice.me.pkcSalt(), Bob.me.publicKey( Bob.pwd ))
      /* Bob decrypts message using his private key, and verifies it using Alice´s public key */
      val decryptedMessage = Crypto.pkcDecrypt(encryptedMessage, Bob.pwd, Bob.me.pkcSalt(), Alice.me.publicKey( Alice.pwd ))

      Assertions.assertArrayEquals( message, decryptedMessage )
   }

   @Test
   fun `Symetric encryption` ()
   {
      val encryptedMessage = Crypto.encrypt( message, Acme.pwd, Acme.me.skcSalt() )
      val decryptedMessage = Crypto.decrypt( encryptedMessage, Acme.pwd, Acme.me.skcSalt() )

      Assertions.assertArrayEquals( message, decryptedMessage )
   }
}
