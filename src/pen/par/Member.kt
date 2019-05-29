package pen.par

import java.io.Serializable
import pen.eco.Log
import pen.eco.common.PasswordProvider
import pen.net.Message
import pen.net.Network
import pen.eco.plan.Block
import pen.eco.credits.CTB
import pen.eco.credits.KToken

/** A member of a council. */
abstract class Member () : Participant(), Serializable
{
   /** The name of the Council. */
   var councilName = ""
   /** Contact information to the council. */
   var councilContact : Contact = UnContact()
   /** History of submitted proposals. */
   val submitHistory = ArrayList<String>()

   /** Submits a proposal(Block) to the council. */
   fun submit (proposal : Block, passwordProvider : PasswordProvider) : Message
   {
      Log.debug( "Submitting proposal" )
      val signedProposal = proposal.signed( passwordProvider, me.pkcSalt() )
      val message = Message( signedProposal, me.contact.contactID, councilContact.contactID, passwordProvider, me.pkcSalt(), councilContact.publicKey )

      Network.send( message )
      submitHistory.add( proposal.progression() )

      return message
   }

   /** Checks if a progression string exists in the recent submit history, meaning proposal has been submitted.
     * @return An empty string if it has been submitted, or the roleName if it has not. */
   fun submitted (progress : String) : Boolean
   {
      var ret = false
      var startSearchAt = 0

      if (submitHistory.size > 0)
      {
         if (submitHistory.size > 10)
            startSearchAt = submitHistory.size - 10

         for (a in startSearchAt until submitHistory.size)
            if (submitHistory[a] == (name + ":" + progress))
               ret = true
      }

      return ret
   }

   /** Sets the council information.
     * @param councilContact The contact information to the Council. */
   fun setCouncil (councilName : String, councilContact : Contact)
   {
      this.councilName = councilName
      this.councilContact = councilContact
   }
   /** Returns a mapping of councilName to councilContact. */
   fun council () : HashMap<String, Contact>
   {return hashMapOf( councilName to councilContact )}
}

class UnMember : Member()
{ override var name = ""; override var icon = "" }

/** A consumer in the economy. */
open class Consumer : Member(), Serializable
{
   override var name = "Consumption"
   override var icon = Constants.ICONS_DIR + "system-software-install.png"

   /** Pays for a product using token. */
   fun pay (productID : Long, token : KToken, passwordProvider : PasswordProvider, pkc_salt : ByteArray) =
    token.use( productID, passwordProvider, pkc_salt )
}

/** A producer in the economy. */
open class Producer : Member(), Serializable
{
   override var name = "Production"
   override var icon = Constants.ICONS_DIR + "applications-development.png"
}
