package pen.net.bc.mt

import pen.eco.Crypto
import pen.eco.types.Vote
import pen.eco.types.Hashable
import pen.net.bc.mt.KMerkleNode

/** Leaf node in a KMerkleTree containg a vote. */
class KVoteMerkleLeaf (val vote : Vote) : KMerkleNode(), Hashable
{
   /** Hash of the vote. */
   override fun hash () = Crypto.digest( vote.toString().toByteArray() + vote.signature )
}
