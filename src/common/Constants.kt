package pen

object Constants
{
   /** Common specification version. */
   const val VERSION                              = 1
   /** Size of block hash in bytes. */
   const val HASH_BYTES                           = 32

   /* Definition of some basic directories and files */
   /** OS specific directory separation sign. */
   val SLASH                                      = slash()
   /** Directory where to save configurations. */
   val CONFIG_DIR                                 = ".pen"
   /** Directory where to save configurations. */
   val JSON_EXTENSION                             = ".json"
   /** Directory of the libsodium library. */
   val LIBSODIUM_DIR                              = "dist${SLASH}lib${SLASH}isc"
   /** Directory where application icons are stored. */
   val ICONS_DIR                                  = "dist${SLASH}resources${SLASH}icons"
   /** Name of the log file. */
   val LOG_FILE                                   = "app.log"
}
