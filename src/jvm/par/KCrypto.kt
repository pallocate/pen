package pen.par

import pen.PasswordProvider
import pen.NoPasswordProvider

class KCrypto (
   private val passwordProvider : PasswordProvider = NoPasswordProvider,
   private val salt : ByteArray = ByteArray(0),
   private val othersPublickKey : ByteArray = ByteArray(0)
) : Crypto
{
   companion object
   { fun void () = KCrypto() }

   fun aes () = KAes( passwordProvider, salt )
   fun ed25519Sha3 () = KEd25519Sha3( passwordProvider, salt, othersPublickKey )
   fun rsa () = KRsa( othersPublickKey )

   fun isVoid () = !hasPasswordProvider() && !hasSalt() && !hasOthersPublickKey()

   fun hasPasswordProvider () = passwordProvider !is NoPasswordProvider
   fun hasSalt () = salt.size > 0
   fun hasOthersPublickKey () = othersPublickKey.size > 0
}
