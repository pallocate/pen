package pen

/** Skeleton code for web assembly. */
actual object Logger
{
   actual fun logMessage (message : String, severity : LogLevel)
   {
      if (severity <= Log.level && severity > LogLevel.QUIET)
         println( severity.name  + ": " + message )
   }
}
