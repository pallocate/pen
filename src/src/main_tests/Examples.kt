package pen.tests

import pen.eco.Constants
import pen.eco.types.*
import pen.par.*

object Examples
{
   object Participants
   {
      object Alice : Member(), PasswordProvider
      {
         val account = Account()

         init
         {
            me = Me(Contact( 3L ))
            me.pkcSalt()
            submitHistory.add( "2017:12" )
            submitHistory.add( "2018:1" )

            val producerRole = KProducer()
            val consumerRole = KConsumer()
            producerRole.setCouncil( "Acme", Acme.me.contact )
            consumerRole.setCouncil( "St Marys", StMarys.me.contact )
            consumerRole.submitHistory.add( "Consumption:2018:1" )
            producerRole.submitHistory.add( "Production:2017:7" )
            producerRole.me.pkcSalt()
            producerRole.me.skcSalt()
            consumerRole.me.pkcSalt()
            consumerRole.me.skcSalt()

            account.roles.apply {
               add( producerRole )
               add( consumerRole )
               add( DataSubject() )
               add( CouncilSigner() )
            }
            account.isLoaded = true
         }

         override fun password () = "monkey"
         override var name = "Alice"
         override var icon = ""
      }

      object Bob : Member(), PasswordProvider
      {
         val account = Account()

         init
         {
            me = Me(Contact( 4L ))
            me.pkcSalt()
            submitHistory.add( "2017:12" )

            val producerRole = KProducer()
            val consumerRole = KConsumer()

            producerRole.setCouncil( "Acme", Acme.me.contact )
            consumerRole.setCouncil( "St Marys", StMarys.me.contact )
            consumerRole.submitHistory.add( "Consumption:2017:7" )
            producerRole.submitHistory.add( "Production:2018:1" )

            account.roles.apply {
               add( producerRole )
               add( consumerRole )
               add( DataSubject() )
            }
            account.isLoaded = true
         }

         override fun password () = "123456"
         override var name = "Bob"
         override var icon = ""
      }

      object Acme : Council(), PasswordProvider
      {
         val account = Account()

         init
         {
            val councilRole = Council()
            me = Me(Contact( 1L ))
            councilRole.addMember( "Alice", Alice.me.contact )
            councilRole.addMember( "Bob", Bob.me.contact )

            account.roles.apply {
               add( councilRole )
               add( KConsumer() )
               add( KProducer() )
               add( DataController() )
            }
            account.isLoaded = true
         }

         override fun password () = "password"
         override var name = "Acme"
         override var icon = ""
      }

      object FPC : Council(), PasswordProvider
      {
         val account = Account()

         init
         { me = Me(Contact( 5L )) }

         override fun password () = "123456"
         override var name = "pipe2019"
         override var icon = ""
      }

      object StMarys : Council(), PasswordProvider
      {
         val account = Account()

         init
         {
            val councilRole = Council()
            me = Me(Contact( 2L ))
            councilRole.addMember( "Alice", Alice.me.contact )
            councilRole.addMember( "Bob", Bob.me.contact )

            account.roles.apply {
               add( councilRole )
               add( KConsumer() )
               add( DataController() )
            }
            account.isLoaded = true
         }

         override fun password () = "residents"
         override var name = "StMarys"
         override var icon = ""
      }
   }

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
