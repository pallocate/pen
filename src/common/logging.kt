package pen

expect object Logger
{
   fun logMessage (message : String, severity : LogLevel)
}

enum class LogLevel { QUIET, CRITICAL, ERROR, WARN, INFO, DEBUG, UNSET }

/** Trigged and tagged logging. */
interface Loggable : Tagged
{
   /** @param trigger At what level to trigger the actual logging. The value could for example be read from a config file. */
   fun log (message : String, trigger : LogLevel = LogLevel.WARN, severity : LogLevel = LogLevel.DEBUG)
   {
      if (trigger >= severity)
         Logger.logMessage( tag() + "- " + message, severity )
   }

   /** @param trigger At what level to trigger the actual logging. The value could for example be read from a config file. */
   fun log (message : () -> String, trigger : LogLevel = LogLevel.WARN, severity : LogLevel = LogLevel.DEBUG)
   {
      if (trigger >= severity)
         Logger.logMessage( tag() + "- " + message(), severity )
   }
}

/** Simple logging. */
object Log
{
   /** A Global log level */
   var level : LogLevel = LogLevel.UNSET

   private fun doLog (message : String, severity : LogLevel)
   {
      if (level == LogLevel.UNSET)
         if (severity <= LogLevel.WARN )
            Logger.logMessage( message, severity )
      else
         Logger.logMessage( message, severity )
   }
   fun debug (message : String) = doLog( message, LogLevel.DEBUG )
   fun info (message : String) = doLog( message, LogLevel.INFO )
   fun warn (message : String) = doLog( message, LogLevel.WARN )
   fun error (message : String) = doLog( message, LogLevel.ERROR )
   fun critical (message : String) = doLog( message, LogLevel.CRITICAL )
}

