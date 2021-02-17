package pen.par

import pen.PasswordProvider
import pen.NoPasswordProvider

interface BlockCipher
interface PublicKeySignatures
interface SymetricCipher

abstract class Crypto (
   protected val passwordProvider : PasswordProvider,
   protected val salt : ByteArray,
   protected val othersPublickKey : ByteArray
)
{
   abstract fun blockCipher () : BlockCipher
   abstract fun pkSignatures () : PublicKeySignatures
   abstract fun symetricCipher () : SymetricCipher

   fun isVoid () = !hasPasswordProvider() && !hasSalt() && !hasOthersPublickKey()
   fun hasPasswordProvider () = passwordProvider !is NoPasswordProvider
   fun hasSalt () = salt.size > 0
   fun hasOthersPublickKey () = othersPublickKey.size > 0
}
