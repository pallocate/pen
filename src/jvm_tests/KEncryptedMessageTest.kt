package pen.tests.net

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.Crypto
import pen.net.Network
import pen.net.KMessage
import pen.tests.Examples.Participants
import pen.tests.Examples.Passwords.password

class KEncryptedMessageTest
{
   val testMessage = "\"Test message\"".toByteArray()
   val alice = Participants.alice()
   val bob = Participants.bob()

   @Test
   fun `Alice to Bob` ()
   {
      val encryptedMessage = KMessage( testMessage, Network.generateId(), Network.generateId(), password( 3 ), alice.me.salt(), bob.me.publicKey( password( 4 ) ) )
      val decryptedMessage = encryptedMessage.decrypt( password( 4 ), bob.me.salt(), alice.me.publicKey( password( 3 ) ) )
      Assertions.assertArrayEquals( testMessage, decryptedMessage )
   }
}
