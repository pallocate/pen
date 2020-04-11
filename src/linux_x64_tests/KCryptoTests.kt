package pen.tests

import sodium.crypto_box_PUBLICKEYBYTES
import pen.Crypto
import pen.KeyType

object Pwd : pen.PasswordProvider
{ override fun password () = "Hello" }

class KCryptoTests
{
   fun testConstants ()
   {
      val salt = Array<Byte>( Crypto.saltSize(), { 0x69 } ).toByteArray()
      val key = Crypto.key( Pwd, salt, KeyType.PUBLIC )
      println(kotlinx.serialization.internal.HexConverter.printHexBinary( key ))

      Assertions.assertEquals( crypto_box_PUBLICKEYBYTES.toLong(), 32L )
   }
}
