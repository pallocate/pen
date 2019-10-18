package pen.tests

import pen.eco.Constants
import pen.eco.types.*
import pen.par.TestParticipant

object Examples
{
   object Alice : TestParticipant ()
   {val pwd = object : PasswordProvider { override fun password () = "monkey" }}

   object Bob : TestParticipant ()
   {val pwd = object : PasswordProvider { override fun password () = "123456" }}

   object Acme : TestParticipant ()
   {val pwd = object : PasswordProvider { override fun password () = "qwerty" }}

   object Proposal
   {
      fun proposal () : KProposal
      {
         val header = KHeader(
            level                                = 3,
            flags                                = Constants.IS_PROPOSAL,
            id                                   = 212,
            iteration                            = 0,
            version                              = 1,
            timestamp                            = 0L
         )

         val item = KItem( 1L, 27131505L )
         val items = listOf<KItem>( item )

         return KProposal( header, items )
      }

      fun mutableProposal () : KMutableProposal
      {
         val header = KMutableHeader(
            level                                = 3,
            flags                                = Constants.IS_PROPOSAL,
            id                                   = 212,
            iteration                            = 0,
            version                              = 1,
            timestamp                            = 0L
         )

         val product = KQuantableProduct  (
                                             id = 27131505L,
                                             name = "Pneumatic drill",
                                             desc = "Heavy pneumatic drill",
                                             amount = 1F,
                                             unit = "piece",
                                             down = 5,
                                             up = 5,
                                             price = 100000L,
                                             analogue = "false"
                                          )
         product.qty = 1
         val products = arrayListOf<KQuantableProduct>( product, product )

         return KMutableProposal( header, products )
      }
   }
}
