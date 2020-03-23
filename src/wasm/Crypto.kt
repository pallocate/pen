package pen

import pen.PasswordProvider

/** Skeleton code for web assembly. */
actual object Crypto : Loggable
{
   actual fun randomBytes (number : Int) = ByteArray( 0 )
   actual fun digest (input : ByteArray) = ByteArray( 0 )
   actual fun signText (text : ByteArray, passwordProvider : PasswordProvider, pkcSalt : ByteArray) = ByteArray( 0 )
   actual fun verifyText (signedText : ByteArray, othersPublicKey : ByteArray) = ByteArray( 0 )
   actual fun signatureOf (input : ByteArray, passwordProvider : PasswordProvider, pkcSalt : ByteArray) = ByteArray( 0 )
   actual fun verifySignatureOf (input : ByteArray, signature : ByteArray, othersPublicKey : ByteArray) = false
   actual fun signBinary (binary : ByteArray, passwordProvider : PasswordProvider, pkcSalt : ByteArray) = ByteArray( 0 )
   actual fun verifyBinary (signedBinary : ByteArray, othersPublicKey : ByteArray) = ByteArray( 0 )
   actual fun encrypt (plainText : ByteArray, passwordProvider : PasswordProvider, skcSalt : ByteArray) = ByteArray( 0 )
   actual fun decrypt (input : ByteArray, passwordProvider : PasswordProvider, skcSalt : ByteArray) = ByteArray( 0 )
   actual fun pkcEncrypt (plainText : ByteArray, passwordProvider : PasswordProvider, pkcSalt : ByteArray, othersPublicKey : ByteArray) = ByteArray( 0 )
   actual fun pkcDecrypt (input : ByteArray, passwordProvider : PasswordProvider, pkcSalt : ByteArray, othersPublicKey : ByteArray) = ByteArray( 0 )
   actual fun publicSigningKeySize () = 0
   actual fun saltSize () = 0
   actual fun publicKey (password : ByteArray, salt : ByteArray) = ByteArray( 0 )
   actual fun getKey (passwordProvider : PasswordProvider, salt : ByteArray, keyFunction : (ByteArray, ByteArray) -> ByteArray) = ByteArray( 0 )
   override fun originName () = ""
}
