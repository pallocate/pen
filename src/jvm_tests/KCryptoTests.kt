package pen.tests.eco

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.Crypto
import pen.PasswordProvider
import pen.tests.Examples.Participants
import pen.tests.Examples.Participants.alicePwd
import pen.tests.Examples.Participants.bobsPwd

class KCryptoTests
{
   val message = "\"Test message\"".toByteArray()
   val alice = Participants.alice()
   val bob = Participants.bob()

   @Test
   fun `Alice to Bob signed` ()
   {
      val signedMessage = Crypto.signText( message, bobsPwd, bob.me.salt() )
      val verifiedMessage = Crypto.verifyText(signedMessage, bob.me.publicKey( bobsPwd ))

      Assertions.assertArrayEquals( message, verifiedMessage )
   }

   @Test
   fun `Alice to Bob encrypted` ()
   {
      /* Alice signs message using her private key, and encrypts it using Bob´s public key. */
      val encryptedMessage = Crypto.pkcEncrypt(message, alicePwd, alice.me.salt(), bob.me.publicKey( bobsPwd ))
      /* Bob decrypts message using his private key, and verifies it using Alice´s public key */
      val decryptedMessage = Crypto.pkcDecrypt(encryptedMessage, bobsPwd, bob.me.salt(), alice.me.publicKey( alicePwd ))

      Assertions.assertArrayEquals( message, decryptedMessage )
   }

   @Test
   fun `Symetric encryption` ()
   {
      val encryptedMessage = Crypto.encrypt( message, alicePwd, alice.me.salt() )
      val decryptedMessage = Crypto.decrypt( encryptedMessage, alicePwd, alice.me.salt() )

      Assertions.assertArrayEquals( message, decryptedMessage )
   }
}
