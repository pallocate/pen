package pen.eco

import pen.eco.Loggable
import pen.eco.types.PasswordProvider

/** Cryptographic functionallity, using the Sodium library. */
expect object Crypto : Loggable
{
   /** Generates high entropy pseudo random bytes.
     * @param number The number of bytes to be generated.
     * @return The output bytes. */
   fun randomBytes (number : Int) : ByteArray

   /** Does a digest of input.
     * Algorithm: BLAKE2b
     * @return The hash. */
   fun digest (input : ByteArray) : ByteArray

   /** Signs a text with a base64 encoded signature at the end.
     * Algorithm: Ed25519
     * @param text The text to be signed.
     * @return The text with an added signature. */
   fun signText (text : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : ByteArray

   /** Verifies text using signature message from the end, which is then stripped off.
     * Algorithm: Ed25519
     * @param signedText The signed text to be verified.
     * @param othersPublicKey The senders public key.
     * @return The original text. */
   fun verifyText (signedText : ByteArray, othersPublicKey : ByteArray) : ByteArray

   /** Returns only the signature of the input.
     * Algorithm: Ed25519
     * @param input The data to calculate signature from.
     * @return The signature. */
   fun signatureOf (input : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : ByteArray

   /** Verifies some signed data.
     * Algorithm: Ed25519
     * @param input The data that was signed.
     * @param signature The signature of the data.
     * @param othersPublicKey The signers public key.
     * @return True if signature matches. */
   fun verifySignatureOf (input : ByteArray, signature : ByteArray, othersPublicKey : ByteArray) : Boolean

   /** Signs a binary message.
     * Algorithm: Ed25519
     * @param binary The binary to be signed.
     * @return The signed binary. */
   fun signBinary (binary : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : ByteArray

   /** Verifies a signed binary message.
     * Algorithm: Ed25519
     * @param signedBinary The signed message.
     * @param othersPublicKey The senders public key.
     * @return The original binary. */
   fun verifyBinary (signedBinary : ByteArray, othersPublicKey : ByteArray) : ByteArray

   /** Performes a symetric encryption of the plain text, and puts the used nonce in the beginning.
     * Algorithm: XSalsa20, Poly1305 MAC
     * @param plainText The plain text to be encrypted.
     * @return The nonce plus the cipher text. */
   fun encrypt (plainText : ByteArray, passwordProvider : PasswordProvider, skc_salt : ByteArray) : ByteArray

   /** Does a symetric decryption of the input.
     * Algorithm: XSalsa20, Poly1305 MAC
     * @param input Input made up by nonce plus cipher text.
     * @return The original plain text. */
   fun decrypt (input : ByteArray, passwordProvider : PasswordProvider, skc_salt : ByteArray) : ByteArray

   /** Encrypts plain text using public key cryptography.
     * Algorithm: X25519, XSalsa20, Poly1305 MAC
     * @param plainText The plain text to be encrypted.
     * @param othersPublicKey The recievers public key.
     * @return The nonce plus the cipher text. */
   fun pkcEncrypt (plainText : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray, othersPublicKey : ByteArray) : ByteArray

   /** Decrypts input using public key cryptography.
     * Algorithm: X25519, XSalsa20, Poly1305 MAC
     * @param input Input made up by nonce plus cipher text.
     * @param othersPublicKey The senders public key.
     * @return The original plain text. */
   fun pkcDecrypt (input : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray, othersPublicKey : ByteArray) : ByteArray

   /** The key size requiered by the ed25519 signing algorithm. */
   fun publicSigningKeySize () : Int
   /** The salt size requiered in the password based key derivation. */
   fun saltSize () : Int

   /** generates a public key from a password and salt */
   fun publicKey (password : ByteArray, salt : ByteArray) : ByteArray

   /** Intiates the sodium library object. Gets a password from the provider,
     * and uses this in the supplied key function.
     * @param passwordProvider The PasswordProvider to use.
     * @param salt Salt to use in key function. */
   fun getKey (passwordProvider : PasswordProvider, salt : ByteArray, keyFunction : (ByteArray, ByteArray) -> ByteArray) : ByteArray
}
