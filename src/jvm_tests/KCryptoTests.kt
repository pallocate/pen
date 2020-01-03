package pen.tests.eco

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.Crypto
import pen.PasswordProvider
import pen.tests.Examples.Participants
import pen.tests.Examples.Passwords.password

class KCryptoTests
{
   val message = "\"Test message\"".toByteArray()
   val alice = Participants.alice()
   val bob = Participants.bob()

   @Test
   fun `Alice to Bob signed` ()
   {
      val signedMessage = Crypto.signText(message, password( 4 ), bob.me.salt())
      val verifiedMessage = Crypto.verifyText(signedMessage, bob.me.publicKey( password( 4 ) ))

      Assertions.assertArrayEquals( message, verifiedMessage )
   }

   @Test
   fun `Alice to Bob encrypted` ()
   {
      /* Alice signs message using her private key, and encrypts it using Bob´s public key. */
      val encryptedMessage = Crypto.pkcEncrypt(message, password( 3 ), alice.me.salt(), bob.me.publicKey( password( 4 ) ))
      /* Bob decrypts message using his private key, and verifies it using Alice´s public key */
      val decryptedMessage = Crypto.pkcDecrypt(encryptedMessage, password( 4 ), bob.me.salt(), alice.me.publicKey( password( 3 ) ))

      Assertions.assertArrayEquals( message, decryptedMessage )
   }

   @Test
   fun `Symetric encryption` ()
   {
      val encryptedMessage = Crypto.encrypt( message, password( 3 ), alice.me.salt() )
      val decryptedMessage = Crypto.decrypt( encryptedMessage, password( 3 ), alice.me.salt() )

      Assertions.assertArrayEquals( message, decryptedMessage )
   }
}
