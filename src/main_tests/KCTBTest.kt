package pen.tests

import org.junit.jupiter.api.*
import pen.eco.Crypto
import pen.eco.types.KIssuedToken
import pen.net.bc.mt.KCTB
import pen.net.bc.mt.KTokenMerkleLeaf

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
