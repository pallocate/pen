package pen.tests

import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object Factory
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "factory" }
   private val salt = "3d4e366579f5e6d14a0e05fcf5c12b521fa1101d6f3cc740fbd219a1d8978477".parseAsHex()

   val contact = KContact(
      5L,
      KContact.KInfo( "Factory" ),
      KContact.KAddress(
         "factory", "commons", "91b8f1b69e835622fb6fe3113cecc59f792392caa470a4a7dd4f726cf481ae87".parseAsHex()
      )
   )


   private val me by lazy {KMe( contact, salt )}

   fun irohaSigner () = me.irohaSigner( passwordProvider )

   fun user () = KUser( me ).apply {

      relations.add(KRelation( Contacts.patricia, Target.PRODUCTION ).apply {
         roles.add( Role.CONCEDER )
         roles.add( Role.DATA_CONTROLLER )
      })

      relations.add(KRelation( Contacts.hospital ).apply {
         roles.add( Role.SUPPLIER )
      })
   }
}
