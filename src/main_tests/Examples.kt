package pen.tests

import pen.Constants

import pen.Filable
import pen.PasswordProvider
import pen.par.*
import pen.eco.*

object Examples
{
   object Participants
   {
      object Alice : Member, PasswordProvider, Filable
      {
         override val me = KMe( 3L, "Alice" )
         override var cRelation = KRelation()
         override var pRelation = KRelation()
         override val submitHistory = ArrayList<String>()

         init
         {
            me.salt()

            val acme = KContact( 1L, "Acme" )
            val acmeRoles = ArrayList<Role>()
            acmeRoles.add( Roles.MEMBER )
            acmeRoles.add( Roles.PRODUCER )
            pRelation = KRelation( acme, acmeRoles )

            val stMarys = KContact( 2L, "St Marys" )
            val stMarysRoles = ArrayList<Role>()
            stMarysRoles.add( Roles.MEMBER )
            stMarysRoles.add( Roles.CONSUMER )
            stMarysRoles.add( Roles.DATA_SUBJECT )
            stMarysRoles.add( Roles.COUNCIL_SIGNER )
            cRelation = KRelation( stMarys, stMarysRoles )

            submitHistory.add( "2019:7-C" )
            submitHistory.add( "2019:1-P" )
            submitHistory.add( "2020:1-C" )

         }

         override fun password () = "monkey"
      }

      object Bob : Member, PasswordProvider, Filable
      {
         override val me = KMe( 4L, "Bob" )
         override var cRelation = KRelation()
         override var pRelation = KRelation()
         override val submitHistory = ArrayList<String>()

         init
         {
            me.salt()

            val acme = KContact( 1L, "Bob" )
            val acmeRoles = ArrayList<Role>()
            acmeRoles.add( Roles.MEMBER )
            acmeRoles.add( Roles.PRODUCER )
            pRelation = KRelation( acme, acmeRoles )

            val stMarys = KContact( 2L, "St Marys" )
            val stMarysRoles = ArrayList<Role>()
            stMarysRoles.add( Roles.MEMBER )
            stMarysRoles.add( Roles.CONSUMER )
            stMarysRoles.add( Roles.DATA_SUBJECT )
            cRelation = KRelation( stMarys, stMarysRoles )
            submitHistory.add( "2019:7-C" )
            submitHistory.add( "2019:7-P" )
            submitHistory.add( "2020:1-P" )

         }

         override fun password () = "123456"
      }

      object Acme : Council, PasswordProvider
      {
         override val me = KMe( 1L, "Acme" )
         override val relations = ArrayList<KRelation>()

         init
         {
            val alice = KContact( 3L, "Alice" )
            val aliceRoles = ArrayList<Role>()
            aliceRoles.add( Roles.COUNCIL )
            relations.add(KRelation( alice, aliceRoles ))

            val bob = KContact( 4L, "Bob" )
            val bobsRoles = ArrayList<Role>()
            bobsRoles.add( Roles.COUNCIL )
            relations.add(KRelation( bob, bobsRoles ))
         }

         override fun password () = "password"
      }

      object FPC : Council, PasswordProvider
      {
         override val me = KMe( 5L, "FPC" )
         override val relations = ArrayList<KRelation>()
         override fun password () = "pipe2019"
      }

      object StMarys : Council, PasswordProvider
      {
         override val me = KMe( 2L, "St Marys" )
         override val relations = ArrayList<KRelation>()

         init
         {
            val alice = KContact( 3L, "Alice" )
            val aliceRoles = ArrayList<Role>()
            aliceRoles.add( Roles.COUNCIL )
            relations.add(KRelation( alice, aliceRoles ))

            val bob = KContact( 4L, "Bob" )
            val bobsRoles = ArrayList<Role>()
            bobsRoles.add( Roles.COUNCIL )
            relations.add(KRelation( bob, bobsRoles ))
         }

         override fun password () = "residents"
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
