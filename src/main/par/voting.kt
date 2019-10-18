package pen.par

import pen.eco.types.PlaceHolder

/** The premise of a proposal. */
interface Premise

/** Details about the votatation. */
interface Motion
{ override fun toString () : String }

/** A simple question, that can be expressed in a sentence or two. */
class KSimpleMotion (val question : String = "") : Motion
{ override fun toString () = question }

interface Votation
{
   val motion : Motion
   var result : Int
   fun add (vote : Vote)
   fun count ()
   fun nrOfVotes () : Int
}
class NoVotation : Votation, PlaceHolder
{
   val origin = "NoVotation"

   override val motion = KSimpleMotion()
   override var result = -1
   override fun add (vote : Vote) = unit( origin )
   override fun count () = unit( origin )
   override fun nrOfVotes () = int( origin )
}

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
