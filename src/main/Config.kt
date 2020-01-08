package pen

expect fun loadConf (filename : String) : Map<String, String>

/** Coniguration settings loaded from file. */
object Config
{
   val supportedPlugins : Map<String, String> = loadConf( "plugins.conf" )
   private val logValues : Map<String, String> = loadConf( "log.conf" )

   fun trigger (name : String) : LogLevel
   {
      var triggerValue = LogLevel.INFO

      logValues.get( name )?.run {
         val intValue = toInt( LogLevel.QUIET.ordinal, LogLevel.DEBUG.ordinal, true )
         triggerValue = enumValues<LogLevel>()[intValue]
      }

      return triggerValue
   }
}
