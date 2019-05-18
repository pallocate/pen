package pen.tests.eco.voting

import org.junit.jupiter.api.*
import pen.par.ConsumptionCouncil
import pen.par.ProductionCouncil
import pen.tests.examples.Acme
import pen.tests.examples.StMarys
import pen.tests.examples.FPC
import pen.net.Network
import pen.eco.voting.*

@DisplayName( "Basic test" )
class VotationTest
{
   val acme = Acme()
   val stmarys = StMarys()
   val fpc = FPC()
   val productionCouncil = ProductionCouncil()
   val consumptionCouncil1 = ConsumptionCouncil()
   val consumptionCouncil2 = ConsumptionCouncil()

   @Test
   @DisplayName( "Basic votation test" )
   fun votationTest ()
   {
      val motion = SimpleMotion( "Approve new council?" )
      val yes = Choise( Choise.APPROVE )
      val no = Choise( Choise.DISMISS )

      val votationID = productionCouncil.startVotation( motion, java.time.LocalDateTime.now().minusWeeks( 1L ) )
      productionCouncil.castVote( votationID, yes, Acme.PWD, acme.me.pkcSalt() )
      consumptionCouncil1.castVote( votationID, yes, StMarys.PWD, stmarys.me.pkcSalt() )
      consumptionCouncil2.castVote( votationID, no, FPC.PWD, fpc.me.pkcSalt() )

      val stuff = Network.findStuff( votationID )
      if (stuff is Votation)
      {
         stuff.count()
         Assertions.assertEquals( Choise.APPROVE, stuff.result )
      }
      else
         Assertions.assertTrue( false )
   }
}
