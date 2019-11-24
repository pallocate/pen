import pen.eco.*

fun main ()
{
   println( "PLUGIN_MANAGER: " + Config.trigger( "PLUGIN_MANAGER" ))
   Log.info( "Log functionallity test" )
   val bar = ByteArray(7, { 0xFA.toByte() })
   println( bar.toHex() )
}
