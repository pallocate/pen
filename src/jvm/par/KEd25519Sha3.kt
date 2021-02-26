package pen.par

import java.security.KeyPair
import pen.sha3Digest
import jp.co.soramitsu.crypto.ed25519.Ed25519Sha3

actual fun ed25519Sha3 (seed : ByteArray) : IrohaCrypto =
   KEd25519Sha3( seed )

class KEd25519Sha3 (private val seed : ByteArray) : IrohaCrypto
{
   private val keyPair by lazy {genKeyPair()}

   /** Genarates a KeyPair from a seed. */
   private fun genKeyPair () : KeyPair =  Ed25519Sha3().generateKeypair( seed )

   override fun sign (input : ByteArray) : ByteArray
   {
      val hash = sha3Digest( input )
      return Ed25519Sha3().rawSign( hash, keyPair )
   }

   override fun publicKey () = keyPair.getPublic().encoded
}
