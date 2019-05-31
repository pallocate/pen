package pen.eco

import java.io.File
import java.lang.System.getProperty

interface Configuration

/** Some global constants and dynamically loadable settings. */
object Config : Configuration
{
   /** Common specification version. */
   const val VERSION                              = 1
   /** Size of hash signature in bytes. */
   const val SIGN_BYTES                           = 64
   /** Size of block hash in bytes. */
   const val HASH_BYTES                           = 32

   /* Definition of some basic directories and files */
   /** OS specific directory separation sign. */
   val SLASH                                      = File.separator
   /** User home directory. */
   val USER_HOME                                  = getProperty( "user.home" )
   /** Directory where to save configurations. */
   val CONFIG_DIR                                 = ".pen"
   /** Directory of the libsodium library. */
   val LIBSODIUM_DIR                              = "dist${SLASH}lib${SLASH}isc"
   /** Name of the log file. */
   val LOG_FILE                                   = "app.log"

   /** Settinig are loaded from file if possible. */
   private var settings : Settings                = NoSettings()

   /** Returns settings possibly loading them from file. */
   fun getSettings () : KSettings
   {
      if (settings is NoSettings)
         settings = KSettings.loadFromFile( "settings.json" )

      return settings as KSettings
   }
}
