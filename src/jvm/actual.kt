package pen

import java.util.Properties
import java.io.File
import java.io.FileReader
import java.security.SecureRandom
import java.time.Clock
import java.time.Instant
import org.apache.commons.codec.binary.Hex

actual fun slash () = File.separator
actual fun now () : Long
{
   val clock = Clock.systemUTC()
   return Instant.now( clock ).toEpochMilli()
}

actual fun ByteArray.toHex () : String = Hex.encodeHexString( this )

actual fun randomBytes (size : Int) = ByteArray( size ).also { SecureRandom().nextBytes(it) }

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
