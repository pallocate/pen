package pen.eco

import pen.eco.Constants
import pen.eco.Config
import pen.eco.Loggable
import pen.eco.LogLevel.WARN
import pen.eco.LogLevel.ERROR
import pen.eco.types.PasswordProvider
import pen.eco.types.NoPasswordProvider

expect fun sodiumInstance () : Sodium

/** Cryptographic functionallity, using the Sodium library. */
object Crypto : Loggable
{
   private val sodium = sodiumInstance()
   private val SIGNAGE_LABEL = byteArrayOf( 0x0A, 0x53, 0x69, 0x67, 0x6E, 0x61, 0x74, 0x75, 0x72, 0x65, 0x3A )
   private val B64_SIGN_BYTES = 4 * if (Constants.SIGN_BYTES%3 == 1) (Constants.SIGN_BYTES + 2)/3 else
   if (Constants.SIGN_BYTES%3 == 2) (Constants.SIGN_BYTES + 1)/3 else Constants.SIGN_BYTES/3

   /** Generates high entropy pseudo random bytes.
     * @param number The number of bytes to be generated.
     * @return The output bytes. */
   fun randomBytes (number : Int) : ByteArray
   {
      sodium.sodium_init()
      val ret = ByteArray( number )
      sodium.randombytes_buf( ret, number )

      return ret
   }

   /** Does a digest of input.
     * Algorithm: BLAKE2b
     * @return The hash. */
   fun digest (input : ByteArray) : ByteArray
   {
      sodium.sodium_init()
      var output = ByteArray( Constants.HASH_BYTES )
      if (sodium.crypto_generichash( output, Constants.HASH_BYTES, input, input.size, null, 0 ) != 0)
         log("Hashing failed!", Config.flag( "CRYPTO" ), WARN)

      return output
   }

   /** Signs a text with a base64 encoded signature at the end.
     * Algorithm: Ed25519
     * @param text The text to be signed.
     * @return The text with an added signature. */
   fun signText (text : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : ByteArray
   {
      log("Signing text", Config.flag( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkc_salt, this::privateKey )

      if (validateSKSize( secretKey ) && text.size > 0)
      {
         val signature = ByteArray( sodium.crypto_sign_ed25519_bytes() )

         if (sodium.crypto_sign_ed25519_detached( signature, 0L, text, text.size.toLong(), secretKey ) != 0)
            log("Text signing failed!", Config.flag( "CRYPTO" ), WARN)
         else
            ret = text + SIGNAGE_LABEL + encode_b64( signature )
      }
      else
         log("Text signing failed! (invalid key/input)", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   /** Verifies text using signature message from the end, which is then stripped off.
     * Algorithm: Ed25519
     * @param signedText The signed text to be verified.
     * @param othersPublicKey The senders public key.
     * @return The original text. */
   fun verifyText (signedText : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("Verifying text", Config.flag( "CRYPTO" ))
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
         { signature = decode_b64( signatureBase64 ) }
         catch (t : Throwable)
         { log("Decoding Base64 failed!" , Config.flag( "PLATFORM" ), ERROR) }

         if (sodium.crypto_sign_ed25519_verify_detached( signature, text, text.size.toLong(), othersPublicKey ) == 0)
            ret = text
         else
            log("Text verification failed!", Config.flag( "CRYPTO" ), WARN)
      }
      else
         log("Text verification failed! (invalid key/input)", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   /** Returns only the signature of the input.
     * Algorithm: Ed25519
     * @param input The data to calculate signature from.
     * @return The signature. */
   fun signatureOf (input : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : ByteArray
   {
      log("Generating a signature from input", Config.flag( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkc_salt, this::privateKey )

      if (validateSKSize( secretKey ) && input.size > 0)
      {
         val signature = ByteArray( sodium.crypto_sign_ed25519_bytes() )

         if (sodium.crypto_sign_ed25519_detached( signature, 0L, input, input.size.toLong(), secretKey ) == 0)
            ret = signature
         else
            log("Signing failed!", Config.flag( "CRYPTO" ), WARN)
      }
      else
         log("Signing failed! (invalid key/input)", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   /** Verifies some signed data.
     * Algorithm: Ed25519
     * @param input The data that was signed.
     * @param signature The signature of the data.
     * @param othersPublicKey The signers public key.
     * @return True if signature matches. */
   fun verifySignatureOf (input : ByteArray, signature : ByteArray, othersPublicKey : ByteArray) : Boolean
   {
      log("Verifying signature..", Config.flag( "CRYPTO" ))
      var success = false
      sodium.sodium_init()

      if (validatePKSize( othersPublicKey ) && input.size > 0)
      {
         if (sodium.crypto_sign_ed25519_verify_detached( signature, input, input.size.toLong(), othersPublicKey) == 0)
            success = true
         else
            log("Verification failed!", Config.flag( "CRYPTO" ), WARN)
      }
      else
         log("Verification failed! (invalid key/input)", Config.flag( "CRYPTO" ), WARN)

      return success
   }

   /** Signs a binary message.
     * Algorithm: Ed25519
     * @param binary The binary to be signed.
     * @return The signed binary. */
   fun signBinary (binary : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : ByteArray
   {
      log("Signing binary", Config.flag( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkc_salt, this::privateKey )

      if (validateSKSize( secretKey ) && binary.size > 0)
      {
         ret = ByteArray( sodium.crypto_sign_ed25519_bytes() + binary.size )

         if (sodium.crypto_sign_ed25519(ret, 0L, binary, binary.size.toLong(), secretKey) != 0)
            log("Binary signing failed!", Config.flag( "CRYPTO" ), WARN)
      }
      else
         log("Binary signing failed! (invalid key/input)", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   /** Verifies a signed binary message.
     * Algorithm: Ed25519
     * @param signedBinary The signed message.
     * @param othersPublicKey The senders public key.
     * @return The original binary. */
   fun verifyBinary (signedBinary : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("Verifying binary", Config.flag( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val mSize = signedBinary.size - sodium.crypto_sign_ed25519_bytes()
      sodium.sodium_init()

      if (validatePKSize( othersPublicKey ) && mSize > 0)
      {
         ret = ByteArray( mSize )

         if (sodium.crypto_sign_ed25519_open( ret, 0L, signedBinary, signedBinary.size.toLong(), othersPublicKey ) != 0)
            log("Binary verification failed!", Config.flag( "CRYPTO" ), WARN)
      }
      else
      {
         ret = ret.drop( sodium.crypto_sign_ed25519_bytes() ).toByteArray()     // Drop supposed signature
         log("Binary verification failed! (invalid key/input)", Config.flag( "CRYPTO" ), WARN)
      }

      return ret
   }

   /** Performes a symetric encryption of the plain text, and puts the used nonce in the beginning.
     * Algorithm: XSalsa20, Poly1305 MAC
     * @param plainText The plain text to be encrypted.
     * @return The nonce plus the cipher text. */
   fun encrypt (plainText : ByteArray, passwordProvider : PasswordProvider, skc_salt : ByteArray) : ByteArray
   {
      log("Encrypting", Config.flag( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val key = getKey( passwordProvider, skc_salt, this::symetricKey )

      if (validateKeySize( key ) && plainText.size > 0)
      {
         ret = ByteArray( (sodium.crypto_box_macbytes() as Number).toInt() + plainText.size )
         val nonce = randomBytes( (sodium.crypto_secretbox_noncebytes() as Number).toInt() )

         if (sodium.crypto_secretbox_easy( ret, plainText, plainText.size.toLong(), nonce, key ) == 0)
            ret = nonce + ret
         else
         {
            log("Encryption failed!", Config.flag( "CRYPTO" ), WARN)
            ret = ByteArray( 0 )
         }
      }
      else
         log("Encryption failed! (invalid key/input)", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   /** Does a symetric decryption of the input.
     * Algorithm: XSalsa20, Poly1305 MAC
     * @param input Input made up by nonce plus cipher text.
     * @return The original plain text. */
   fun decrypt (input : ByteArray, passwordProvider : PasswordProvider, skc_salt : ByteArray) : ByteArray
   {
      log("Decrypting", Config.flag( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val nonceSize = (sodium.crypto_secretbox_noncebytes() as Number).toInt()
      val plainTextSize = (input.size - (sodium.crypto_secretbox_macbytes() as Number).toInt()) - nonceSize
      val key = getKey( passwordProvider, skc_salt, this::symetricKey )

      if (validateKeySize( key ) && plainTextSize > 0)
      {
         val nonce = input.copyOf( nonceSize )
         val cipherText = input.drop( nonceSize ).toByteArray()

         ret = ByteArray( cipherText.size - (sodium.crypto_box_macbytes() as Number).toInt() )
         if (sodium.crypto_secretbox_open_easy (ret, cipherText, cipherText.size.toLong(), nonce, key) != 0)
         {
            log("Decryption failed!", Config.flag( "CRYPTO" ), WARN)
            ret = ByteArray( 0 )
         }
      }
      else
         log("Decryption failed! (invalid key/input)", Config.flag( "CRYPTO" ), ERROR)

      return ret
   }

   /** Encrypts plain text using public key cryptography.
     * Algorithm: X25519, XSalsa20, Poly1305 MAC
     * @param plainText The plain text to be encrypted.
     * @param othersPublicKey The recievers public key.
     * @return The nonce plus the cipher text. */
   fun pkcEncrypt (plainText : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("PKC encrypting", Config.flag( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkc_salt, this::privateKey )

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
            log("PKC encryption failed!", Config.flag( "CRYPTO" ), WARN)
            ret = ByteArray( 0 )
         }
      }
      else
         log("PKC encryption failed! (invalid keys/input)", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   /** Decrypts input using public key cryptography.
     * Algorithm: X25519, XSalsa20, Poly1305 MAC
     * @param input Input made up by nonce plus cipher text.
     * @param othersPublicKey The senders public key.
     * @return The original plain text. */
   fun pkcDecrypt (input : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      log("PKC decrypting..", Config.flag( "CRYPTO" ))
      var ret = ByteArray( 0 )
      val nonceSize = (sodium.crypto_box_noncebytes() as Number).toInt()
      val plainTextSize = (input.size - (sodium.crypto_box_macbytes() as Number).toInt()) - nonceSize
      val secretKey = getKey( passwordProvider, pkc_salt, this::privateKey )

      if (validateSKSize( secretKey ) && validatePKSize( othersPublicKey ) && plainTextSize > 0)
      {
         val nonce = input.copyOf( nonceSize )
         val cipherText = input.drop( nonceSize ).toByteArray()
         val pk = convertPK( othersPublicKey )
         val sk = convertSK( secretKey )

         ret = ByteArray( plainTextSize )
         if (sodium.crypto_box_open_easy(ret, cipherText, cipherText.size.toLong(), nonce, pk, sk) != 0)
         {
            log("PKC decryption failed!", Config.flag( "CRYPTO" ), WARN)
            ret = ByteArray( 0 )
         }
      }
      else
         log("PKC decryption failed! (invalid keys/input)", Config.flag( "CRYPTO" ), ERROR)

      return ret
   }

   /** The key size requiered by the ed25519 signing algorithm. */
   fun publicSigningKeySize () = (sodium.crypto_sign_ed25519_publickeybytes() as Number).toInt()
   /** The salt size requiered in the password based key derivation. */
   fun saltSize () = sodium.crypto_pwhash_saltbytes()
   override fun originName () = "Crypto"

   /** generates a symetric key from a password and salt */
   private fun symetricKey (password : ByteArray, salt : ByteArray) : ByteArray
   {
      val symKeySize = (sodium.crypto_secretbox_keybytes() as Number).toInt()
      var ret = ByteArray( symKeySize )

      if (sodium.crypto_pwhash( ret, symKeySize.toLong(), password, password.size.toLong(), salt, sodium.crypto_pwhash_opslimit_moderate(),
      sodium.crypto_pwhash_memlimit_moderate(), sodium.crypto_pwhash_alg_argon2id13() ) != 0)
         log("Key generation failed!", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   /** generates a private key from a password and salt */
   private fun privateKey (password : ByteArray, salt : ByteArray) : ByteArray
   {
      var ret = ByteArray( 0 )
      val seedSize = (sodium.crypto_sign_seedbytes() as Number).toInt()
      val seed = ByteArray( seedSize )

      if (sodium.crypto_pwhash( seed, seedSize.toLong(), password, password.size.toLong(), salt, sodium.crypto_pwhash_opslimit_moderate(),
      sodium.crypto_pwhash_memlimit_moderate(), sodium.crypto_pwhash_alg_argon2id13() ) == 0)
      {
         val foo = ByteArray( publicSigningKeySize() )
         ret = ByteArray( secretSigningKeySize() )

         if (sodium.crypto_sign_seed_keypair( foo, ret, seed ) != 0)
            log("Keypair generation failed!", Config.flag( "CRYPTO" ), WARN)
      }
      else
         log("Password seeding failed!", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   /** generates a public key from a password and salt */
   fun publicKey (password : ByteArray, salt : ByteArray) : ByteArray
   {
      var ret = ByteArray( 0 )
      val seedSize = (sodium.crypto_sign_seedbytes() as Number).toInt()
      val seed = ByteArray( seedSize )

      if (sodium.crypto_pwhash( seed, seedSize.toLong(), password, password.size.toLong(), salt, sodium.crypto_pwhash_opslimit_moderate(),
      sodium.crypto_pwhash_memlimit_moderate(), sodium.crypto_pwhash_alg_argon2id13() ) == 0)
      {
         ret = ByteArray( publicSigningKeySize() )
         val foo = ByteArray( secretSigningKeySize() )

         if (sodium.crypto_sign_seed_keypair( ret, foo, seed ) != 0)
            log("Keypair generation failed!", Config.flag( "CRYPTO" ), WARN)
      }
      else
         log("Password seeding failed!", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   /** Intiates the sodium library object. Gets a password from the provider,
     * and uses this in the supplied key function.
     * @param passwordProvider The PasswordProvider to use.
     * @param salt Salt to use in key function. */
   fun getKey (passwordProvider : PasswordProvider, salt : ByteArray, keyFunction : (ByteArray, ByteArray) -> ByteArray) : ByteArray
   {
      var ret = ByteArray( 0 )

      if (passwordProvider is NoPasswordProvider)
         log("Password input needed!", Config.flag( "CRYPTO" ), WARN)
      else
         if (validateSaltSize( salt ))
         {
            val password = Utils.stringToByteArray( passwordProvider.password() )

            if (password.size == 0)
               log("Invalid password!", Config.flag( "CRYPTO" ), WARN)
            else
            {
               sodium.sodium_init()
               ret = keyFunction( password, salt )
            }
         }
         else
            log("Invalid salt", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   private fun convertPK (publicSigningKey : ByteArray) : ByteArray
   {
      var ret = ByteArray( (sodium.crypto_box_publickeybytes() as Number).toInt() )

      if (sodium.crypto_sign_ed25519_pk_to_curve25519( ret, publicSigningKey ) != 0)
         log("Key conversion failed!", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   private fun convertSK (secretSigningKey : ByteArray) : ByteArray
   {
      var ret = ByteArray( (sodium.crypto_box_secretkeybytes() as Number).toInt() )

      if (sodium.crypto_sign_ed25519_sk_to_curve25519( ret, secretSigningKey ) != 0)
         log("Key conversion failed!", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   private fun validateKeySize (key : ByteArray) : Boolean
   {
      var ret = false
      if (key.size == (sodium.crypto_secretbox_keybytes() as Number).toInt())
         ret = true
      else
         log("Invalid key!", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   private fun validatePKSize (pk : ByteArray) : Boolean
   {
      var ret = false
      if (pk.size == publicSigningKeySize())
         ret = true
      else
         log("Invalid public key!", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   private fun validateSKSize (sk : ByteArray) : Boolean
   {
      var ret = false
      if (sk.size == secretSigningKeySize())
         ret = true
      else
         log("Invalid secret key!", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   private fun validateSaltSize (salt : ByteArray) : Boolean
   {
      var ret = false
      if (salt.size == saltSize())
         ret = true
      else
         log("Invalid salt!", Config.flag( "CRYPTO" ), WARN)

      return ret
   }

   private fun secretSigningKeySize () = (sodium.crypto_sign_ed25519_secretkeybytes() as Number).toInt()
}
