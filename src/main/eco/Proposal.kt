package pen.eco

import kotlinx.serialization.Serializable
import pen.Crypto
import pen.Utils
import pen.PasswordProvider

interface Proposal
{
   val header : Header
   val products : List<Product>
}
class NoProposal : Proposal
{
   override val header = KHeader()
   override val products = ArrayList<Product>()
}

@Serializable
class KProposal (override val header : KHeader, override val products : List<KProduct>) : Proposal
{
   /** Returns the proposal signed as a array of bytes. */
   fun signed (passwordProvider : PasswordProvider, salt : ByteArray) : ByteArray
   {
      val plainText = Utils.stringToByteArray( encode() )
      return Crypto.signText( plainText, passwordProvider, salt )
   }

   fun encode () = "TODO: encode to json"

//   fun load (filename : String) = read( filename )
//   fun save (filename : String) : Boolean
}
