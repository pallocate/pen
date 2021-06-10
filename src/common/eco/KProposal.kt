package pen.eco

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import pen.Voidable

@Serializable
@SerialName( "Proposal" )
data class KProposal (val header : KHeader = KHeader(), val products : ArrayList<KProduct> = ArrayList<KProduct>()) : Voidable
{
   companion object
   { fun void () = KProposal() }

   override fun isVoid () = (header.id == 0L)
}
