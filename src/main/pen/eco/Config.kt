package pen.eco

expect fun loadConf (filename : String) : Map<String, String>

/** Some loadable settings. */
object Config
{
   val supportedPlugins : Map<String, String> = loadConf( "plugins.conf" )
   private val debugFlags : Map<String, String> = loadConf( "debug.conf" )

   fun flag (name : String) : Boolean
   {
      var status = false

      val getResult = debugFlags.get( name )
      if (getResult != null)
         status = getResult.equals( "true" )

      return status
   }
}
