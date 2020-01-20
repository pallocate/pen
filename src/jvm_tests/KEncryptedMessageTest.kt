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
   val alice = KMe().apply { name = "Alice" }
   val bob = KMe().apply { name = "Bob" }

   @Test
   fun `Alice to Bob` ()
   {
      val encryptedMessage = KMessage( testMessage, Network.generateId(), Network.generateId(), password( 3 ), alice.salt(), bob.publicKey( password( 4 ) ) )
      val decryptedMessage = encryptedMessage.decrypt( password( 4 ), bob.salt(), alice.publicKey( password( 3 ) ) )
      Assertions.assertArrayEquals( testMessage, decryptedMessage )
   }
}
