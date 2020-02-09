package pen

expect object Logger
{
   fun logMessage (message : String, severity : LogLevel)
}

enum class LogLevel { QUIET, CRITICAL, ERROR, WARN, INFO, DEBUG }

/** Trigged logging. */
interface Loggable : Tagged
{
   fun log (message : String, trigger : LogLevel = LogLevel.WARN, logLevel : LogLevel = LogLevel.DEBUG)
   {
      if (trigger >= logLevel)
         Logger.logMessage( tag() + "- " + message, logLevel )
   }

   fun log (message : () -> String, trigger : LogLevel = LogLevel.WARN, logLevel : LogLevel = LogLevel.DEBUG)
   {
      if (trigger >= logLevel)
         Logger.logMessage( tag() + "- " + message(), logLevel )
   }
}

/** Simple logging. */
object Log
{
   /** Logging threshold */
   var level = LogLevel.WARN

   fun debug (msg : String) = Logger.logMessage( msg, LogLevel.DEBUG )
   fun info (msg : String) = Logger.logMessage( msg, LogLevel.INFO )
   fun warn (msg : String) = Logger.logMessage( msg, LogLevel.WARN )
   fun error (msg : String) = Logger.logMessage( msg, LogLevel.ERROR )
   fun critical (msg : String) = Logger.logMessage( msg, LogLevel.CRITICAL )
}

