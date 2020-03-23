package pen.tests

import platform.posix.getpid
import sodium.crypto_box_PUBLICKEYBYTES

class KCryptoTests
{
   fun shit ()
   {
      println("Hello, The pid is ${getpid()}")
   }

   fun testConstants ()
   {
      Assertions.assertEquals( crypto_box_PUBLICKEYBYTES.toLong(), 32L )
   }
}
