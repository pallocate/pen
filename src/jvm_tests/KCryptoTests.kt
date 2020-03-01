package pen.tests.eco

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.Crypto
import pen.par.KMe
import pen.tests.ExamplePasswords.password

class KCryptoTests
{
   val message = "\"Test message\"".toByteArray()
   val alice = KMe.factory( 3L, "Alice", "", password( 3 ))
   val bob = KMe.factory( 4L, "Bob", "", password( 4 ))

   @Test
   fun `Alice to Bob signed` ()
   {
      val signedMessage = Crypto.signText(message, password( 4 ), bob.salt)
      val verifiedMessage = Crypto.verifyText( signedMessage, bob.publicKey )

      Assertions.assertArrayEquals( message, verifiedMessage )
   }

   @Test
   fun `Alice to Bob encrypted` ()
   {
      /* Alice signs message using her private key, and encrypts it using Bob´s public key. */
      val encryptedMessage = Crypto.pkcEncrypt(message, password( 3 ), alice.salt, bob.publicKey)
      /* Bob decrypts message using his private key, and verifies it using Alice´s public key */
      val decryptedMessage = Crypto.pkcDecrypt(encryptedMessage, password( 4 ), bob.salt, alice.publicKey)

      Assertions.assertArrayEquals( message, decryptedMessage )
   }

   @Test
   fun `Symetric encryption` ()
   {
      val encryptedMessage = Crypto.encrypt( message, password( 3 ), alice.salt )
      val decryptedMessage = Crypto.decrypt( encryptedMessage, password( 3 ), alice.salt )

      Assertions.assertArrayEquals( message, decryptedMessage )
   }
}
