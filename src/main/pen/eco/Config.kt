package pen.eco

expect fun user_home () : String
expect fun slash () : String

/** Some global constants and dynamically loadable settings. */
object Config
{
   /** Settinig are loaded from file if possible. */
   private var settings : Settings                = NoSettings()

   /** Returns settings possibly loading them from file. */
   fun getSettings () : KSettings
   {
      if (settings is NoSettings)
         settings = KSettings.loadFromFile( "settings.json" )

      return settings as KSettings
   }
}
