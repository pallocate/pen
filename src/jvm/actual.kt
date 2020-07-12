package pen

import java.lang.System
import java.lang.Math
import java.util.Properties
import java.util.Base64
import java.io.File
import java.io.FileReader
import java.security.MessageDigest
import java.time.Clock
import java.time.Instant
import pen.Plugin
import pen.NoPlugin

actual fun slash () = File.separator
actual fun encode_b64 (bytes : ByteArray) = Base64.getEncoder().encode( bytes )
actual fun decode_b64 (encoded : ByteArray) = Base64.getDecoder().decode( encoded )
actual fun hash_md5 (bytes : ByteArray) = MessageDigest.getInstance( "MD5" ).digest( bytes )

actual fun now () : Long
{
   val clock = Clock.systemUTC()
   return Instant.now( clock ).getEpochSecond()
}

actual fun pluginInstance (className : String) : Plugin
{
   var tmp : Any = NoPlugin()

   try
   { tmp = Class.forName( className ).newInstance() }
   catch (e : Exception) {}

   return   if (tmp is Plugin)
               tmp
            else
            {
               Log.log("plugin instanciation failed \"${className}\"", Config.trigger( "PLUGINS" ), LogLevel.ERROR)
               NoPlugin()
            }
}

actual fun createDir (path : String)
{
   Log.log("creating directory $path", Config.trigger( "FILES" ))

   try
   { (File( path )).mkdirs() }
   catch (e : Exception)
   {Log.log("directory create failed", Config.trigger( "FILES" ), LogLevel.WARN)}
}

actual fun loadConf (filename : String) : Map<String, String>
{
   Log.debug( "loding config file \"$filename\"" )
   val properties = Properties()
   val resultMap = HashMap<String, String>()

   try
   {
      properties.load(FileReader( filename ))

      for (key in properties.keys())
         resultMap.put(key as String, properties.get( key ) as String)
   }
   catch (e : Exception)
   {Log.warn( "load config failed!" )}

   return resultMap
}
