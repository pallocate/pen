package pen.eco.types

import pen.eco.Crypto
import pen.eco.Utils
import pen.eco.types.PasswordProvider

class KProposal (val header : KHeader = KHeader(), val items : List<KItem>)
{
   fun encode () : String
   {
      var ret = header.encode()

      for (item in items)
         ret += item

      return ret
   }

   /** Returns the proposal signed as a array of bytes. */
   fun signed (passwordProvider : PasswordProvider, salt : ByteArray) : ByteArray
   {
      val plainText = Utils.stringToByteArray( encode() )
      return Crypto.signText( plainText, passwordProvider, salt )
   }
}
