package pen

object Constants
{
   /** Common specification version. */
   const val VERSION                              = 1
   /** Size of block hash in bytes. */
   const val HASH_BYTES                           = 32

   /** OS specific directory separation sign. */
   val SLASH                                      = slash()
   /** Directory where to save configurations. */
   val JSON_EXTENSION                             = ".json"
   /** Name of the log file. */
   val LOG_FILE                                   = "app.log"

   /** Size of salt used by SCrypt password hashing function. */
   val SALT_SIZE = 32
   /** Seed size for the Ed25519Sha3 function. */
   val SEED_SIZE = 32
   /** Public key size for the Ed25519Sha3 function. */
   val PUBLIC_KEY_SIZE = 32

   /** Nr. of bits in the AES key. */
   val AES_KEY_BITS = 256
   /** Password hashing function (used by AES). */
   val PKBD_ALGORITHM = "PBKDF2WithHmacSHA1"

   val AES_NONCE_SIZE = 16
}
