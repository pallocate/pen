package pen.eco

expect fun loadConf (filename : String) : Map<String, String>

/** Some loadable settings. */
object Config
{
   val supportedPlugins : Map<String, String> = loadConf( "plugins.conf" )
   private val logValues : Map<String, String> = loadConf( "log.conf" )

   fun trigger (name : String) : LogLevel
   {
      var triggerValue = LogLevel.INFO

      val getResult = logValues.get( name )
      if (getResult != null)
      {
         val intValue = Utils.stringToInt( getResult, LogLevel.QUIET.ordinal, LogLevel.DEBUG.ordinal, true )
         triggerValue = enumValues<LogLevel>()[intValue]
      }

      return triggerValue
   }
}
