package pen.tests

import pen.Constants

import pen.PasswordProvider
import pen.par.*
import pen.eco.*

object Examples
{
   object Participants
   {
      object Alice : TestParticipant(), PasswordProvider
      {
         val account = Account()

         init
         {
            me = Me(Contact( 3L ))
            me.pkcSalt()

            val producerRole = KProducer()
            val consumerRole = KConsumer()

            producerRole.setCouncil( "Acme", Acme.me.contact )
            consumerRole.setCouncil( "St Marys", StMarys.me.contact )
            consumerRole.submitHistory.add( "2019:7" )
            producerRole.submitHistory.add( "2019:1" )
            consumerRole.submitHistory.add( "2020:1" )

            with( account.roles ) {
               add( producerRole )
               add( consumerRole )
               add( DataSubject() )
               add( CouncilSigner() )
            }
            account.isLoaded = true
         }

         override fun password () = "monkey"
      }

      object Bob : TestParticipant(), PasswordProvider
      {
         val account = Account()

         init
         {
            me = Me(Contact( 4L ))
            me.pkcSalt()

            val producerRole = KProducer()
            val consumerRole = KConsumer()

            producerRole.setCouncil( "Acme", Acme.me.contact )
            consumerRole.setCouncil( "St Marys", StMarys.me.contact )
            consumerRole.submitHistory.add( "2019:7" )
            producerRole.submitHistory.add( "2019:7" )
            producerRole.submitHistory.add( "2020:1" )

            with( account.roles ) {
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

            with( account.roles ) {
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

      object FPC : TestParticipant(), PasswordProvider
      {
         val account = Account()
         val councilRole = Council()

         init
         { me = Me(Contact( 5L )) }

         override fun password () = "123456"
         override var name = "pipe2019"
         override var icon = ""
      }

      object StMarys : TestParticipant(), PasswordProvider
      {
         val account = Account()
         val councilRole = Council()

         init
         {
            me = Me(Contact( 2L ))
            councilRole.addMember( "Alice", Alice.me.contact )
            councilRole.addMember( "Bob", Bob.me.contact )

            with( account.roles ) {
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
         product.qty = 10000
         val products = arrayListOf<KQuantableProduct>( product, product )

         return KMutableProposal( header, products )
      }
   }
}
