package pen.eco.credits

import pen.eco.Log
import pen.eco.common.Crypto
import pen.eco.common.Hashable
import pen.eco.common.MerkleTree
import pen.eco.common.KMerkleNode

/** A leaf node in the CTB. */
class TokenMerkleLeaf (val token : KToken) : KMerkleNode(), Hashable
{
   /** Hash of the token. */
   override fun hash () = Crypto.digest( token.toString().toByteArray() )
}

/** Credit Token Blockchain */
object CTB : MerkleTree()
{
   /** Returns the last occurrence of a token with the specified id. */
   fun last (id : Long) : Token
   {
      Log.debug( "Finding last occurrence of token \"$id\"" )
      var ret : Token = NoToken()

      if (lastIdx > 0)
         for (leafIdx in lastIdx downTo 1 step 2)
         {
            val leaf = storage[leafIdx]
            if (leaf is TokenMerkleLeaf)
            {
               if (leaf.token.id == id)
                  ret = leaf.token
            }
            else
               Log.err( "Wrong type! (TokenMerkleLeaf expected)" )
         }

      return ret
   }

   /** Adds a token to the CTB. */
   fun add (token : KToken) = super.add(TokenMerkleLeaf( token ))
}
