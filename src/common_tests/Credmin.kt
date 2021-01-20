package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.par.KMe
import pen.par.KCrypto

object Credmin
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "credmin" }
   private val salt = "112c8707c1a59774fd32d3424dd38072ad3a1fbe12a590c3a3e6840ff961dc16".parseAsHex()
   val crypto = KCrypto( passwordProvider, salt )

   fun publicKey () = crypto.ed25519Sha3().publicKey().toHex()
}
// pk: "d9bb072d1c464bf5a8e47e037497ebd9a64f470f77fc46e489cff80a3236ef96"
