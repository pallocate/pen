package pen.par

interface Vote
{
   val votationId : Long
//   val councilId : Long
   val voteId : Long
   val choise : Choise
   val signature : ByteArray
}
