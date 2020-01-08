package pen.par

import kotlinx.serialization.Serializable
import pen.eco.KMutableProposal

@Serializable
class KMutableTender (override var proposal : KMutableProposal = KMutableProposal(), override var relation : KRelation = KRelation()) : Tender
{
   constructor (tender : KTender) : this(KMutableProposal( tender.proposal )) {}

   fun toKTender () = KTender( proposal.toKProposal(), relation )
}
