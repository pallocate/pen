package pen.tests.eco

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.Crypto
import pen.tests.Examples.Participants.Alice
import pen.tests.Examples.Participants.Bob

class KCryptoTests
{
   val message = "\"Test message\"".toByteArray()

   @Test
   fun `Alice to Bob signed` ()
   {
      val signedMessage = Crypto.signText( message, Bob, Bob.me.salt() )
      val verifiedMessage = Crypto.verifyText(signedMessage, Bob.me.publicKey( Bob ))

      Assertions.assertArrayEquals( message, verifiedMessage )
   }

   @Test
   fun `Alice to Bob encrypted` ()
   {
      /* Alice signs message using her private key, and encrypts it using Bob´s public key. */
      val encryptedMessage = Crypto.pkcEncrypt(message, Alice, Alice.me.salt(), Bob.me.publicKey( Bob ))
      /* Bob decrypts message using his private key, and verifies it using Alice´s public key */
      val decryptedMessage = Crypto.pkcDecrypt(encryptedMessage, Bob, Bob.me.salt(), Alice.me.publicKey( Alice ))

      Assertions.assertArrayEquals( message, decryptedMessage )
   }

   @Test
   fun `Symetric encryption` ()
   {
      val encryptedMessage = Crypto.encrypt( message, Alice, Alice.me.salt() )
      val decryptedMessage = Crypto.decrypt( encryptedMessage, Alice, Alice.me.salt() )

      Assertions.assertArrayEquals( message, decryptedMessage )
   }
}
