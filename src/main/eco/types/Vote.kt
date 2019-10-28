package pen.eco.types


/** @param nrOfOptions Nr of options, DISMISS incluced. */
class Choise (val selected : Int, val nrOfOptions : Int = 2)
{
   companion object
   {
      const val DISMISS = 0
      const val APPROVE = 1
   }
}

interface Vote
{
   val votationID : Long
   val voteID : Long
   val choise : Choise
   val signature : ByteArray
}
