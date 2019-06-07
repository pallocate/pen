package pen.eco

object Constants
{
   /** Common specification version. */
   const val VERSION                              = 1
   /** Size of hash signature in bytes. */
   const val SIGN_BYTES                           = 64
   /** Size of block hash in bytes. */
   const val HASH_BYTES                           = 32
   /** Size of public key. */
   const val PUBKEY_BYTES                         = 32

   /* Definition of some basic directories and files */
   /** OS specific directory separation sign. */
   val SLASH                                      = slash()
   /** User home directory. */
   val USER_HOME                                  = user_home()
   /** Directory where to save configurations. */
   val CONFIG_DIR                                 = ".pen"
   /** Directory of the libsodium library. */
   val LIBSODIUM_DIR                              = "dist${SLASH}lib${SLASH}isc"
   /** Name of the log file. */
   val LOG_FILE                                   = "app.log"

   /* Block flags */
   /** If it is a production block, otherwise consumption. */
   const val IS_PRODUCTION                        = 1
   /** If it is a proposal (it only has Product children). */
   const val IS_PROPOSAL                          = 2
   /** If the proposal contains sensetive information. */
   const val HAS_SENSETIVE_DATA                   = 4
   //const val IS_ADJUSTMENT                        = 8
}
