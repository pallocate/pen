package pen.eco.types.proposal

import pen.eco.Crypto
import pen.eco.types.PasswordProvider

abstract class Proposal (open val header : KHeader = KHeader()) {}

class KProposal (header : KHeader, val items : List<KItem>) : Proposal( header )
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
      val plainText = toString().toByteArray()
      return Crypto.signText( plainText, passwordProvider, salt )
   }
}
