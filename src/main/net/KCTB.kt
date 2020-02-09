package pen.net

import pen.Config
import pen.Loggable
import pen.LogLevel.ERROR
import pen.eco.Token
import pen.eco.NoToken

/** Credit Token Blockchain */
object KCTB : KMerkleTree(), Loggable
{
   /** Returns the last occurrence of a token with the specified id. */
   fun last (id : Long) : Token
   {
      log("Finding last occurrence of token \"$id\"", Config.trigger( "PLUGINS_CTB" ))
      var ret : Token = NoToken()

      if (lastIdx > 0)
         for (leafIdx in lastIdx downTo 1 step 2)
         {
            val leaf = storage[leafIdx]
            if (leaf is KTokenMerkleLeaf)
            {
               if (leaf.token.id == id)
               {
                  ret = leaf.token
                  log("Token found \"$id\"", Config.trigger( "PLUGINS_CTB" ))
                  break
               }
            }
            else
               log("Wrong type! (KTokenMerkleLeaf expected)", Config.trigger( "PLUGINS_CTB" ), ERROR )
         }

      return ret
   }

   /** Adds a token to the CTB. */
   fun add (token : Token)
   {
      if (token !is NoToken)
         super.add(KTokenMerkleLeaf( token ))
   }

   override fun tag () = "KCTB"
}
