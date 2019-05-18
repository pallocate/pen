package pen.eco.voting

import pen.eco.common.Log
import pen.eco.common.Crypto
import pen.eco.common.PasswordProvider
import pen.eco.common.KMerkleNode
import pen.eco.common.Hashable

/** Leaf node in a MerkleTree containg a vote. */
class VoteMerkleLeaf (val vote : Vote) : KMerkleNode(), Hashable
{
   /** Hash of the vote. */
   override fun hash () = Crypto.digest( vote.toString().toByteArray() + vote.signature )
}

/** @param nrOfOptions Nr of options, DISMISS incluced. */
class Choise (var selected : Int = DISMISS, val nrOfOptions : Int = 2)
{
   companion object
   {
      const val DISMISS = 0
      const val APPROVE = 1
   }
}

/** A vote in a votation. */
class Vote (val votationID : Long, val voteID : Long, val choise : Choise, passwordProvider : PasswordProvider, pkc_salt : ByteArray)
{
   val signature : ByteArray

   init
   {
      Log.debug( "Signing vote \"$voteID\"" )
      signature = Crypto.signatureOf( toString().toByteArray(), passwordProvider, pkc_salt )
   }

   override fun toString () : String
   { return "" + voteID + votationID + choise.selected }
}
