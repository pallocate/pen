package pen.par

import kotlinx.serialization.Serializable
import pen.Log
import pen.PasswordProvider
import pen.eco.KProposal
import pen.eco.Target.UNDEFINED
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

@Serializable
class KCouncil () : Participant
{
   override val me = KMe()
   val relations = ArrayList<KRelation>()
}

interface Member : Participant
{
   var consumerRelation : KRelation
   val producerRelations : ArrayList<KRelation>

   fun economicRelations () : Array<KRelation>
}
class NoMember () : Member { override val me = KMe(); override var consumerRelation = KRelation(); override val producerRelations = ArrayList<KRelation>(); override fun economicRelations () = Array<KRelation>( 0, {KRelation()} )}

@Serializable
class KMember () : Member
{
   override val me = KMe()
   override var consumerRelation = KRelation()
   override val producerRelations = ArrayList<KRelation>()

   override fun economicRelations () : Array<KRelation> = arrayOf( consumerRelation ) + producerRelations
}
