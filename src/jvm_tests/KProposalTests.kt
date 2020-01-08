package pen.tests.eco

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.writeObject
import pen.readObject
import pen.eco.KProposal
import pen.tests.Examples

class KProposalTests
{
   @Test
   fun `Save, read` ()
   {
      val filename = "dist${pen.Constants.SLASH}test.out"
      val proposal = Examples.Proposal.proposal()

      writeObject( proposal, {KProposal.serializer()}, filename )
      val obj = readObject<KProposal>( {KProposal.serializer()}, filename )

      Assertions.assertEquals( 10000, obj!!.products[0].qty )
   }
}
