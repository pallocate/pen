package pen.tests.eco

import org.junit.jupiter.api.*
import pen.eco.common.Crypto
import pen.tests.par.*

@DisplayName( "Encryption test" )
class EncryptionTest
{
   val acme = Acme()
   val alice = Alice()
   val bob = Bob()
   val message = "\"Test message\"".toByteArray()

   @Test
   @DisplayName( "Alice to Bob signed" )
   fun signedText ()
   {
      val signedMessage = Crypto.signText( message, Bob.PWD, bob.me.pkcSalt() )
      val verifiedMessage = Crypto.verifyText( signedMessage, bob.me.contact.publicKey )

      Assertions.assertArrayEquals( message, verifiedMessage )
   }

   @Test
   @DisplayName( "Alice to Bob encrypted" )
   fun pkcEncrypted ()
   {
      /* Alice signs message using her private key, and encrypts it using Bob´s public key. */
      val encryptedMessage = Crypto.pkcEncrypt( message, Alice.PWD, alice.me.pkcSalt(), bob.me.contact.publicKey )
      /* Bob decrypts message using his private key, and verifies it using Alice´s public key */
      val decryptedMessage = Crypto.pkcDecrypt( encryptedMessage, Bob.PWD, bob.me.pkcSalt(), alice.me.contact.publicKey )

      Assertions.assertArrayEquals( message, decryptedMessage )
   }

   @Test
   @DisplayName( "Symetric encryption" )
   fun symetricEncryption ()
   {
      val encryptedMessage = Crypto.encrypt( message, Acme.PWD, acme.me.skcSalt() )
      val decryptedMessage = Crypto.decrypt( encryptedMessage, Acme.PWD, acme.me.skcSalt() )

      Assertions.assertArrayEquals( message, decryptedMessage )
   }
}
