package pen.tests

import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object CrowBeach
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "crowbeach" }
   private val salt = "cf7ab9dc5bbebd3670c91d255706e5da6941aaa5fca2fecb569c6fca437f5be7".parseAsHex()

   val contact = KContact(
      8L,
      KContact.KInfo( name = "Crow beach" ),
      KContact.KAddress(
         "crowbeach", "commons", "4824d1cf3fb0936fe3c77b112c6e602e3e2c6251302b2449c4b368b3dd10350a".parseAsHex()
      )
   )


   private val me by lazy {KMe( contact, salt )}

   fun irohaSigner () = me.irohaSigner( passwordProvider )

   fun user () = KUser( me ).apply {
      relations.add(KRelation( Contacts.david, Target.CONSUMPTION ).apply {
         roles.add( Role.CONCEDER )
         roles.add( Role.DATA_CONTROLLER )
      })
   }
}
