package pen.par

import kotlinx.serialization.Serializable
import pen.eco.KProposal

interface Channel

@Serializable
class KTender (var relation : KRelation, val proposal : KProposal, val channel : Channel)
{
   fun tender () {}
}
