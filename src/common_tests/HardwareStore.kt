package pen.tests

import pen.parseAsHex
import pen.PasswordProvider
import pen.par.KMe
import pen.par.KContact

object HardwareStore
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "hardwarestore" }
   private val salt = "4e28dc4a0269839a9505896cd1747b174e5b5d15c8448d30012051666a8fd570".parseAsHex()

   val contact = KContact(
      10L,
      address = KContact.KAddress(
         "hardware", "supplier", "4e28dc4a0269839a9505896cd1747b174e5b5d15c8448d30012051666a8fd570".parseAsHex()
      )
   )


   fun irohaSigner () = KMe( contact, salt ).irohaSigner( passwordProvider )
}
