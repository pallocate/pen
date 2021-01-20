package pen.eco

import kotlinx.serialization.Serializable
import pen.PasswordProvider

@Serializable
data class KProposal (val header : KHeader = KHeader(), val products : ArrayList<KProduct> = ArrayList<KProduct>())
{
   companion object
   { fun void () = KProposal() }

   fun isVoid () = (header.id == 0L)
}
