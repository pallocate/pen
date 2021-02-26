package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object Factory
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "factory" }
   private val salt = "3d4e366579f5e6d14a0e05fcf5c12b521fa1101d6f3cc740fbd219a1d8978477".parseAsHex()
   val crypto = KIrohaCrypto( passwordProvider, salt )

   fun user () = KUser( KMe(5L, KContactInfo( "Furniture Factory", crypto.pkSignatures().publicKey() ), salt) ).apply {

      relations.add(KRelation( ContactList.patricia, Target.PRODUCTION ).apply {
         roles.add( Role.CONCEDER )
         roles.add( Role.DATA_CONTROLLER )
      })

      relations.add(KRelation( ContactList.hospital ).apply {
         roles.add( Role.SUPPLIER )
      })
   }

   fun publicKey () = crypto.pkSignatures().publicKey().toHex()
}
// pk: "16d6d1d0afd3e044ea110e58c227c5ae97e847c943dff9e641ec50a69a758629"
