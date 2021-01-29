package pen.par

import java.security.KeyPair
import pen.sha3Digest
import pen.PasswordProvider
import pen.NoPasswordProvider
import pen.Constants
import org.bouncycastle.crypto.generators.SCrypt
import jp.co.soramitsu.crypto.ed25519.Ed25519Sha3

class KEd25519Sha3 (
   private val passwordProvider : PasswordProvider = NoPasswordProvider,
   private val salt : ByteArray = ByteArray(0),
   private val othersPublickKey : ByteArray = ByteArray(0)
)
{
   private val keyPair by lazy {genKeyPair()}

   /** Genarates a KeyPair using a seed. The seed is a SCrypt hash from password and salt */
   private fun genKeyPair () : KeyPair
   {
      val pwd = passwordProvider.password().toByteArray()
      val seed = SCrypt.generate( pwd, salt, 65536, 15, 3, Constants.SEED_SIZE )

      return Ed25519Sha3().generateKeypair( seed )
   }

   /** Proved authentication by signing a challenge using a cryptographic hash. */
   fun prove (challenge : ByteArray) = sign( challenge )
   fun sign (input : ByteArray) : ByteArray
   {
      val hash = sha3Digest( input )
      return Ed25519Sha3().rawSign( hash, keyPair )
   }

   fun publicKey () = keyPair.getPublic().encoded
}
