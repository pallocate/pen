package pen.eco.voting

import java.time.LocalDateTime
import pen.eco.common.Log
import pen.eco.common.MerkleTree
import pen.eco.common.Identifiable

/** Used to agree in a change to the current state of the economy.
  * @param id A id unique to the votation.
  * @param motion The change detailed.
  * @param expiration When the voting ends.
  * @param nrOfCouncils Number of participating councils.
  * @param nrOfAlternatives How many alternatives there is to choose from. */
class Votation (override val id : Long, val motion : Motion, val expiration : LocalDateTime, val nrOfCouncils : Int, val nrOfAlternatives : Int = 2) : MerkleTree(), Identifiable
{
   val ballot = Array<Int>(nrOfAlternatives, { 0 })
   var result = -1
      private set

   fun add (vote : Vote)
   {
      var priorVote = false
      for (i in 1 until storage.size step 2)
      {
         val leaf = storage[i]
         if (leaf is VoteMerkleLeaf && leaf.vote.voteID == vote.voteID)
         {
            priorVote = true
            break
         }
      }
      if (vote.votationID == id && !priorVote)
         super.add(VoteMerkleLeaf( vote ))
   }

   /** Counts the votes and tries to get a majority result. */
   fun count ()
   {
      Log.debug( "Counting votes" )
      for (leafIdx in 1..lastIdx step 2)
      {
         val leaf = storage[leafIdx]
         if (leaf is VoteMerkleLeaf)
         {
            val selection = leaf.vote.choise.selected
            if (selection >= 0 && selection < nrOfAlternatives)
               ballot[selection]++
         }
         else
            Log.err( "Wrong type! (VoteMerkleLeaf expected)" )
      }

      /* Uses system clock to determine if the voting time has expired. */
      if (LocalDateTime.now() > expiration)
      {
         var candidate = 0
         var draw = false

         for (i in 0 until ballot.size)
            if (ballot[i] == ballot[candidate])
               draw = true
            else
               if (ballot[i] > ballot[candidate])
               {
                  candidate = i
                  draw = false
               }

         if (draw)
            Log.info( "Vote counting result: a draw" )
         else
         {
            result = candidate
            Log.info( "Vote counting result: \"$result\"" )
         }
      }
   }

   fun nrOfVotes () = if (lastIdx > 0) lastIdx/2 + 1 else 0
}
