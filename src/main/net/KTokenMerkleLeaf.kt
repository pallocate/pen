package pen.net

import pen.Utils
import pen.Crypto
import pen.eco.Token
import pen.Hashable

/** A leaf node in the CTB. */
class KTokenMerkleLeaf (val token : Token) : KMerkleNode(), Hashable
{
   /** Hash of the token. */
   override fun hash () = Crypto.digest(Utils.stringToByteArray( token.toString() ))
}
