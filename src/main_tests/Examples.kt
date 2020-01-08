package pen.tests

import pen.Constants

import pen.PasswordProvider
import pen.par.*
import pen.eco.*
import pen.eco.Target

object Examples
{
   object Passwords
   {
      private val passwords = arrayOf( "monkey", "123456", "password", "pipe2019", "residents" )

      fun password (n : Int) = object : PasswordProvider {
         override fun password () = passwords[n - 1]
      }
   }

   object Participants
   {
      fun alice () = KMember().apply {
         me.contactId = 3L
         me.name = "Alice"
         me.salt()

         consumerRelation = KRelation().apply {
            other = KContact( 2L, "St Marys" )
            target = Target.CONSUMPTION
            roles = ArrayList<Role>().apply {
               add( Role.PROPOSER )
               add( Role.DATA_SUBJECT )
               add( Role.COUNCIL_SIGNER )
            }
         }

         producerRelations.add(KRelation().apply {
            other = KContact( 1L, "Acme" )
            target = Target.PRODUCTION
            roles = ArrayList<Role>().apply {
               add( Role.PROPOSER )
               add( Role.DATA_SUBJECT )
            }
         })

         producerRelations.add(KRelation().apply {
            other = KContact( 5L, "FPC" )
            target = Target.PRODUCTION
            roles = ArrayList<Role>().apply {
               add( Role.PROPOSER )
               add( Role.DATA_SUBJECT )
            }
         })
      }

      fun bob () = KMember().apply {
         me.contactId = 4L
         me.name = "Bob"
         me.salt()

         consumerRelation = KRelation().apply {
            other = KContact( 2L, "St Marys" )
            target = Target.CONSUMPTION
            roles = ArrayList<Role>().apply {
               add( Role.PROPOSER )
               add( Role.DATA_SUBJECT )
            }
         }

         producerRelations.add(KRelation().apply {
            other = KContact( 1L, "Acme" )
            target = Target.PRODUCTION
            roles = ArrayList<Role>().apply {
               add( Role.PROPOSER )
               add( Role.DATA_SUBJECT )
            }
         })
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
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })

         relations.add(KRelation().apply {
            other = KContact( 4L, "Bob" )
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })
      }

      fun fpc () = KCouncil().apply {
         me.contactId = 5L
         me.name = "FPC"

         relations.add(KRelation().apply {
            other = KContact( 1L, "Acme" )
            roles.add( Role.CUSTOMER )
         })

         relations.add(KRelation().apply {
            other = KContact( 3L, "Alice" )
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })
      }

      fun stMarys () = KCouncil().apply {
         me.contactId = 2L
         me.name = "St Marys"

         relations.add(KRelation().apply {
            other = KContact( 3L, "Alice" )
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })

         relations.add(KRelation().apply {
            other = KContact( 4L, "Bob" )
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
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

         val product = KProduct( 1L, 10000L )
         val items = listOf<KProduct>( product )

         return KProposal( header, items )
      }

      fun mutableProposal () = KMutableProposal( proposal() )
   }
}
