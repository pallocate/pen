package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.par.KMe
import pen.par.KCrypto

object HardwareStore
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "hardwarestore" }
   private val salt = "4e28dc4a0269839a9505896cd1747b174e5b5d15c8448d30012051666a8fd570".parseAsHex()
   val crypto = KCrypto( passwordProvider, salt )

   fun publicKey () = crypto.ed25519Sha3().publicKey().toHex()
}
// pk: "d136ef69a5bd7ebc5586ee1902f4ed6ca96d52d57c95ea2d96f79a693c94ca0c"
