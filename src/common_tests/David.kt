package pen.tests

import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object David
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "david" }
   private val salt = "317360b40f32aceff6a3f71d0d6b1fc8cdcebcfff67b16e3348ba5f378b2e549".parseAsHex()

   val contact = KContact(
      4L,
      KContact.KInfo( "David" ),
      KContact.KAddress(
         "david", "crowbeach", "12de0687108198b9c420350911b9f5667142f39cf15ce199edd0406297ef830d".parseAsHex()
      )
   )

   private val me by lazy {KMe( contact, salt )}

   fun irohaSigner () = me.irohaSigner( passwordProvider )

   fun user () = KUser( me ).apply {

      relations.add(KRelation( Contacts.crowbeach, Target.CONSUMPTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( Contacts.university, Target.PRODUCTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( Contacts.farmlands, Target.PRODUCTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( Contacts.hospital, Target.PRODUCTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })
   }
}
