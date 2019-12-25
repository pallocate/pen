package pen.tests.net

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.Crypto
import pen.net.Network
import pen.net.KMessage
import pen.tests.Examples.Participants
import pen.tests.Examples.Participants.alicePwd
import pen.tests.Examples.Participants.bobsPwd

class KEncryptedMessageTest
{
   val testMessage = "\"Test message\"".toByteArray()
   val alice = Participants.alice()
   val bob = Participants.bob()

   @Test
   fun `Alice to Bob` ()
   {
      val encryptedMessage = KMessage( testMessage, Network.generateId(), Network.generateId(), alicePwd, alice.me.salt(), bob.me.publicKey( bobsPwd ) )
      val decryptedMessage = encryptedMessage.decrypt( bobsPwd, bob.me.salt(), alice.me.publicKey( alicePwd ) )
      Assertions.assertArrayEquals( testMessage, decryptedMessage )
   }
}
