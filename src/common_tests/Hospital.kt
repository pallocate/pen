package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object Hospital
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "hospital" }
   private val salt = "bae2566b167023507c86692209a502723f4bdb3fd8eb7d81cb4307d47d7e68fa".parseAsHex()
   val crypto = KCrypto( passwordProvider, salt )

   fun user () = KUser( KMe(6L, KContactInfo( "Hospital", crypto.pkSignatures().publicKey() ), salt) ).apply {

      relations.add(KRelation( ContactList.david, Target.PRODUCTION ).apply {
         roles.add( Role.CONCEDER )
         roles.add( Role.DATA_CONTROLLER )
      })

      relations.add(KRelation( ContactList.factory ).apply {
         roles.add( Role.CUSTOMER )
      })
   }

   fun publicKey () = crypto.pkSignatures().publicKey().toHex()
}
// pk: "9a542cbb04e415ce61e46217f2609305816fa665ac18bcb8a02504a2df3534f7"
