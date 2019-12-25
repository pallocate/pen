package pen.par

import pen.Log
import pen.Filable
import pen.PasswordProvider
import pen.eco.KProposal
import pen.eco.EconomicFunction
import pen.net.Network
import pen.net.Message
import pen.net.NoMessage
import pen.net.KMessage

interface Participant
{
   val me : KMe
}
class NoParticipant : Participant
{ override val me = KMe() }

class KCouncil () : Participant, Filable
{
   override val me = KMe()
   val relations = ArrayList<KRelation>()
}

interface Member : Participant
{
   var consumerRelation : KRelation
   var producerRelation : KRelation
}
class NoMember () : Member
{
   override val me = KMe()
   override var consumerRelation = KRelation()
   override var producerRelation = KRelation()
}

class KMember () : Member, Filable
{
   override val me = KMe()
   override var consumerRelation = KRelation()
   override var producerRelation = KRelation()
}
