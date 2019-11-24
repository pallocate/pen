package pen.eco

expect fun loadConf (filename : String) : Map<String, String>

/** Some loadable settings. */
object Config
{
   val supportedPlugins : Map<String, String> = loadConf( "plugins.conf" )
   private val logValues : Map<String, String> = loadConf( "log.conf" )

   fun trigger (name : String) : LogLevel
   {
      var trigValue = LogLevel.INFO

      val getResult = logValues.get( name )
      if (getResult != null)
      {
         val trig = Utils.stringToInt( getResult, LogLevel.QUIET.ordinal, LogLevel.DEBUG.ordinal, true )
         trigValue = enumValues<LogLevel>()[trig]
      }

      return trigValue
   }
}
