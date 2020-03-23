package pen

import pen.Plugin
import pen.NoPlugin

/** Using javascript Date object to get epoch seconds. */
@SymbolName( "jsNow" )
external fun jsNow () : Int
actual fun now () = jsNow().toLong()

actual fun slash () = "/"

/* Stubs */
actual fun hash_md5 (bytes : ByteArray) = ByteArray( 0 )
actual fun pluginInstance (className : String) : Plugin = NoPlugin()
actual fun loadConf (filename : String) : Map<String, String>
{
   val ret = HashMap<String, String>()

   ret.put( "PLATFORM", "5" )
   ret.put( "MERKLE_TREE", "5" )

   return ret
}

actual fun createDir (path : String) {}
