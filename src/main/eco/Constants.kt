package pen.eco

expect fun user_home () : String
expect fun slash () : String

object Constants
{
   /** Common specification version. */
   const val VERSION                              = 1
   /** Size of block hash in bytes. */
   const val HASH_BYTES                           = 32

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

   /* Proposal flags */
   /** If it is a production block, otherwise consumption. */
   const val IS_PRODUCTION                        = 1
   /** If the proposal contains sensetive information. */
   const val HAS_SENSETIVE_DATA                   = 2
   //const val IS_ADJUSTMENT                        = 8
}
