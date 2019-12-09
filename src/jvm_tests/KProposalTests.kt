package pen.tests.eco

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.eco.types.KMutableProposal
import pen.tests.Examples

class KProposalTests
{
   @Test
   fun `Save, read` ()
   {
      val filename = "dist${pen.eco.Constants.SLASH}test.out"
      val proposal = Examples.Proposal.mutableProposal()
      Assertions.assertTrue( proposal.save( filename ) )

      val mutableProposal = KMutableProposal()
      mutableProposal.load( filename )
      Assertions.assertEquals( 10000, mutableProposal.products[0].qty )
   }
}
