package pen.eco

expect fun log (message : String, severity : LogLevel = LogLevel.INFO)


enum class LogLevel { QUIET, CRITICAL, ERROR, WARN, INFO, DEBUG }

/** Trigged logging. */
interface Loggable
{
   /** A name that identifies the origin of the log message. */
   abstract fun originName () : String

   fun log (message : String, trigger : LogLevel = LogLevel.ERROR, logLevel : LogLevel = LogLevel.DEBUG)
   {
      if (trigger >= logLevel)
         log( originName() + "- " + message, logLevel )
   }

   fun log (message : () -> String, trigger : LogLevel = LogLevel.ERROR, logLevel : LogLevel = LogLevel.DEBUG)
   {
      if (trigger >= logLevel)
         log( originName() + "- " + message(), logLevel )
   }
}

/** Simple logging. */
object Log
{
   var level = LogLevel.WARN

   fun debug (msg : String) = log( msg, LogLevel.DEBUG )
   fun info (msg : String) = log( msg, LogLevel.INFO )
   fun warn (msg : String) = log( msg, LogLevel.WARN )
   fun err (msg : String) = log( msg, LogLevel.ERROR )
   fun critical (msg : String) = log( msg, LogLevel.CRITICAL )
}

