package pen.eco.types.proposal

import pen.eco.Crypto
import pen.eco.Utils
import pen.eco.types.PasswordProvider

class KProposal (val header : KHeader = KHeader(), val items : List<KItem>)
{
   override fun toString () : String
   {
      var ret = header.toString()

      for (item in items)
         ret += item

      return ret
   }

   /** Returns the proposal signed as a array of bytes. */
   fun signed (passwordProvider : PasswordProvider, salt : ByteArray) : ByteArray
   {
      val plainText = Utils.stringToByteArray( toString() )
      return Crypto.signText( plainText, passwordProvider, salt )
   }
}
