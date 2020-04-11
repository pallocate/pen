package pen.tests

import pen.par.*
import pen.eco.Target

object ExampleCouncils
{
   fun university () : KCouncil
   {
      val me = KMe.factory( 1L, "University", "", ExamplePasswords.password( 1 ))
      return KCouncil( me ).apply {

         val patricia = KContact( 3L, "Patricia" )
         relations.add(KRelation( patricia, Target.PRODUCTION ).apply {
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })

         val david = KContact( 4L, "David" )
         relations.add(KRelation( david, Target.PRODUCTION ).apply {
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })
      }
   }

   fun bbaCables () : KCouncil 
   {
      val me = KMe.factory( 5L, "BBA Cables", "", ExamplePasswords.password( 5 ))
      return KCouncil( me ).apply {
      
         val patricia = KContact( 3L, "Patricia" )
         relations.add(KRelation( patricia, Target.PRODUCTION ).apply {
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })

         val hospital = KContact( 6L, "Hospital" )
         relations.add(KRelation( hospital ).apply {
            roles.add( Role.SUPPLIER )
         })
      }
   }

   fun farmlands () : KCouncil
   {      
      val me = KMe( 7L, "Farmlands cooperative" )
      return KCouncil( me ).apply {

         val patricia = KContact( 3L, "Patricia" )
         relations.add(KRelation( patricia, Target.PRODUCTION ).apply {
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })

         val david = KContact( 4L, "David" )
         relations.add(KRelation( david, Target.PRODUCTION ).apply {
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })
      }
   }

   fun hospital () : KCouncil 
   {      
      val me = KMe( 6L, "Hospital" )
      return KCouncil( me ).apply {

         val david = KContact( 4L, "David" )
         relations.add(KRelation( david, Target.PRODUCTION ).apply {
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })

         val bbaCables = KContact( 5L, "BBA Cables" )
         relations.add(KRelation( bbaCables ).apply {
            roles.add( Role.CUSTOMER )
         })
      }
   }

   fun loboStreet20 () : KCouncil
   {
      val me = KMe( 8L, "Lobo street 20-30" )
      return KCouncil( me ).apply {

         val david = KContact( 4L, "David" )
         relations.add(KRelation( david, Target.CONSUMPTION ).apply {
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })
      }
   }

   fun artysan () : KCouncil
   {
      val me = KMe.factory( 2L, "Artysan CC", "", ExamplePasswords.password( 2 ))
      return KCouncil( me ).apply {

         val patricia = KContact( 3L, "Patricia" )
         relations.add(KRelation( patricia, Target.CONSUMPTION ).apply {
            roles.add( Role.CONCEDER )
            roles.add( Role.DATA_CONTROLLER )
         })
      }
   }
}
