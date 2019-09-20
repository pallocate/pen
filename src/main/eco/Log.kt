package pen.eco

expect fun log (message : String, severity : LogLevel = LogLevel.INFO)

enum class LogLevel { DEBUG, INFO, WARN, ERROR, CRITICAL, QUIET }

/** Makes logging a little smoother. */
interface Loggable
{
   /** A name that identifies the origin of the log message. */
   abstract fun originName () : String

   /** @param confirmation Confirms conditional logging. */
   fun log (message : String, confirmation : Boolean = true, severity : LogLevel = LogLevel.DEBUG)
   {
      if (confirmation)
         log( originName() + "- " + message, severity )
   }
   /** @param confirmation Confirms conditional logging. */
   fun log (message : () -> String, confirmation : Boolean = true, severity : LogLevel = LogLevel.DEBUG)
   {
      if (confirmation)
         log( originName() + "- " + message(), severity )
   }
}

/** A simple logger. */
object Log
{
   /** At what minimum level to log events. */
   var level = LogLevel.DEBUG

   /** @param confirmation Confirms conditional logging. */
   fun debug (messageFunction : () -> String, confirmation : Boolean = true)
   {
      if (confirmation)
         log( messageFunction(), LogLevel.DEBUG )
   }
   /** @param confirmation Confirms conditional logging. */
   fun info (messageFunction : () -> String, confirmation : Boolean = true)
   {
      if (confirmation)
         log( messageFunction(), LogLevel.INFO )
   }
   /** @param confirmation Confirms conditional logging. */
   fun warn (messageFunction : () -> String, confirmation : Boolean = true)
   {
      if (confirmation)
         log( messageFunction(), LogLevel.WARN )
   }

   fun debug (msg : String) = log( msg, LogLevel.DEBUG )
   fun info (msg : String) = log( msg, LogLevel.INFO )
   fun warn (msg : String) = log( msg, LogLevel.WARN )
   fun err (msg : String) = log( msg, LogLevel.ERROR )
   fun critical (msg : String) = log( msg, LogLevel.CRITICAL )
}
