package pen.tests.examples

import pen.eco.common.Utils
import pen.eco.common.PasswordProvider
import pen.par.Me
import pen.par.Contact

class Acme
{
   val me = Me(Contact( 1L ))

   init
   {me.publicKey( PWD )}

   object PWD : PasswordProvider
   { override fun password () = "password" }
}

class FPC
{
   val me = Me(Contact( 5L ))

   init
   {me.publicKey( PWD )}

   object PWD : PasswordProvider
   { override fun password () = "pipe34" }
}

class StMarys
{
   val me = Me(Contact( 2L ))

   init
   {me.publicKey( PWD )}

   object PWD : PasswordProvider
   { override fun password () = "stmarys" }
}

class Alice
{
   val me = Me(Contact( 3L ))

   init
   {me.publicKey( PWD )}

   object PWD : PasswordProvider
   { override fun password () = "monkey" }
}

class Bob
{
   val me = Me(Contact( 4L ))

   init
   {me.publicKey( PWD )}

   object PWD : PasswordProvider
   { override fun password () = "123456" }
}
