package pen.net.bc.mt

import pen.eco.Config
import pen.eco.Loggable
import pen.eco.LogLevel.ERROR
import pen.eco.types.Token
import pen.eco.types.NoToken

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

   override fun originName () = "KCTB"
}
