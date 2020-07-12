package pen

expect object LogManager
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
         LogManager.logMessage( prependix() + message, severity )
   }

   /** @param trigger At what level to trigger the actual logging. The value could for example be read from a config file. */
   fun log (message : () -> String, trigger : LogLevel = LogLevel.WARN, severity : LogLevel = LogLevel.DEBUG)
   {
      if (trigger >= severity)
         LogManager.logMessage( prependix() + message(), severity )
   }

   fun log (trigger : LogLevel)
   {
      if (trigger >= LogLevel.DEBUG)
         LogManager.logMessage( tag(), LogLevel.DEBUG )
   }

   private fun prependix () = if (tag() == "") "" else tag() + "- "
}

/** Simple logging. Implementing the Loggable interface also gives access to trigged logging. */
object Log : Loggable
{
   /** Global log level. If not explicitly set warnings and errors will be logged.  */
   var level : LogLevel = LogLevel.UNSET

   private fun logMessage (message : String, severity : LogLevel)
   {
      if (level == LogLevel.UNSET)
      {
         if (severity <= LogLevel.WARN )
            LogManager.logMessage( message, severity )
      }
      else
         LogManager.logMessage( message, severity )
   }

   fun debug (message : String) = logMessage( message, LogLevel.DEBUG )
   fun info (message : String) = logMessage( message, LogLevel.INFO )
   fun warn (message : String) = logMessage( message, LogLevel.WARN )
   fun error (message : String) = logMessage( message, LogLevel.ERROR )
   fun critical (message : String) = logMessage( message, LogLevel.CRITICAL )

   override fun tag () = ""
}
