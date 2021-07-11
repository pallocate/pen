package pen.utils

import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object Hospital
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "hospital" }
   private val salt = "bae2566b167023507c86692209a502723f4bdb3fd8eb7d81cb4307d47d7e68fa".parseAsHex()

   val contact = KContact(
      6L,
      KContact.KInfo( "Hospital" ),
      KContact.KAddress(
         "hospital", "commons", "c65f69a31e2d37550b42fe297712b163c8237840b4220c8cc2c29a6ad4fcaed7".parseAsHex()
      )
   )


   private val me by lazy {KMe( contact, salt )}

   fun irohaSigner () = me.irohaSigner( passwordProvider )

   fun user () = KUser( me ).apply {

      relations.add(KRelation( Contacts.david, Target.PRODUCTION ).apply {
         roles.add( Role.CONCEDER )
         roles.add( Role.DATA_CONTROLLER )
      })

      relations.add(KRelation( Contacts.factory ).apply {
         roles.add( Role.CUSTOMER )
      })
   }
}
