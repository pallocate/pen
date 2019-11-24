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
      val proposal = Examples.Proposal.mutableProposal()

      println( proposal.toString() )

      Assertions.assertTrue( proposal.save( "dist/output" ) )
   }
}
