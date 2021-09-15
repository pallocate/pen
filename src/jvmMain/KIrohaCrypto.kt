package pen

import java.security.KeyPair
import jp.co.soramitsu.crypto.ed25519.Ed25519Sha3
import org.bouncycastle.jcajce.provider.digest.SHA3

class KIrohaSignatory (private val seed : ByteArray) : IrohaSignatory
{
   private val keyPair by lazy {genKeyPair()}

   /** Genarates a KeyPair from a seed. */
   private fun genKeyPair () : KeyPair =  Ed25519Sha3().generateKeypair( seed )

   override fun sign (input : ByteArray) : String
   {
      val hash = sha3Digest( input )
      return Ed25519Sha3().rawSign( hash, keyPair ).toHex()
   }

   override fun publicKey () = keyPair.getPublic().encoded.toHex()
}
