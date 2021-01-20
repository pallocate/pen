package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object Artysan
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "artysan" }
   private val salt = "21842d52a0339468907591d6989213e8cc5f53522a0af6ad17966c6891a887a3".parseAsHex()
   val crypto = KCrypto( passwordProvider, salt )

   fun participant () = KUser( KMe(2L, KContactInfo( "Artysan", crypto.ed25519Sha3().publicKey() ), salt) ).apply {

      relations.add(KRelation( ContactList.patricia, Target.CONSUMPTION ).apply {
         roles.add( Role.CONCEDER )
         roles.add( Role.DATA_CONTROLLER )
      })
   }

   fun publicKey () = crypto.ed25519Sha3().publicKey().toHex()
}
// pk: "16f8a186263327290b0e8e12140499adefa7ad1e6ca35772a1b6058efa291c3c"
