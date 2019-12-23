package pen.par

interface Participant
{
   val me : KContact
}
interface Council : Participant
{
   val relations : ArrayList<KRelation>
}
interface Member : Participant
{
   val cRelation : KRelation
   val pRelation : KRelation
   val submitHistory : ArrayList<String>
}
