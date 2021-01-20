package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object CrowBeach
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "crowbeach" }
   private val salt = "cf7ab9dc5bbebd3670c91d255706e5da6941aaa5fca2fecb569c6fca437f5be7".parseAsHex()
   val crypto = KCrypto( passwordProvider, salt )

   fun user () = KUser( KMe(8L, KContactInfo( "Crow beach", crypto.ed25519Sha3().publicKey() ), salt) ).apply {

      relations.add(KRelation( ContactList.david, Target.CONSUMPTION ).apply {
         roles.add( Role.CONCEDER )
         roles.add( Role.DATA_CONTROLLER )
      })
   }

   fun publicKey () = crypto.ed25519Sha3().publicKey().toHex()
}
// pk: "d6f9d33c5a15dcbbff232c9bd2f4dbe78b2ff6528f1c4ff94ec1f6ac90d1fd0e"
