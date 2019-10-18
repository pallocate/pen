package pen.tests.net

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.eco.Crypto
import pen.net.Network
import pen.net.Message
import pen.tests.Examples.Alice
import pen.tests.Examples.Bob

class KEncryptedMessageTest
{
   val testMessage = "\"Test message\"".toByteArray()

   @Test
   fun `Alice to Bob` ()
   {
      val encryptedMessage = Message( testMessage, Network.generateID(), Network.generateID(), Alice.pwd, Alice.me.pkcSalt(), Bob.me.publicKey( Bob.pwd ) )
      val decryptedMessage = encryptedMessage.decrypt( Bob.pwd, Bob.me.pkcSalt(), Alice.me.publicKey( Alice.pwd ) )
      Assertions.assertArrayEquals( testMessage, decryptedMessage )
   }
}
