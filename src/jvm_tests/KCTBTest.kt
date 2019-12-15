package pen.tests.net

import org.junit.jupiter.api.*
import pen.Crypto
import pen.eco.KIssuedToken
import pen.net.KCTB
import pen.net.KTokenMerkleLeaf

class KCTBTest
{
   @Test
   fun `Add token` ()
   {
      val issuedToken = KIssuedToken(1L, 10, 101L, ByteArray( Crypto.publicSigningKeySize() ))
      KCTB.add( issuedToken )
      KCTB.add( issuedToken )
      KCTB.add( issuedToken )

      Assertions.assertTrue( KCTB.storage[5] is KTokenMerkleLeaf )
   }
}
