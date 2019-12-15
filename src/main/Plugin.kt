package pen

data class KPluginInfo (val name : String = "", val version : Float = 0f, val max_version : Float = 0F) {}

interface Plugin
{
   val info : KPluginInfo
   val dependencies : Array<KPluginInfo>

   fun initialize (plugins : Array<Plugin>)
}

class NoPlugin : Plugin
{
   override val info = KPluginInfo( "", 0F )
   override val dependencies = Array<KPluginInfo>( 0, {KPluginInfo()} )
   override fun initialize (plugins : Array<Plugin>) {}
}
