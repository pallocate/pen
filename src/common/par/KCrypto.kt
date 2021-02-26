package pen.par

import pen.Config
import pen.Constants
import pen.coerceToInt
import pen.Loggable
import pen.LogLevel.WARN
import pen.LogLevel.ERROR
import pen.PasswordProvider
import pen.VoidPasswordProvider

/** Cryptographic functionallity, using the Sodium library. */
class KCrypto (private val passwordProvider : PasswordProvider, private val salt : ByteArray) : Loggable
{
   companion object
   {
      val VOID_BYTES = ByteArray( 0 )
      fun void () = KCrypto( VoidPasswordProvider, VOID_BYTES )
   }

   private val sodium = sodiumInstance()

   fun randomBytes (number : Int) : ByteArray
   {
      log("Generating random bytes", Config.trigger( "CRYPTO" ))
      sodium.sodium_init()
      return ByteArray( number ).also {sodium.randombytes_buf( it, number.toLong() )}
   }

   /** Using BLAKE2b alorithm from the Sodium library. */
   fun digest (input : ByteArray) : ByteArray
   {
      log("Hashing input", Config.trigger( "CRYPTO" ))

      sodium.sodium_init()
      return ByteArray( Constants.HASH_BYTES ).also {
         if (sodium.crypto_generichash( it, Constants.HASH_BYTES.toLong(), input, input.size.toLong(), null, 0 ) != 0)
            log("Hashing failed!", Config.trigger( "CRYPTO" ), WARN)
      }
   }

   /** Using Ed25519 alorithm from the Sodium library. */
   fun signDetached (input : ByteArray) : ByteArray
   {
      log("Generating a signature from input", Config.trigger( "CRYPTO" ))
      var ret = VOID_BYTES
      val secretKey = keyPairKey( secret = true )

      if (validateSKSize( secretKey ) && input.size > 0)
      {
         val signature = ByteArray( signBytes() )

         if (sodium.crypto_sign_ed25519_detached( signature, 0L, input, input.size.toLong(), secretKey ) == 0)
            ret = signature
         else
            log("Signing failed!", Config.trigger( "CRYPTO" ), WARN)
      }
      else
         log("Signing failed! (invalid key/input)", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   /** Using Ed25519 alorithm from the Sodium library. */
   fun verifyDetached (input : ByteArray, signature : ByteArray, othersPublicKey : ByteArray) : Boolean
   {
      log("Verifying signature", Config.trigger( "CRYPTO" ))
      var success = false
      sodium.sodium_init()

      if (validatePKSize( othersPublicKey ) && input.size > 0)
      {
         if (sodium.crypto_sign_ed25519_verify_detached( signature, input, input.size.toLong(), othersPublicKey) == 0)
            success = true
         else
            log("Verification failed!", Config.trigger( "CRYPTO" ), WARN)
      }
      else
         log("Verification failed! (invalid key/input)", Config.trigger( "CRYPTO" ), WARN)

      return success
   }

   /** Using Ed25519 alorithm from the Sodium library. */
   fun sign (binary : ByteArray) : ByteArray
   {
      log("Signing binary", Config.trigger( "CRYPTO" ))
      var ret = VOID_BYTES
      val secretKey = keyPairKey( secret = true )

      if (validateSKSize( secretKey ) && binary.size > 0)
      {
         ret = ByteArray( signBytes() + binary.size )
         if (sodium.crypto_sign_ed25519(ret, 0L, binary, binary.size.toLong(), secretKey) != 0)
            log("Binary signing failed!", Config.trigger( "CRYPTO" ), WARN)
      }
      else
         log("Binary signing failed! (invalid key/input)", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   /** Using Ed25519 alorithm from the Sodium library. */
   fun verify (signedBinary : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("Verifying binary", Config.trigger( "CRYPTO" ))
      var ret = VOID_BYTES
      val mSize = signedBinary.size - signBytes()
      sodium.sodium_init()

      if (validatePKSize( othersPublicKey ) && mSize > 0)
      {
         ret = ByteArray( mSize ).also {
            if (sodium.crypto_sign_ed25519_open( it, 0L, signedBinary, signedBinary.size.toLong(), othersPublicKey ) != 0)
               log("Binary verification failed!", Config.trigger( "CRYPTO" ), WARN)
         }
      }
      else
      {
         ret = ret.drop( signBytes() ).toByteArray()     // Drop supposed signature
         log("Binary verification failed! (invalid key/input)", Config.trigger( "CRYPTO" ), WARN)
      }

      return ret
   }

   /** Using XSalsa20 and Poly1305 MAC alorithm from the Sodium library. */
   fun encrypt (plainText : ByteArray) : ByteArray
   {
      log("Encrypting", Config.trigger( "CRYPTO" ))
      var ret = VOID_BYTES
      val key = keyPairKey( secret = true )

      if (validateKeySize( key ) && plainText.size > 0)
      {
         ret = ByteArray( (sodium.crypto_box_macbytes() as Number).toInt() + plainText.size )
         val nonce = randomBytes( (sodium.crypto_secretbox_noncebytes() as Number).toInt() )

         if (sodium.crypto_secretbox_easy( ret, plainText, plainText.size.toLong(), nonce, key ) == 0)
            ret = nonce + ret
         else
         {
            log("Encryption failed!", Config.trigger( "CRYPTO" ), WARN)
            ret = VOID_BYTES
         }
      }
      else
         log("Encryption failed! (invalid key/input)", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   /** Using XSalsa20 and Poly1305 MAC alorithm from the Sodium library. */
   fun decrypt (input : ByteArray) : ByteArray
   {
      log("Decrypting", Config.trigger( "CRYPTO" ))
      var ret = VOID_BYTES
      val nonceSize = (sodium.crypto_secretbox_noncebytes() as Number).toInt()
      val plainTextSize = (input.size - (sodium.crypto_secretbox_macbytes() as Number).toInt()) - nonceSize
      val key = keyPairKey( secret = true )

      if (validateKeySize( key ) && plainTextSize > 0)
      {
         val nonce = input.copyOf( nonceSize )
         val cipherText = input.drop( nonceSize ).toByteArray()

         ret = ByteArray( cipherText.size - (sodium.crypto_box_macbytes() as Number).toInt() )
         if (sodium.crypto_secretbox_open_easy (ret, cipherText, cipherText.size.toLong(), nonce, key) != 0) {
            log("Decryption failed!", Config.trigger( "CRYPTO" ), WARN)
            ret = VOID_BYTES
         }
      }
      else
         log("Decryption failed! (invalid key/input)", Config.trigger( "CRYPTO" ), ERROR)

      return ret
   }

   /** Using X25519, XSalsa20 and Poly1305 MAC alorithm from the Sodium library. */
   fun pkcEncrypt (plainText : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("PKC encrypting", Config.trigger( "CRYPTO" ))
      var ret = VOID_BYTES
      val secretKey = keyPairKey( secret = true )

      if (validateSKSize( secretKey ) && validatePKSize( othersPublicKey ) && plainText.size > 0)
      {
         ret = ByteArray( (sodium.crypto_box_macbytes() as Number).toInt() + plainText.size )
         val pk = convertPK( othersPublicKey )
         val sk = convertSK( secretKey )
         val nonce = randomBytes( (sodium.crypto_box_noncebytes() as Number).toInt() )

         if (sodium.crypto_box_easy( ret, plainText, plainText.size.toLong(), nonce, pk, sk ) == 0)
            ret = nonce + ret
         else
         {
            log("PKC encryption failed!", Config.trigger( "CRYPTO" ), WARN)
            ret = VOID_BYTES
         }
      }
      else
         log("PKC encryption failed! (invalid keys/input)", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   /** Using X25519, XSalsa20 and Poly1305 MAC alorithm from the Sodium library. */
   fun pkcDecrypt (input : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("PKC decrypting", Config.trigger( "CRYPTO" ))
      var ret = VOID_BYTES
      val nonceSize = (sodium.crypto_box_noncebytes() as Number).toInt()
      val plainTextSize = (input.size - (sodium.crypto_box_macbytes() as Number).toInt()) - nonceSize
      val secretKey = keyPairKey( secret = true )

      if (validateSKSize( secretKey ) && validatePKSize( othersPublicKey ) && plainTextSize > 0)
      {
         val nonce = input.copyOf( nonceSize )
         val cipherText = input.drop( nonceSize ).toByteArray()
         val pk = convertPK( othersPublicKey )
         val sk = convertSK( secretKey )

         ret = ByteArray( plainTextSize )
         if (sodium.crypto_box_open_easy(ret, cipherText, cipherText.size.toLong(), nonce, pk, sk) != 0)
         {
            log("PKC decryption failed!", Config.trigger( "CRYPTO" ), WARN)
            ret = VOID_BYTES
         }
      }
      else
         log("PKC decryption failed! (invalid keys/input)", Config.trigger( "CRYPTO" ), ERROR)

      return ret
   }

   /** Generates keypair and returns one of the keys. */
   fun keyPairKey (secret : Boolean = false) : ByteArray
   {
      log("Generating keypair", Config.trigger( "CRYPTO" ))
      var ret = VOID_BYTES

      if (!isVoid())
      {
         val derivedKey = deriveKey()

         if (derivedKey.size > 0)
         {
            val publicKey = ByteArray( publicSigningKeySize() )
            val secretKey = ByteArray( secretSigningKeySize() )

            if (sodium.crypto_sign_ed25519_seed_keypair( publicKey, secretKey, derivedKey ) == 0)
            {
               if (secret)
                  ret = secretKey
               else
                  ret = publicKey
            }
            else
               log("Keypair generation failed!", Config.trigger( "CRYPTO" ), WARN)
         }
      }

      return ret
   }

   /** Derives a key from the password and salt. */
   fun deriveKey () : ByteArray
   {
      log("Deriving key", Config.trigger( "CRYPTO" ))
      var ret = VOID_BYTES
      if (!isVoid())
      {
         val password = passwordProvider.password().toByteArray()
         val key = ByteArray( seedSize() )

         if (sodium.crypto_pwhash( key, seedSize().toLong(), password, password.size.toLong(), salt, sodium.crypto_pwhash_opslimit_moderate(),
            sodium.crypto_pwhash_memlimit_moderate(), sodium.crypto_pwhash_alg_default() ) == 0)
         else
            log("Key derivation failed!", Config.trigger( "CRYPTO" ), WARN)

         ret = key
      }

      return ret
   }

   fun symetricKey () : ByteArray
   {
      val key = deriveKey()

      return if (key.size == symetricKeySize())
            key
         else
            VOID_BYTES
   }

   fun symetricKeySize () = sodium.crypto_secretbox_keybytes().coerceToInt()
   fun seedSize () = sodium.crypto_sign_ed25519_seedbytes().coerceToInt()
   fun publicSigningKeySize () = sodium.crypto_sign_ed25519_publickeybytes().coerceToInt()
   fun secretSigningKeySize () = sodium.crypto_sign_ed25519_secretkeybytes().coerceToInt()
   fun saltSize () = sodium.crypto_pwhash_saltbytes().coerceToInt()
   private fun signBytes () = sodium.crypto_sign_ed25519_bytes().coerceToInt()
   fun isVoid () = passwordProvider is VoidPasswordProvider && salt.size == 0
   override fun tag () = "Crypto"

   private fun convertPK (publicSigningKey : ByteArray) : ByteArray
   {
      var ret = ByteArray( (sodium.crypto_box_publickeybytes() as Number).toInt() )

      if (sodium.crypto_sign_ed25519_pk_to_curve25519( ret, publicSigningKey ) != 0)
         log("Key conversion failed!", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   private fun convertSK (secretSigningKey : ByteArray) : ByteArray
   {
      var ret = ByteArray( (sodium.crypto_box_secretkeybytes() as Number).toInt() )

      if (sodium.crypto_sign_ed25519_sk_to_curve25519( ret, secretSigningKey ) != 0)
         log("Key conversion failed!", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   private fun validateKeySize (key : ByteArray) : Boolean
   {
      var ret = false
      if (key.size == (sodium.crypto_secretbox_keybytes() as Number).toInt())
         ret = true
      else
         log("Invalid key!", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   private fun validatePKSize (pk : ByteArray) : Boolean
   {
      var ret = false
      if (pk.size == publicSigningKeySize())
         ret = true
      else
         log("Invalid public key!", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   private fun validateSKSize (sk : ByteArray) : Boolean
   {
      var ret = false
      if (sk.size == secretSigningKeySize())
         ret = true
      else
         log("Invalid secret key!", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   private fun validateSaltSize (salt : ByteArray) : Boolean
   {
      var ret = false
      if (salt.size == saltSize())
         ret = true
      else
         log("Invalid salt!", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }
}
