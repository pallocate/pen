package pen.net

import pen.Crypto
import pen.Utils
import pen.par.Vote
import pen.Hashable
import pen.net.KMerkleNode

/** Leaf node in a KMerkleTree containg a vote. */
class KVoteMerkleLeaf (val vote : Vote) : KMerkleNode(), Hashable
{
   /** Hash of the vote. */
   override fun hash () = Crypto.digest(Utils.stringToByteArray( vote.toString() ) + vote.signature)
}
