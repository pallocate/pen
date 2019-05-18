package pen.eco.common

import java.io.File

/** A set of Kademlia configuration parameters. Default values are
  * supplied and can be changed by the application as necessary */
object Config
{
   /** Common specification version. */
   const val VERSION                              = 1
   /** Size of hash signature in bytes. */
   const val SIGN_BYTES                           = 64
   /** Size of block hash in bytes. */
   const val HASH_BYTES                           = 32

   /* Files and directories */
   /** Directory where to save stuff. */
   const val LOCAL_FOLDER                         = ".kademlia"
   /** Name of the log file. */
   const val LOG_FILE                             = "app.log"
   /** Directory of the libsodium library. */
   const val LIBSODIUM_DIR                        = "dist/lib/isc/"

   private var settings : Settings                = NoSettings()

   fun getSettings () : KSettings
   {
      if (settings is NoSettings)
         settings = KSettings.loadFromFile( "settings.json" )

      return settings as KSettings
   }

   /** @return The pathname to the storage directory of the owner. */
   fun nodeDir (ownerName : String) : String
   {
      /* Creating the storage directory. */
      val path = System.getProperty( "user.home" ) + File.separator + LOCAL_FOLDER
      val dir = File( path )
      if (!dir.isDirectory())
         dir.mkdir()

      /* Creating owner subfolder. */
      val ownerDirectory = File( dir.toString() + File.separator + ownerName )
      if (!ownerDirectory.isDirectory())
         ownerDirectory.mkdir()

      return ownerDirectory.toString()
   }
}
