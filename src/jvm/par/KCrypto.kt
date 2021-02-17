package pen.par

import pen.PasswordProvider
import pen.NoPasswordProvider

class KCrypto (
   passwordProvider : PasswordProvider = NoPasswordProvider,
   salt : ByteArray = ByteArray(0),
   othersPublickKey : ByteArray = ByteArray(0)
) : Crypto( passwordProvider, salt, othersPublickKey )
{
   companion object
   { fun void () = KCrypto() }

   override fun blockCipher () = KAes( passwordProvider, salt )
   override fun pkSignatures () = KEd25519Sha3( passwordProvider, salt, othersPublickKey )
   override fun symetricCipher () = KRsa( othersPublickKey )
}