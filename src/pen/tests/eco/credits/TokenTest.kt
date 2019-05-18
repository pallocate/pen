package pen.tests.eco.credits

import java.time.LocalDateTime
import org.junit.jupiter.api.*
import pen.eco.common.Utils
import pen.eco.common.Crypto
import pen.eco.planning.Proposal
import pen.eco.planning.VerseProduct
import pen.eco.voting.*
import pen.eco.credits.*
import pen.par.ProductionCouncil
import pen.par.Consumer
import pen.par.Contact
import pen.tests.examples.Bob
import pen.tests.examples.Acme
import pen.tests.examples.Blocks

@DisplayName( "Token tests" )
class TokenTest
{
   val acme = Acme()
   val bob = Bob()
   val productionCouncil = ProductionCouncil()
   val consumer = Consumer()
   val votation : Votation

   init
   {
      /* Sets up a votation needed for token issuence. */
      val proposal = Blocks.testProposal()
      val consumptionMotion = ConsumptionMotion(proposal, ProgramPremise( 0L ))
      votation = Votation(23L, consumptionMotion, LocalDateTime.now().minusWeeks( 1L ), 1)
      val vote = Vote(23L, 30L, Choise( Choise.APPROVE ), Acme.PWD, acme.me.pkcSalt())
      votation.add( vote )
   }

   @Test
   @DisplayName( "Basic token lifecycle" )
   fun tokenLifecycle ()
   {
      /* Acme issues a 1000 credit token, */
      val tokenID = productionCouncil.issueToken( 1000, votation, Acme.PWD, acme.me.pkcSalt() )

      /* and defines Bob as the user. */
      var token = CTB.last( tokenID )
      if (token is KToken)
         productionCouncil.defineTokenUser( token, bob.me.contact.publicKey, Acme.PWD, acme.me.pkcSalt() )

      /* Bob uses the token in a shop. */
      token = CTB.last( tokenID )                                               // The shop retrieves the lastest token instance from the CTB,
      if (token is KToken && token.state == KToken.States.USABLE)               // and checks that it is usable.
      {
         consumer.pay( 3L, token, Bob.PWD, bob.me.pkcSalt() )                   // Bob pays and hands over the token.
         if (token.account())                                                   // The shop verifies it, accounts it,
            CTB.add( token )                                                    // and adds it to the CTB.
      }

      /* Assertion that the token has been accounted. */
      token = CTB.last( tokenID )
      Assertions.assertEquals( KToken.States.ACCOUNTED, (token as KToken).state )
   }
}
