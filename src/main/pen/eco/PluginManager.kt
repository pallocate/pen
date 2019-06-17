package pen.eco

import pen.eco.LogLevel.INFO
import pen.eco.LogLevel.WARN
import pen.eco.LogLevel.ERROR
import pen.eco.types.Plugin
import pen.eco.types.KPluginInfo
import pen.eco.types.NoPlugin

interface PluginManager
{ val plugins: Map<String, Plugin> }
class NoPluginManager : PluginManager
{ override val plugins = HashMap<String, Plugin>() }

class KPluginManager (supportedPlugins : Map<String, String>, vararg requestedPlugins : KPluginInfo) : PluginManager, Loggable
{
   /** Loaded plugins */
   override val plugins : Map<String, Plugin>

   /** Initializes the plugin manager, loads plugins and their dependencies.
    *  @param supportedPlugins Maps plugin names to their class names. */
   init
   {
      log("initializing", Config.flag( "PLUGIN_MANAGER" ), INFO)
      plugins = HashMap<String, Plugin>()                                       // Plugin names mapped to Plugins

      for (requestedPlugin in requestedPlugins)
         loadPlugin( supportedPlugins, requestedPlugin, plugins, 0 )
   }

   private fun loadPlugin (supportedPlugins : Map<String, String>, requestedPlugInfo : KPluginInfo,
      resultMap : HashMap<String, Plugin>, recursiveDepth : Int) : Plugin
   {
      var ret : Plugin = NoPlugin()

      /* Only permitting 10 dependency levels(to avoid dependency cycles). */
      if (recursiveDepth <= 10)
      {
         val requstedName = requestedPlugInfo.name
         val className = supportedPlugins.get( requstedName )                   // Lookup the plugin class name

         if (className == null)
            log( "plugin not supported \"${requestedPlugInfo}\"", true, ERROR )
         else
         {
            try
            {
               val plugin = Class.forName( className!! ).newInstance()          // Try to create a plugin from the class name
               if (plugin is Plugin)
               {
                  /* Check that the version of the plugin instance is acceptable. */
                  if (plugin.info.version >= requestedPlugInfo.version && (plugin.info.version < requestedPlugInfo.max_version || requestedPlugInfo.max_version == 0F))
                  {
                     val resultMapFinding = resultMap.get( requstedName )       // Check if the result map already contains a similar plugin
                     if (resultMapFinding == null)
                     {
                        resultMap.put( requstedName, plugin )                   // Add the plugin
                        log( "plugin \"${requstedName}\" loaded", Config.flag( "PLUGIN_MANAGER" ) )

                        /* Handle dependencies and initialize the plugin */
                        val nrOfDepends = plugin.dependencies.size
                        val initArray = Array<Plugin>( nrOfDepends, {NoPlugin()} )

                        for (i in 0 until nrOfDepends)                          // Process dependencies recursively
                           initArray[i] = loadPlugin( supportedPlugins, plugin.dependencies[i], resultMap, recursiveDepth + 1 )

                        plugin.initialize( initArray )                          // Initialize the plugin with the requested dependencies
                        ret = plugin
                     }
                     else
                        ret = resultMapFinding
                  }
                  else
                     log("plugin load failed(unsupported version)! expected: ${requestedPlugInfo}, found: ${plugin.info}", true, ERROR )
               }
            }
            catch (t : Throwable)
            { log( "plugin load failed \"${requstedName}\" $t", true, ERROR ) }
         }
      }
      else
         log( "dependency cycle detected!", Config.flag( "PLUGIN_MANAGER" ), WARN )

      return ret
   }

   override fun originName () = "KPluginManager"
}
