package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object Farmlands
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "farmlands" }
   private val salt = "cb0812c14288ff2c70fd79fecf8cc27a2b553799be3f5092664223fd988b371a".parseAsHex()
   val crypto = KCrypto( passwordProvider, salt )

   fun user () = KUser( KMe(7L, KContactInfo( "Farmlands", crypto.ed25519Sha3().publicKey() ), salt) ).apply {

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
// pk: "e41ab2d67e444ad52a93dbc2974609a1b4eb44219d0c5345acebae05a694cadd"
