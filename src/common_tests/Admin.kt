package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.par.KMe
import pen.par.KIrohaCrypto

object Admin
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "admin" }
   private val salt = "6c4d810f497c4562d70e11e8c329add6dc946a3e0d0be419446864a903a2f046".parseAsHex()
   val crypto = KIrohaCrypto( passwordProvider, salt )

   fun publicKey () = crypto.pkSignatures().publicKey().toHex()
}
// pk: "3a08a5901253adbf377be6ed8bd08ffe2d5ecd368393535004eb52907a437220"
