package pen.tests

import pen.PasswordProvider

object ExamplePasswords
{
   private val passwords = arrayOf( "monkey", "123456", "password", "pipe2019", "residents" )

   fun password (n : Int) = object : PasswordProvider {
      override fun password () = passwords[n - 1]
   }
}
