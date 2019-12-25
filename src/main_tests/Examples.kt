package pen.tests

import pen.Constants

import pen.PasswordProvider
import pen.par.*
import pen.eco.*

object Examples
{
   object Participants
   {
      val alicePwd = object : PasswordProvider { override fun password () = "monkey" }
      val bobsPwd = object : PasswordProvider { override fun password () = "123456" }
      val acmePwd = object : PasswordProvider { override fun password () = "password" }
      val fpcPwd = object : PasswordProvider { override fun password () = "pipe2019" }
      val stMarysPwd = object : PasswordProvider { override fun password () = "residents" }

      fun alice () = KMember().apply {
         me.contactId = 3L
         me.name = "Alice"
         me.salt()

         producerRelation = KRelation().apply {
            other = KContact( 1L, "Acme" )
            roles = ArrayList<Role>().apply {
               add( Role.SUBMITTER )
               add( Role.PRODUCER )
            }
         }

         consumerRelation = KRelation().apply {
            other = KContact( 2L, "St Marys" )
            roles = ArrayList<Role>().apply {
               add( Role.SUBMITTER )
               add( Role.CONSUMER )
               add( Role.DATA_SUBJECT )
               add( Role.COUNCIL_SIGNER )
            }
         }
//         submitHistory.add( "2019:7-C" )
      }

      fun bob () = KMember().apply {
         me.contactId = 4L
         me.name = "Bob"
         me.salt()

         consumerRelation = KRelation().apply {
            other = KContact( 2L, "St Marys" )
            roles = ArrayList<Role>().apply {
               add( Role.SUBMITTER )
               add( Role.CONSUMER )
               add( Role.DATA_SUBJECT )
            }
         }

         producerRelation = KRelation().apply {
            other = KContact( 1L, "Bob" )
            roles = ArrayList<Role>().apply {
               add( Role.SUBMITTER )
               add( Role.PRODUCER )
            }
         }
      }

      fun acme () = KCouncil().apply {
         me.contactId = 1L
         me.name = "Acme"

         relations.add(KRelation().apply {
            other = KContact( 5L, "FPC" )
            roles.add( Role.SUPPLIER )
         })

         relations.add(KRelation().apply {
            other = KContact( 3L, "Alice" )
            roles.add( Role.CONSIDER )
         })

         relations.add(KRelation().apply {
            other = KContact( 4L, "Bob" )
            roles.add( Role.CONSIDER )
         })
      }

      fun fpc () = KCouncil().apply {
         me.contactId = 5L
         me.name = "FPC"

         relations.add(KRelation().apply {
            other = KContact( 1L, "Acme" )
            roles.add( Role.CUSTOMER )
         })
      }

      fun stMarys () = KCouncil().apply {
         me.contactId = 2L
         me.name = "St Marys"

         relations.add(KRelation().apply {
            other = KContact( 3L, "Alice" )
            roles.add( Role.CONSIDER )
         })

         relations.add(KRelation().apply {
            other = KContact( 4L, "Bob" )
            roles.add( Role.CONSIDER )
         })
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
