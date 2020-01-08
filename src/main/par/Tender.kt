package pen.par

import kotlinx.serialization.Serializable
import pen.PasswordProvider
import pen.eco.KProposal
import pen.eco.NoProposal
import pen.eco.Proposal
import pen.net.KMessage
import pen.net.Network

interface Tender
{
   val proposal : Proposal
   val relation : Relation
}
class NoTender : Tender
{
   override val proposal = NoProposal()
   override val relation = NoRelation()
}

@Serializable
class KTender (override val proposal : KProposal, override val relation : KRelation) : Tender
{
   fun submit (me : KMe, passwordProvider : PasswordProvider) : KMessage
   {
      val signedProposal = proposal.signed( passwordProvider, me.salt() )
      val message = KMessage( signedProposal, me.contactId, relation.other.contactId, passwordProvider, me.salt(), relation.other.publicKey )

      Network.send( message )
      return message
   }
}
