package pen.par

import java.util.Objects
import java.io.InputStream
import java.io.OutputStream
import org.bouncycastle.crypto.io.CipherInputStream
import org.bouncycastle.crypto.io.CipherOutputStream
import org.bouncycastle.crypto.modes.CBCBlockCipher
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher
import org.bouncycastle.crypto.engines.AESEngine
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.crypto.params.ParametersWithIV
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

import pen.Log
import pen.Constants
import pen.randomBytes
import pen.PasswordProvider
import pen.NoPasswordProvider

class KAes (
   private val passwordProvider : PasswordProvider = NoPasswordProvider(),
   private val salt : ByteArray = ByteArray(0)
) : Crypto
{
   private fun aesCipher (forEncryption : Boolean, nonce : ByteArray) : PaddedBufferedBlockCipher
   {
      if (passwordProvider is NoPasswordProvider)
         Log.warn( "A valid PasswordProvider is required!" )

      val passwordChars = passwordProvider.password().toCharArray()
      val keySpec = PBEKeySpec( passwordChars, salt, 100000, Constants.AES_KEY_BITS )
      val keyFactory = SecretKeyFactory.getInstance( Constants.PKBD_ALGORITHM )
      val key = keyFactory.generateSecret( keySpec ).getEncoded()

      val cipher = PaddedBufferedBlockCipher(CBCBlockCipher( AESEngine() ))
      val keyParam = KeyParameter( key )
      val params = ParametersWithIV( keyParam, nonce )
      cipher.init( forEncryption, params )

      return cipher
   }

   fun encryptToStream (bytes : ByteArray, outputStream : OutputStream)
   {
      val nonce = randomBytes( Constants.AES_NONCE_SIZE )
      outputStream.write( nonce )

      CipherOutputStream(outputStream, aesCipher( true, nonce )).use { it.write( bytes ) }
   }

   fun decryptFromStream (inputStream : InputStream) : ByteArray
   {
      var ret : ByteArray
      val nonce = ByteArray( Constants.AES_NONCE_SIZE ).also { inputStream.read( it ) }

      CipherInputStream(inputStream, aesCipher( false, nonce )).use { cipherStream ->
         ret = ByteArray( inputStream.available() )
         cipherStream.read( ret )
      }

      return ret
   }
}
