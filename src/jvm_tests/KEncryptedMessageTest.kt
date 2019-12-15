package pen.tests.net

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.Crypto
import pen.net.Network
import pen.net.Message
import pen.tests.Examples.Participants.Alice
import pen.tests.Examples.Participants.Bob

class KEncryptedMessageTest
{
   val testMessage = "\"Test message\"".toByteArray()

   @Test
   fun `Alice to Bob` ()
   {
      val encryptedMessage = Message( testMessage, Network.generateId(), Network.generateId(), Alice, Alice.me.pkcSalt(), Bob.me.publicKey( Bob ) )
      val decryptedMessage = encryptedMessage.decrypt( Bob, Bob.me.pkcSalt(), Alice.me.publicKey( Alice ) )
      Assertions.assertArrayEquals( testMessage, decryptedMessage )
   }
}
