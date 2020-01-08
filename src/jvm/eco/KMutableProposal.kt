package pen.eco

import java.io.File
import java.nio.file.Files
import kotlinx.serialization.Serializable
import pen.Log
import pen.Constants
import pen.WrongVersionException

@Serializable
class KMutableProposal (override var header : KMutableHeader = KMutableHeader(), override val products : ArrayList<KMutableProduct> = ArrayList<KMutableProduct>( 0 )) : Proposal
{
   constructor (proposal : KProposal) : this()
   {
      header = KMutableHeader( proposal.header )

      for (product in proposal.products)
         this.products += KMutableProduct( product )
   }

   fun toKProposal () : KProposal
   {
      val products = ArrayList<KProduct>()

      for (product in products)
         products.add(KProduct( product.qty, product.id ))

      return KProposal( header.toKHeader(), products )
   }
}
