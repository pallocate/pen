package pen.utils

import pen.parseAsHex
import pen.PasswordProvider
import pen.par.KMe
import pen.par.KContact

object Admin
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "admin" }
   private val salt = "6c4d810f497c4562d70e11e8c329add6dc946a3e0d0be419446864a903a2f046".parseAsHex()

   val contact = KContact(
      0L,
      KContact.KInfo( "Admin" ),
      KContact.KAddress( "admin", "system" )
   )

   fun irohaSigner () = KMe( contact, salt ).irohaSigner( passwordProvider )
}
// pk: "d22675d937af2c0cea3a54057148e8c3467fea9ae08236c2fd2a2e06f68fba79"
