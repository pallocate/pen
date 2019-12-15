package pen.eco

import pen.Crypto
import pen.Utils
import pen.PasswordProvider

class KProposal (val header : KHeader = KHeader(), val items : List<KItem>)
{
   fun encode () : String
   {
      var ret = header.encode()

      for (item in items)
         ret += item.encode()

      return ret
   }

   /** Returns the proposal signed as a array of bytes. */
   fun signed (passwordProvider : PasswordProvider, salt : ByteArray) : ByteArray
   {
      val plainText = Utils.stringToByteArray( encode() )
      return Crypto.signText( plainText, passwordProvider, salt )
   }
}
