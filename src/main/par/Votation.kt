package pen.par

import pen.eco.types.PlaceHolder

interface Votation
{
   fun add (vote : KVote)
   fun count ()
   fun nrOfVotes () : Int
}
class NoVotation : Votation, PlaceHolder
{
   val name = "NoVotation"
   override fun add (vote : KVote) = unit( name )
   override fun count () = unit( name )
   override fun nrOfVotes () = int( name )
}
