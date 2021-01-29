package pen.par

import org.bouncycastle.crypto.util.PublicKeyFactory
import org.bouncycastle.crypto.util.PrivateKeyFactory
import org.bouncycastle.crypto.engines.RSAEngine
//import org.bouncycastle.crypto.encodings.PKCS1Encoding

class KRsa (private val othersPublickKey : ByteArray = ByteArray(0))
{
   fun encrypt (plainText : ByteArray) : ByteArray
   {
      val publicKey = PublicKeyFactory.createKey( othersPublickKey )
      val cipher = RSAEngine().apply {init( true, publicKey )}
//      val cipher = PKCS1Encoding(RSAEngine().apply {init( true, publicKey )} )

      return cipher.processBlock( plainText, 0, plainText.size )
   }

   fun decrypt (cipherText : ByteArray) : ByteArray
   {
      val privateKey = PrivateKeyFactory.createKey( othersPublickKey )
      return ByteArray( 0 )
   }
}
