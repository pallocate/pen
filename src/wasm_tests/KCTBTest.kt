package pen.tests.net

import pen.Crypto
import pen.eco.KIssuedToken
import pen.net.KCTB
import pen.net.KTokenMerkleLeaf
import pen.tests.Assertions

class KCTBTests
{
   fun addToken ()
   {
      val issuedToken = KIssuedToken(1L, 10, 101L, ByteArray( Crypto.publicSigningKeySize() ))
      KCTB.add( issuedToken )
      KCTB.add( issuedToken )
      KCTB.add( issuedToken )

      Assertions.assertTrue( KCTB.storage[5] is KTokenMerkleLeaf )
   }
}
