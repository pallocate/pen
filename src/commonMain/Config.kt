package pen

expect fun loadConf (filename : String) : Map<String, String>

/** Coniguration settings loaded from file. */
object Config
{
   val logValues : Map<String, String> = loadConf( "log.conf" )
   val supportedPlugins : Map<String, String> = loadConf( "plugins.conf" )

   fun trigger (name : String) : LogLevel
   {
      var enumValue = LogLevel.INFO
      val logValue = logValues.get( name )

      if (logValue != null)
      {
         val intValue = logValue.toInt( LogLevel.QUIET.ordinal, LogLevel.DEBUG.ordinal, true )
         enumValue = enumValues<LogLevel>()[intValue]
      }

      return enumValue
   }
}
