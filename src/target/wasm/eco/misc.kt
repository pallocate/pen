package pen.eco

import pen.eco.types.Plugin
import pen.eco.types.NoPlugin

/** Skeleton code for web assembly. */
actual fun user_home () = "/home/user_name"
actual fun slash () = "/"

actual fun encode_b64 (bytes : ByteArray) = ByteArray( 0 )
actual fun decode_b64 (encoded : ByteArray) = ByteArray( 0 )
actual fun hash_md5 (bytes : ByteArray) = ByteArray( 0 )
actual fun create_dir (path : String) {}
actual fun plugin_instance (className : String) : Plugin = NoPlugin()

actual fun loadConf (filename : String) : Map<String, String>
{
   val ret = HashMap<String, String>()

   ret.put( "PLATFORM", "true" )
   ret.put( "PLUGIN_MANAGER", "true" )

   return ret
}
