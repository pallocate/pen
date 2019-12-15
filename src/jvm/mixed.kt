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
actual fun user_home () = System.getProperty( "user.home" )
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
               Log.error( "plugin instanciation failed \"${className}\"" )
               NoPlugin()
            }
}

actual fun createDir (path : String)
{
   Log.debug( "creating directory $path" )

   try
   { (File( path )).mkdirs() }
   catch (e : Exception)
   {Log.warn( "directory create failed" )}
}

actual fun loadConf (filename : String) : Map<String, String>
{
   val properties = Properties()
   val resultMap = HashMap<String, String>()

   try
   {
      /* Setting some default log levels. */
      resultMap.put( "PLATFORM", "5" )
      resultMap.put( "PLACE_HOLDER", "5" )
      resultMap.put( "FILES", "5" )

      properties.load(FileReader( filename ))

      for (key in properties.keys())
         resultMap.put(key as String, properties.get( key ) as String)
   }
   catch (e : Exception)
   { Log.warn( "Conf-file load failed!" ) }

   return resultMap
}
