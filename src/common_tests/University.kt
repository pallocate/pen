package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object University
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "university" }
   private val salt = "38a5a3f320bf5bd0813c71cfa41123567302158dbaec12d311b63dbd082b47f8".parseAsHex()
   val crypto = KCrypto( passwordProvider, salt )

   fun user () = KUser( KMe(1L, KContactInfo( "University", crypto.ed25519Sha3().publicKey() ), salt) ).apply {

      relations.add(KRelation( ContactList.patricia, Target.PRODUCTION ).apply {
         roles.add( Role.CONCEDER )
         roles.add( Role.DATA_CONTROLLER )
      })

      relations.add(KRelation( ContactList.david, Target.PRODUCTION ).apply {
         roles.add( Role.CONCEDER )
         roles.add( Role.DATA_CONTROLLER )
      })
   }

   fun publicKey () = crypto.ed25519Sha3().publicKey().toHex()
}
// pk: "74b9149a328539da8a167479cd1106310b1caaa4195bf7c6476bb6386d9b5cc3"
