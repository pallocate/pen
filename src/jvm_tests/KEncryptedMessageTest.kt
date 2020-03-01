package pen.tests.net

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.Crypto
import pen.par.KMe
import pen.net.Network
import pen.net.KMessage
import pen.tests.ExamplePasswords.password

class KEncryptedMessageTest
{
   val testMessage = "\"Test message\"".toByteArray()
   val alice = KMe.factory( 3L, "Alice", "", password( 3 ))
   val bob = KMe.factory( 4L, "Bob", "", password( 4 ))

   @Test
   fun `Alice to Bob` ()
   {
      val encryptedMessage = KMessage( testMessage, Network.generateId(), Network.generateId(), password( 3 ), alice.salt, bob.publicKey )
      val decryptedMessage = encryptedMessage.decrypt( password( 4 ), bob.salt, alice.publicKey )
      Assertions.assertArrayEquals( testMessage, decryptedMessage )
   }
}
