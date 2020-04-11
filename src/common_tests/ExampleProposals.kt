package pen.tests

import pen.eco.*

object ExampleProposals
{
   fun proposal () : KProposal
   {
      val header = KHeader(
         id                                   = 212,
         iteration                            = 0,
         version                              = 1,
         timestamp                            = 0L
      )

      val product = KProduct( 1L, 10000L )
      val items = listOf<KProduct>( product )

      return KProposal( header, items )
   }

   fun mutableProposal () = KMutableProposal( proposal() )
}
