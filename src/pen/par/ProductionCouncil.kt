package pen.par

import pen.eco.common.Log
import pen.eco.common.PasswordProvider
import pen.net.Network
import pen.eco.voting.Votation
import pen.eco.voting.Choise
import pen.eco.voting.ConsumptionMotion
import pen.eco.credits.CTB
import pen.eco.credits.KToken

/** A production council in the economy. */
class ProductionCouncil : Council()
{
   override var name = "Production council"
   override var icon = Constants.ICONS_DIR + "applications-development.png"
   var issuedCredits = 0
      private set

   /** Issues a token of a certain value.
     * @param value how many credits the token should represent.
     * @param votation The votation that constitutes the ground for the token issuence.  */
   fun issueToken (value : Int, votation : Votation, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : Long
   {
      Log.debug( "Issuing a $value credit token" )
      var ret = -1L

      if (votation.result == -1)
         votation.count()

      if (votation.result == Choise.APPROVE)
      {
         val motion = votation.motion
         if (motion is ConsumptionMotion && issuedCredits + value <= motion.value())
         {
            ret = Network.generateID()
            val newToken = KToken( ret, value, me.contact.contactID, passwordProvider, pkc_salt )
            issuedCredits += value
            CTB.add( newToken )
         }
      }
      else
         Log.warn( "Token issuence failed! (Votation was not approved)" )

      return ret
   }

   /** Defines the user(user key) of a token. */
   fun defineTokenUser (token : KToken, userKey : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray)
   {
      Log.debug( "Defining token user" )
      token.defineUser(userKey, passwordProvider, pkc_salt)
      CTB.add( token )
   }
}
