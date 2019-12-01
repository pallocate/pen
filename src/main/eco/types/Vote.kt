package pen.eco.types

interface Vote
{
   val votationId : Long
//   val councilId : Long
   val voteId : Long
   val choise : Choise
   val signature : ByteArray
}
