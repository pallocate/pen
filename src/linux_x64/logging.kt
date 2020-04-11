package pen

import platform.posix.fdopen
import platform.posix.fprintf
import platform.posix.fflush

/** Skeleton code for web assembly. */
actual object Logger
{
   val STDERR = fdopen( 2, "w" )

   actual fun logMessage (message : String, severity : LogLevel)
   {
      if (severity <= Log.level && severity > LogLevel.QUIET)
         printErr( severity.name  + ": " + message )
   }

   fun printErr (message : String)
   {
      fprintf( STDERR, message + "\n" )
      fflush( STDERR )
   }
}
