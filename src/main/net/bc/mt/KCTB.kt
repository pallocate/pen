package pen.net.bc.mt

import pen.eco.Log
import pen.eco.types.Token
import pen.eco.types.NoToken

/** Credit Token Blockchain */
object KCTB : KMerkleTree()
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
            if (leaf is KTokenMerkleLeaf)
            {
               if (leaf.token.id == id)
                  ret = leaf.token
            }
            else
               Log.err( "Wrong type! (KTokenMerkleLeaf expected)" )
         }

      return ret
   }

   /** Adds a token to the CTB. */
   fun add (token : Token)
   {
      if (token !is NoToken)
         super.add(KTokenMerkleLeaf( token ))
   }
}
