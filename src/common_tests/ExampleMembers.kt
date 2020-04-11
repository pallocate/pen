package pen.tests

import pen.par.*
import pen.eco.Target

object ExampleMembers
{
   fun patricia () : KMember
   {
      val me = KMe.factory( 3L, "Patricia", "", ExamplePasswords.password( 3 ))
      return KMember( me ).apply {

         val artysan = KContact( 2L, "Artysan CC" )
         consumerRelation = KRelation( artysan, Target.CONSUMPTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
         }

         val bbaCables = KContact( 5L, "BBA Cables" )
         producerRelations.add(KRelation( bbaCables, Target.PRODUCTION ).apply {
               roles.add( Role.PROPOSER )
               roles.add( Role.DATA_SUBJECT )
         })

         val university = KContact( 1L, "University" )
         producerRelations.add(KRelation( university, Target.PRODUCTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
         })

         val farmland = KContact( 7L, "Farmland coop" )
         producerRelations.add(KRelation( farmland, Target.PRODUCTION ).apply {
               roles.add( Role.PROPOSER )
               roles.add( Role.DATA_SUBJECT )
               roles.add( Role.COUNCIL_SIGNER )
         })
      }
   }

   fun david () : KMember 
   {
      val me = KMe.factory( 4L, "David", "", ExamplePasswords.password( 4 ))
      return KMember( me ).apply {

         val lobos = KContact( 8L, "Lobos 20:ish" )
         consumerRelation = KRelation( lobos, Target.CONSUMPTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
         }

         val university = KContact( 1L, "University" )
         producerRelations.add(KRelation( university, Target.PRODUCTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
         })

         val hospital = KContact( 6L, "Hospital" )
         producerRelations.add(KRelation( hospital, Target.PRODUCTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
         })

         val farmland = KContact( 7L, "Farmland coop" )
         producerRelations.add(KRelation( farmland, Target.PRODUCTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
         })
      }
   }
}
