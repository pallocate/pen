package pen.tests.eco

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.serializeToFile
import pen.deserializeFromFile
import pen.eco.KProposal
import pen.tests.ExampleProposals

class KProposalTests
{
   @Test
   fun `Save, read` ()
   {
      val filename = "dist${pen.Constants.SLASH}test.out"
      val proposal = ExampleProposals.proposal()

      serializeToFile( proposal, filename, KProposal.serializer() )
      val obj = deserializeFromFile<KProposal>( filename, KProposal.serializer() )

      Assertions.assertEquals( 10000, obj!!.products[0].qty )
   }
}
