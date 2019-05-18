package pen.eco.common

import java.util.Base64
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.NativeLong

/** Cryptographic functionallity, using the Sodium library.*/
object Crypto
{
   private val sodium = Native.loadLibrary( NaCl().filename, Sodium::class.java ) as Sodium
   private val B64_SIGN_BYTES = if (Config.SIGN_BYTES%3 == 1) 4*((Config.SIGN_BYTES + 2)/3) else
   if (Config.SIGN_BYTES%3 == 2) 4*((Config.SIGN_BYTES + 1)/3) else 4*(Config.SIGN_BYTES/3)
   private val SIGNAGE_LABEL = "\nSignature:".toByteArray()

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
      var output = ByteArray( Config.HASH_BYTES )
      if (sodium.crypto_generichash( output, Config.HASH_BYTES, input, input.size, null, 0 ) != 0)
         Log.warn( "Hashing failed!" )

      return output
   }

   /** Signs a text with a base64 encoded signature at the end.
     * Algorithm: Ed25519
     * @param text The text to be signed.
     * @return The text with an added signature. */
   fun signText (text : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : ByteArray
   {
      Log.debug( "Signing text.." )
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkc_salt, this::privateKey )

      if (validateSKSize( secretKey ) && text.size > 0)
      {
         val signature = ByteArray( sodium.crypto_sign_ed25519_bytes() )

         if (sodium.crypto_sign_ed25519_detached( signature, 0L, text, text.size.toLong(), secretKey ) != 0)
            Log.warn( "Text signing failed!" )
         else
            ret = text + SIGNAGE_LABEL + Base64.getEncoder().encode( signature )
      }
      else
         Log.warn( "Text signing failed! (invalid key/input)" )

      return ret
   }

   /** Verifies text using signature message from the end, which is then stripped off.
     * Algorithm: Ed25519
     * @param signedText The signed text to be verified.
     * @param othersPublicKey The senders public key.
     * @return The original text. */
   fun verifyText (signedText : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      Log.debug( "Verifying text.." )
      var ret = ByteArray( 0 )
      sodium.sodium_init()

      if (validatePKSize( othersPublicKey ) && signedText.size > (B64_SIGN_BYTES + SIGNAGE_LABEL.size))
      {
         val signatureFrom = signedText.size - B64_SIGN_BYTES
         val textTo = signatureFrom - SIGNAGE_LABEL.size

         val text = signedText.copyOf( textTo )
         val signatureBase64 = signedText.drop( signatureFrom ).toByteArray()
         val signature = Base64.getDecoder().decode( signatureBase64 )

         if (sodium.crypto_sign_ed25519_verify_detached( signature, text, text.size.toLong(), othersPublicKey ) == 0)
            ret = text
         else
            Log.warn( "Text verification failed!" )
      }
      else
         Log.warn( "Text verification failed! (invalid key/input)" )

      return ret
   }

   /** Returns only the signature of the input.
     * Algorithm: Ed25519
     * @param input The data to calculate signature from.
     * @return The signature. */
   fun signatureOf (input : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : ByteArray
   {
      Log.debug( "Getting signature of.." )
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkc_salt, this::privateKey )

      if (validateSKSize( secretKey ) && input.size > 0)
      {
         val signature = ByteArray( sodium.crypto_sign_ed25519_bytes() )

         if (sodium.crypto_sign_ed25519_detached( signature, 0L, input, input.size.toLong(), secretKey ) == 0)
            ret = signature
         else
            Log.warn( "Signing failed!" )
      }
      else
         Log.warn( "Signing failed! (invalid key/input)" )

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
      Log.debug( "Verifying signature.." )
      var success = false
      sodium.sodium_init()

      if (validatePKSize( othersPublicKey ) && input.size > 0)
      {
         if (sodium.crypto_sign_ed25519_verify_detached( signature, input, input.size.toLong(), othersPublicKey) == 0)
            success = true
         else
            Log.warn( "Verification failed!" )
      }
      else
         Log.warn( "Verification failed! (invalid key/input)" )

      return success
   }

   /** Signs a binary message.
     * Algorithm: Ed25519
     * @param binary The binary to be signed.
     * @return The signed binary. */
   fun signBinary (binary : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : ByteArray
   {
      Log.debug( "Signing binary.." )
      var ret = ByteArray( 0 )
      val secretKey = getKey( passwordProvider, pkc_salt, this::privateKey )

      if (validateSKSize( secretKey ) && binary.size > 0)
      {
         ret = ByteArray( sodium.crypto_sign_ed25519_bytes() + binary.size )

         if (sodium.crypto_sign_ed25519(ret, 0L, binary, binary.size.toLong(), secretKey) != 0)
            Log.warn( "Binary signing failed!" )
      }
      else
         Log.warn( "Binary signing failed! (invalid key/input)" )

      return ret
   }

   /** Verifies a signed binary message.
     * Algorithm: Ed25519
     * @param signedBinary The signed message.
     * @param othersPublicKey The senders public key.
     * @return The original binary. */
   fun verifyBinary (signedBinary : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      Log.debug( "Verifying binary.." )
      var ret = ByteArray( 0 )
      val mSize = signedBinary.size - sodium.crypto_sign_ed25519_bytes()
      sodium.sodium_init()

      if (validatePKSize( othersPublicKey ) && mSize > 0)
      {
         ret = ByteArray( mSize )

         if (sodium.crypto_sign_ed25519_open( ret, 0L, signedBinary, signedBinary.size.toLong(), othersPublicKey ) != 0)
            Log.warn( "Binary verification failed!" )
      }
      else
      {
         ret = ret.drop( sodium.crypto_sign_ed25519_bytes() ).toByteArray()     // Drop supposed signature
         Log.warn( "Binary verification failed! (invalid key/input)" )
      }

      return ret
   }

   /** Performes a symetric encryption of the plain text, and puts the used nonce in the beginning.
     * Algorithm: XSalsa20, Poly1305 MAC
     * @param plainText The plain text to be encrypted.
     * @return The nonce plus the cipher text. */
   fun encrypt (plainText : ByteArray, passwordProvider : PasswordProvider, skc_salt : ByteArray) : ByteArray
   {
      Log.debug( "Encrypting.." )
      var ret = ByteArray( 0 )
      val key = getKey( passwordProvider, skc_salt, this::symetricKey )

      if (validateKeySize( key ) && plainText.size > 0)
      {
         ret = ByteArray( (sodium.crypto_box_macbytes() as Number).toInt() + plainText.size )
         val nonce = randomBytes( (sodium.crypto_secretbox_noncebytes() as Number).toInt() )

         if (sodium.crypto_secretbox_easy (ret, plainText, plainText.size.toLong(), nonce, key) == 0)
            ret = nonce + ret
         else
         {
            Log.warn( "Encryption failed!" )
            ret = ByteArray( 0 )
         }
      }
      else
         Log.warn( "Encryption failed! (invalid key/input)" )

      return ret
   }

   /** Does a symetric decryption of the input.
     * Algorithm: XSalsa20, Poly1305 MAC
     * @param input Input made up by nonce plus cipher text.
     * @return The original plain text. */
   fun decrypt (input : ByteArray, passwordProvider : PasswordProvider, skc_salt : ByteArray) : ByteArray
   {
      Log.debug( "Decrypting.." )
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
            Log.warn( "Decryption failed!" )
            ret = ByteArray( 0 )
         }
      }
      else
         Log.err( "Decryption failed! (invalid key/input)" )

      return ret
   }

   /** Encrypts plain text using public key cryptography.
     * Algorithm: X25519, XSalsa20, Poly1305 MAC
     * @param plainText The plain text to be encrypted.
     * @param othersPublicKey The recievers public key.
     * @return The nonce plus the cipher text. */
   fun pkcEncrypt (plainText : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      Log.debug( "PKC encrypting.." )
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
            Log.warn( "PKC encryption failed!" )
            ret = ByteArray( 0 )
         }
      }
      else
         Log.warn( "PKC encryption failed! (invalid keys/input)" )

      return ret
   }

   /** Decrypts input using public key cryptography.
     * Algorithm: X25519, XSalsa20, Poly1305 MAC
     * @param input Input made up by nonce plus cipher text.
     * @param othersPublicKey The senders public key.
     * @return The original plain text. */
   fun pkcDecrypt (input : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      Log.debug( "PKC decrypting.." )
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
            Log.err( "PKC decryption failed!" )
            ret = ByteArray( 0 )
         }
      }
      else
         Log.err( "PKC decryption failed! (invalid keys/input)" )

      return ret
   }

   /** The key size requiered by the ed25519 signing algorithm. */
   fun publicSigningKeySize () = (sodium.crypto_sign_ed25519_publickeybytes() as Number).toInt()
   /** The salt size requiered in the password based key derivation. */
   fun saltSize () = sodium.crypto_pwhash_saltbytes()

   /** generates a symetric key from a password and salt */
   private fun symetricKey (password : ByteArray, salt : ByteArray) : ByteArray
   {
      val symKeySize = (sodium.crypto_secretbox_keybytes() as Number).toInt()
      var ret = ByteArray( symKeySize )

      if (sodium.crypto_pwhash( ret, symKeySize.toLong(), password, password.size.toLong(), salt, sodium.crypto_pwhash_opslimit_moderate(),
      sodium.crypto_pwhash_memlimit_moderate(), sodium.crypto_pwhash_alg_argon2id13() ) != 0)
         Log.warn( "Key generation failed!" )

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
            Log.warn( "Keypair generation failed!" )
      }
      else
         Log.warn( "Seeding failed!" )

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
            Log.warn( "Keypair generation failed!" )
      }
      else
         Log.warn( "Seeding failed!" )

      return ret
   }

   /** Intiates the sodium library object. Asks the user for a password, and uses this in the supplied key function
     * @param passwordProvider The PasswordProvider to use to get a password from a user.
     * @param salt Salt to use in key function. */
   fun getKey (passwordProvider : PasswordProvider, salt : ByteArray, keyFunction : (ByteArray, ByteArray) -> ByteArray) : ByteArray
   {
      var ret = ByteArray( 0 )

      if (passwordProvider is NoPasswordProvider)
         Log.warn( "Password input needed" )
      else
         if (validateSaltSize( salt ))
         {
            val password = passwordProvider.password().toByteArray()

            if (password.size == 0)
               Log.warn( "Invalid password" )
            else
            {
               sodium.sodium_init()
               ret = keyFunction( password, salt )
            }
         }
         else
            Log.warn( "Invalid salt" )

      return ret
   }

   private fun convertPK (publicSigningKey : ByteArray) : ByteArray
   {
      var ret = ByteArray( (sodium.crypto_box_publickeybytes() as Number).toInt() )

      if (sodium.crypto_sign_ed25519_pk_to_curve25519( ret, publicSigningKey ) != 0)
         Log.warn( "Key conversion failed!" )

      return ret
   }

   private fun convertSK (secretSigningKey : ByteArray) : ByteArray
   {
      var ret = ByteArray( (sodium.crypto_box_secretkeybytes() as Number).toInt() )

      if (sodium.crypto_sign_ed25519_sk_to_curve25519( ret, secretSigningKey ) != 0)
         Log.warn( "Key conversion failed!" )

      return ret
   }

   private fun validateKeySize (key : ByteArray) : Boolean
   {
      var ret = false
      if (key.size == (sodium.crypto_secretbox_keybytes() as Number).toInt())
         ret = true
      else
         Log.warn( "Invalid key!" )

      return ret
   }

   private fun validatePKSize (pk : ByteArray) : Boolean
   {
      var ret = false
      if (pk.size == publicSigningKeySize())
         ret = true
      else
         Log.warn( "Invalid public key!" )

      return ret
   }

   private fun validateSKSize (sk : ByteArray) : Boolean
   {
      var ret = false
      if (sk.size == secretSigningKeySize())
         ret = true
      else
         Log.warn( "Invalid secret key!" )

      return ret
   }

   private fun validateSaltSize (salt : ByteArray) : Boolean
   {
      var ret = false
      if (salt.size == saltSize())
         ret = true
      else
         Log.warn( "Invalid salt!" )

      return ret
   }

   private fun secretSigningKeySize () = (sodium.crypto_sign_ed25519_secretkeybytes() as Number).toInt()
}
