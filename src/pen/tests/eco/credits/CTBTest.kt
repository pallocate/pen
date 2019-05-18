package pen.tests.eco.credits

import org.junit.jupiter.api.*
import pen.tests.examples.Acme
import pen.eco.credits.*

@DisplayName( "CTB tests" )
class CTBTest
{
   @Test
   @DisplayName( "Basic add test" )
   fun basicAddTest ()
   {
      val acme = Acme()
      CTB.add(KToken( 1L, 20, 101L, Acme.PWD, acme.me.pkcSalt() ))
      CTB.add(KToken( 2L, 30, 101L, Acme.PWD, acme.me.pkcSalt() ))
      CTB.add(KToken( 3L, 40, 101L, Acme.PWD, acme.me.pkcSalt() ))

      Assertions.assertTrue( CTB.storage[5] is TokenMerkleLeaf )
   }
}
