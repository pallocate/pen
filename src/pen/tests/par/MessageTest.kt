package pen.tests.par

import org.junit.jupiter.api.*
import pen.eco.common.Crypto
import pen.net.Network
import pen.net.Message

@DisplayName( "Message test" )
class MessageTest
{
   val testMessage = "\"Test message\"".toByteArray()

   @Test
   @DisplayName( "Alice to Bob" )
   fun usingMessage ()
   {
      val alice = Alice()
      val bob = Bob()

      val encryptedMessage = Message( testMessage, Network.generateID(), Network.generateID(), Alice.PWD, alice.me.pkcSalt(), bob.me.publicKey( Bob.PWD ) )
      val decryptedMessage = encryptedMessage.decrypt( Bob.PWD, bob.me.pkcSalt(), alice.me.publicKey( Alice.PWD ) )
      Assertions.assertArrayEquals( testMessage, decryptedMessage )
   }
}
