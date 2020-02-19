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
class KCouncil (override val me : KMe) : Participant {
   val relations = ArrayList<KRelation>()
}

interface Member : Participant
{
   var consumerRelation : KRelation
   val producerRelations : ArrayList<KRelation>

   fun economicRelations () : Array<KRelation>
}
class NoMember () : Member
{
   override val me = KMe()
   override var consumerRelation = KRelation( KContact() )
   override val producerRelations = ArrayList<KRelation>()
   override fun economicRelations () = Array<KRelation>( 0, {KRelation( KContact() )} )
}

@Serializable
class KMember (override val me : KMe, override var consumerRelation : KRelation = KRelation( KContact() )) : Member
{
   override val producerRelations = ArrayList<KRelation>()

   override fun economicRelations () : Array<KRelation> = arrayOf( consumerRelation ) + producerRelations
}
