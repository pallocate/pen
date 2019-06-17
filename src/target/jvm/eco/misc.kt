package pen.eco

import java.lang.System
import java.io.File
import java.io.FileReader
import java.util.Properties
import java.util.Base64
import java.security.MessageDigest
import pen.eco.Log
import pen.eco.Config

actual fun slash () = File.separator
actual fun user_home () = System.getProperty( "user.home" )
actual fun encode_b64 (bytes : ByteArray) = Base64.getEncoder().encode( bytes )
actual fun decode_b64 (encoded : String) = Base64.getDecoder().decode( encoded.toByteArray() )
actual fun hash_md5 (bytes : ByteArray) = MessageDigest.getInstance( "MD5" ).digest( bytes )

actual fun create_dir (path : String)
{
   Log.debug( {"Creating directory $path"}, Config.flag( "COMMON_FILES" ))

   try
   { (File( path )).mkdirs() }
   catch (e : Exception)
   {Log.warn( "Directory create failed" )}
}

actual fun loadConf (filename : String) : Map<String, String>
{
   val properties = Properties()
   val resultMap = HashMap<String, String>()

   try
   {
      resultMap.put( "COMMON_PLACE_HOLDER", "true" )
      resultMap.put( "COMMON_FILES", "true" )

      properties.load(FileReader( filename ))

      for (key in properties.keys())
         resultMap.put(key as String, properties.get( key ) as String)
   }
   catch (e : Exception) {}

   return resultMap
}
