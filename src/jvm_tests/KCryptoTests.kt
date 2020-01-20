package pen.tests.eco

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.Crypto
import pen.par.KMe
import pen.tests.ExamplePasswords.password

class KCryptoTests
{
   val message = "\"Test message\"".toByteArray()
   val alice = KMe().apply { name = "Alice" }
   val bob = KMe().apply { name = "Bob" }

   @Test
   fun `Alice to Bob signed` ()
   {
      val signedMessage = Crypto.signText(message, password( 4 ), bob.salt())
      val verifiedMessage = Crypto.verifyText(signedMessage, bob.publicKey( password( 4 ) ))

      Assertions.assertArrayEquals( message, verifiedMessage )
   }

   @Test
   fun `Alice to Bob encrypted` ()
   {
      /* Alice signs message using her private key, and encrypts it using Bob´s public key. */
      val encryptedMessage = Crypto.pkcEncrypt(message, password( 3 ), alice.salt(), bob.publicKey( password( 4 ) ))
      /* Bob decrypts message using his private key, and verifies it using Alice´s public key */
      val decryptedMessage = Crypto.pkcDecrypt(encryptedMessage, password( 4 ), bob.salt(), alice.publicKey( password( 3 ) ))

      Assertions.assertArrayEquals( message, decryptedMessage )
   }

   @Test
   fun `Symetric encryption` ()
   {
      val encryptedMessage = Crypto.encrypt( message, password( 3 ), alice.salt() )
      val decryptedMessage = Crypto.decrypt( encryptedMessage, password( 3 ), alice.salt() )

      Assertions.assertArrayEquals( message, decryptedMessage )
   }
}
