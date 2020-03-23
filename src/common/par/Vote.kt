package pen.par

import pen.Crypto
import pen.Utils

abstract class Vote ()
{
   abstract val votingId : Long
   abstract val voteId : Long
   abstract val selection : Alternatives
   abstract val signature : ByteArray

   override fun toString () = "votation: { id: ${votingId}, vote: { id: ${voteId}, selection: ${selection} } }"
}
