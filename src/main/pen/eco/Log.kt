package pen.eco

expect fun log (message : String, severity : LogLevel = LogLevel.INFO)

enum class LogLevel { DEBUG, INFO, WARN, ERROR, CRITICAL, QUIET }

/** Brings conditionallity to the logging. */
interface Loggable
{
   /** A name that identifies the origin of the log message. */
   abstract fun originName () : String

   /** @param execute Enables conditional logging, like if some flag is set */
   fun log (message : String, execute : Boolean = true, severity : LogLevel = LogLevel.DEBUG)
   {
      if (execute)
         log( originName() + "- " + message, severity )
   }
   fun log (message : () -> String, execute : Boolean = true, severity : LogLevel = LogLevel.DEBUG)
   {
      if (execute)
         log( originName() + "- " + message(), severity )
   }
}

/** A simple logger. */
object Log
{
   /** At what minimum level to log events. */
   var level = LogLevel.WARN

   fun debug (messageFunction : () -> String, execute : Boolean = true)
   {
      if (execute)
         log( messageFunction(), LogLevel.DEBUG )
   }
   fun info (messageFunction : () -> String, execute : Boolean = true)
   {
      if (execute)
         log( messageFunction(), LogLevel.INFO )
   }
   fun warn (messageFunction : () -> String, execute : Boolean = true)
   {
      if (execute)
         log( messageFunction(), LogLevel.WARN )
   }

   fun debug (msg : String) = log( msg, LogLevel.DEBUG )
   fun info (msg : String) = log( msg, LogLevel.INFO )
   fun warn (msg : String) = log( msg, LogLevel.WARN )
   fun err (msg : String) = log( msg, LogLevel.ERROR )
   fun critical (msg : String) = log( msg, LogLevel.CRITICAL )
}
