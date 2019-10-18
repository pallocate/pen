package pen.par

import java.io.Serializable
import java.time.LocalDateTime
import pen.eco.Log
import pen.eco.types.PasswordProvider
import pen.net.Message
import pen.net.Network

/** A consumption council in the economy. */
class KConsumptionCouncil : Council()
{
   override var name = "Consumption council"
   override var icon = Constants.ICONS_DIR + "applications-development.png"
}

/** A production council in the economy. */
class KProductionCouncil : Council()
{
   override var name = "Production council"
   override var icon = Constants.ICONS_DIR + "applications-development.png"
   var issuedCredits = 0
}

/** A council in the economy. */
abstract class Council : Participant(), Serializable
{
   val members = HashMap<String, Contact>()
   val votations = ArrayList<Votation>()

   /** Adds a member, mapping name to Contact */
   fun addMember (name : String, contact : Contact) = members.put( name, contact )
   fun member (name : String) : Contact = members.getOrElse(name, { UnContact() })

   /** Sends a Message to several members.
     * @param council The council role used for sending message.
     * @param avatar The sending Avatar
     * @param group A ':' delimited list of groups or members */
   fun groupSend (me : Me, message : String, group : String, passwordProvider : PasswordProvider)
   {
      Log.debug( "Sending message to group" )
      var recipients = ArrayList<Contact>()
      val groups = group.split( ":" )

      /* Assume group */
      for ((name,contact) in members)
         for (g in groups)
         {
            val gt = g.trim()
            if (gt == contact.group)
               recipients.add( contact )
         }

      /* Otherwise members */
      if (recipients.isEmpty())
      {
         for (m in groups)
         {
            val mt = m.trim()
            if (members.containsKey( mt ))
            {
               recipients.add( members.get( mt )!! )
               break
            }
         }
      }

      if (recipients.isEmpty())
         Log.warn( "Group Send failed! (no recipients)" )
      else
         for (recipient in recipients)
            Network.send(Message( message.toByteArray(), recipient.contactID, Network.generateID(), passwordProvider, me.pkcSalt(), recipient.publicKey ))
   }
}
