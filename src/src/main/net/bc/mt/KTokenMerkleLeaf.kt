package pen.net.bc.mt

import pen.eco.Crypto
import pen.eco.types.Token
import pen.eco.types.Hashable

/** A leaf node in the CTB. */
class KTokenMerkleLeaf (val token : Token) : KMerkleNode(), Hashable
{
   /** Hash of the token. */
   override fun hash () = Crypto.digest( token.toString().toByteArray() )
}
