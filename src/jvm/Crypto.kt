package pen

import pen.LogLevel.WARN
import pen.LogLevel.ERROR
import pen.PasswordProvider
import pen.NoPasswordProvider

/** Cryptographic functionallity, using the Sodium library. */
actual object Crypto : Loggable
{
   private val sodium = sodiumInstance()
   private val SIGNAGE_LABEL = byteArrayOf( 0x0A, 0x53, 0x69, 0x67, 0x6E, 0x61, 0x74, 0x75, 0x72, 0x65, 0x3A )
   private val SIGN_BYTES = sodium.crypto_sign_ed25519_bytes()
   private val B64_SIGN_BYTES = 4 * if (SIGN_BYTES%3 == 1) (SIGN_BYTES + 2)/3 else
   if (SIGN_BYTES%3 == 2) (SIGN_BYTES + 1)/3 else SIGN_BYTES/3

   actual fun randomBytes (number : Int) : ByteArray
   {
      log("Generating random bytes", Config.trigger( "CRYPTO" ))
      sodium.sodium_init()
      val ret = ByteArray( number )
      sodium.randombytes_buf( ret, number )

      return ret
   }

   /** Using BLAKE2b alorithm from the Sodium library. */
   actual fun digest (input : ByteArray) : ByteArray
   {
      sodium.sodium_init()
      var output = ByteArray( Constants.HASH_BYTES )
      if (sodium.crypto_generichash( output, Constants.HASH_BYTES, input, input.size, null, 0 ) != 0)
         log("Hashing failed!", Config.trigger( "CRYPTO" ), WARN)

      return output
   }

   /** Using Ed25519 alorithm from the Sodium library. */
   actual fun signText (text : ByteArray, passwordProvider : PasswordProvider, pkcSalt : ByteArray) : ByteArray
   {
      log("Signing text", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkcSalt, this::privateKey )

      if (validateSKSize( secretKey ) && text.size > 0)
      {
         val signature = ByteArray( sodium.crypto_sign_ed25519_bytes() )

         if (sodium.crypto_sign_ed25519_detached( signature, 0L, text, text.size.toLong(), secretKey ) != 0)
            log("Text signing failed!", Config.trigger( "CRYPTO" ), WARN)
         else
            ret = text + SIGNAGE_LABEL + Utils.encodeB64( signature ).toByteArray()
      }
      else
         log("Text signing failed! (invalid key/input)", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   /** Using Ed25519 alorithm from the Sodium library. */
   actual fun verifyText (signedText : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("Verifying text", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      sodium.sodium_init()

      if (validatePKSize( othersPublicKey ) && signedText.size > (B64_SIGN_BYTES + SIGNAGE_LABEL.size))
      {
         val signatureFrom = signedText.size - B64_SIGN_BYTES
         val textTo = signatureFrom - SIGNAGE_LABEL.size

         val text = signedText.copyOf( textTo )
         val signatureBase64 = signedText.copyOfRange( signatureFrom, signedText.size )

         var signature = ByteArray( 0 )
         try
         { signature = Utils.decodeB64(String( signatureBase64 )) }
         catch (t : Throwable)
         { log("Decoding Base64 failed!" , Config.trigger( "PLATFORM" ), ERROR) }

         if (sodium.crypto_sign_ed25519_verify_detached( signature, text, text.size.toLong(), othersPublicKey ) == 0)
            ret = text
         else
            log("Text verification failed!", Config.trigger( "CRYPTO" ), WARN)
      }
      else
         log("Text verification failed! (invalid key/input)", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   /** Using Ed25519 alorithm from the Sodium library. */
   actual fun signatureOf (input : ByteArray, passwordProvider : PasswordProvider, pkcSalt : ByteArray) : ByteArray
   {
      log("Generating a signature from input", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkcSalt, this::privateKey )

      if (validateSKSize( secretKey ) && input.size > 0)
      {
         val signature = ByteArray( sodium.crypto_sign_ed25519_bytes() )

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
   actual fun verifySignatureOf (input : ByteArray, signature : ByteArray, othersPublicKey : ByteArray) : Boolean
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
   actual fun signBinary (binary : ByteArray, passwordProvider : PasswordProvider, pkcSalt : ByteArray) : ByteArray
   {
      log("Signing binary", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkcSalt, this::privateKey )

      if (validateSKSize( secretKey ) && binary.size > 0)
      {
         ret = ByteArray( sodium.crypto_sign_ed25519_bytes() + binary.size )

         if (sodium.crypto_sign_ed25519(ret, 0L, binary, binary.size.toLong(), secretKey) != 0)
            log("Binary signing failed!", Config.trigger( "CRYPTO" ), WARN)
      }
      else
         log("Binary signing failed! (invalid key/input)", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   /** Using Ed25519 alorithm from the Sodium library. */
   actual fun verifyBinary (signedBinary : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("Verifying binary", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val mSize = signedBinary.size - sodium.crypto_sign_ed25519_bytes()
      sodium.sodium_init()

      if (validatePKSize( othersPublicKey ) && mSize > 0)
      {
         ret = ByteArray( mSize )

         if (sodium.crypto_sign_ed25519_open( ret, 0L, signedBinary, signedBinary.size.toLong(), othersPublicKey ) != 0)
            log("Binary verification failed!", Config.trigger( "CRYPTO" ), WARN)
      }
      else
      {
         ret = ret.drop( sodium.crypto_sign_ed25519_bytes() ).toByteArray()     // Drop supposed signature
         log("Binary verification failed! (invalid key/input)", Config.trigger( "CRYPTO" ), WARN)
      }

      return ret
   }

   /** Using XSalsa20 and Poly1305 MAC alorithm from the Sodium library. */
   actual fun encrypt (plainText : ByteArray, passwordProvider : PasswordProvider, skcSalt : ByteArray) : ByteArray
   {
      log("Encrypting", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val key = getKey( passwordProvider, skcSalt, this::symetricKey )

      if (validateKeySize( key ) && plainText.size > 0)
      {
         ret = ByteArray( (sodium.crypto_box_macbytes() as Number).toInt() + plainText.size )
         val nonce = randomBytes( (sodium.crypto_secretbox_noncebytes() as Number).toInt() )

         if (sodium.crypto_secretbox_easy( ret, plainText, plainText.size.toLong(), nonce, key ) == 0)
            ret = nonce + ret
         else
         {
            log("Encryption failed!", Config.trigger( "CRYPTO" ), WARN)
            ret = ByteArray( 0 )
         }
      }
      else
         log("Encryption failed! (invalid key/input)", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   /** Using XSalsa20 and Poly1305 MAC alorithm from the Sodium library. */
   actual fun decrypt (input : ByteArray, passwordProvider : PasswordProvider, skcSalt : ByteArray) : ByteArray
   {
      log("Decrypting", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val nonceSize = (sodium.crypto_secretbox_noncebytes() as Number).toInt()
      val plainTextSize = (input.size - (sodium.crypto_secretbox_macbytes() as Number).toInt()) - nonceSize
      val key = getKey( passwordProvider, skcSalt, this::symetricKey )

      if (validateKeySize( key ) && plainTextSize > 0)
      {
         val nonce = input.copyOf( nonceSize )
         val cipherText = input.drop( nonceSize ).toByteArray()

         ret = ByteArray( cipherText.size - (sodium.crypto_box_macbytes() as Number).toInt() )
         if (sodium.crypto_secretbox_open_easy (ret, cipherText, cipherText.size.toLong(), nonce, key) != 0)
         {
            log("Decryption failed!", Config.trigger( "CRYPTO" ), WARN)
            ret = ByteArray( 0 )
         }
      }
      else
         log("Decryption failed! (invalid key/input)", Config.trigger( "CRYPTO" ), ERROR)

      return ret
   }

   /** Using X25519, XSalsa20 and Poly1305 MAC alorithm from the Sodium library. */
   actual fun pkcEncrypt (plainText : ByteArray, passwordProvider : PasswordProvider, pkcSalt : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("PKC encrypting", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkcSalt, this::privateKey )

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
            ret = ByteArray( 0 )
         }
      }
      else
         log("PKC encryption failed! (invalid keys/input)", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   /** Using X25519, XSalsa20 and Poly1305 MAC alorithm from the Sodium library. */
   actual fun pkcDecrypt (input : ByteArray, passwordProvider : PasswordProvider, pkcSalt : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("PKC decrypting", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val nonceSize = (sodium.crypto_box_noncebytes() as Number).toInt()
      val plainTextSize = (input.size - (sodium.crypto_box_macbytes() as Number).toInt()) - nonceSize
      val secretKey = getKey( passwordProvider, pkcSalt, this::privateKey )

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
            ret = ByteArray( 0 )
         }
      }
      else
         log("PKC decryption failed! (invalid keys/input)", Config.trigger( "CRYPTO" ), ERROR)

      return ret
   }

   actual fun publicSigningKeySize () = (sodium.crypto_sign_ed25519_publickeybytes() as Number).toInt()
   actual fun saltSize () = sodium.crypto_pwhash_saltbytes()
   override fun originName () = "Crypto"

   private fun symetricKey (password : ByteArray, salt : ByteArray) : ByteArray
   {
      log("Generating symetric key", Config.trigger( "CRYPTO" ))
      val symKeySize = (sodium.crypto_secretbox_keybytes() as Number).toInt()
      var ret = ByteArray( symKeySize )

      if (sodium.crypto_pwhash( ret, symKeySize.toLong(), password, password.size.toLong(), salt, sodium.crypto_pwhash_opslimit_moderate(),
      sodium.crypto_pwhash_memlimit_moderate(), sodium.crypto_pwhash_alg_argon2id13() ) != 0)
         log("Key generation failed!", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   private fun privateKey (password : ByteArray, salt : ByteArray) : ByteArray
   {
      log("Generating private key", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val seedSize = (sodium.crypto_sign_seedbytes() as Number).toInt()
      val seed = ByteArray( seedSize )

      if (sodium.crypto_pwhash( seed, seedSize.toLong(), password, password.size.toLong(), salt, sodium.crypto_pwhash_opslimit_moderate(),
      sodium.crypto_pwhash_memlimit_moderate(), sodium.crypto_pwhash_alg_argon2id13() ) == 0)
      {
         val foo = ByteArray( publicSigningKeySize() )
         ret = ByteArray( secretSigningKeySize() )

         if (sodium.crypto_sign_seed_keypair( foo, ret, seed ) != 0)
            log("Keypair generation failed!", Config.trigger( "CRYPTO" ), WARN)
      }
      else
         log("Password seeding failed!", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   actual fun publicKey (password : ByteArray, salt : ByteArray) : ByteArray
   {
      log("Generating public key", Config.trigger( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val seedSize = (sodium.crypto_sign_seedbytes() as Number).toInt()
      val seed = ByteArray( seedSize )

      if (sodium.crypto_pwhash( seed, seedSize.toLong(), password, password.size.toLong(), salt, sodium.crypto_pwhash_opslimit_moderate(),
      sodium.crypto_pwhash_memlimit_moderate(), sodium.crypto_pwhash_alg_argon2id13() ) == 0)
      {
         ret = ByteArray( publicSigningKeySize() )
         val foo = ByteArray( secretSigningKeySize() )

         if (sodium.crypto_sign_seed_keypair( ret, foo, seed ) != 0)
            log("Keypair generation failed!", Config.trigger( "CRYPTO" ), WARN)
      }
      else
         log("Password seeding failed!", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

   actual fun getKey (passwordProvider : PasswordProvider, salt : ByteArray, keyFunction : (ByteArray, ByteArray) -> ByteArray) : ByteArray
   {
      var ret = ByteArray( 0 )

      if (passwordProvider is NoPasswordProvider)
         log("Password input needed!", Config.trigger( "CRYPTO" ), WARN)
      else
         if (validateSaltSize( salt ))
         {
            val password = passwordProvider.password().toByteArray()

            if (password.size == 0)
               log("Invalid password!", Config.trigger( "CRYPTO" ), WARN)
            else
            {
               sodium.sodium_init()
               ret = keyFunction( password, salt )
            }
         }
         else
            log("Invalid salt", Config.trigger( "CRYPTO" ), WARN)

      return ret
   }

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

   private fun secretSigningKeySize () = (sodium.crypto_sign_ed25519_secretkeybytes() as Number).toInt()
}
