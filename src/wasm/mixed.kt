package pen

import pen.Plugin
import pen.NoPlugin

/* Stub code for web assembly. */
actual fun user_home () = "/home/user_name"
actual fun slash () = "/"

actual fun encode_b64 (bytes : ByteArray) = ByteArray( 0 )
actual fun decode_b64 (encoded : ByteArray) = ByteArray( 0 )
actual fun hash_md5 (bytes : ByteArray) = ByteArray( 0 )
actual fun createDir (path : String) {}
actual fun now () = 0L
actual fun pluginInstance (className : String) : Plugin = NoPlugin()

actual fun loadConf (filename : String) : Map<String, String>
{
   val ret = HashMap<String, String>()

   ret.put( "PLATFORM", "true" )
   ret.put( "PLUGIN_MANAGER", "true" )

   return ret
}
