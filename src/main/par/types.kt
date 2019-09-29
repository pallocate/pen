package pen.par

import pen.eco.types.PlaceHolder
import pen.eco.types.PasswordProvider

interface TokenManager
{
   fun issueToken (value : Int, votation : Votation, councilId : Long, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : Long
}
class NoTokenManager : TokenManager
{
   override fun issueToken (value : Int, votation : Votation, councilId : Long, passwordProvider : PasswordProvider, pkc_salt : ByteArray) = 0L
}

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
   fun add (vote : KVote)
   fun count ()
   fun nrOfVotes () : Int
}
class NoVotation : Votation, PlaceHolder
{
   val origin = "NoVotation"

   override val motion = KSimpleMotion()
   override var result = -1
   override fun add (vote : KVote) = unit( origin )
   override fun count () = unit( origin )
   override fun nrOfVotes () = int( origin )
}
