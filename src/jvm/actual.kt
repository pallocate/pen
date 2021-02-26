package pen

import java.util.Properties
import java.util.Base64
import java.io.File
import java.io.FileReader
import java.nio.file.Paths
import java.security.SecureRandom
import java.time.Clock
import java.time.Instant
import org.apache.commons.codec.binary.Hex

actual fun slash () = File.separator
actual fun encode_b64 (bytes : ByteArray) = Base64.getEncoder().encode( bytes )
actual fun decode_b64 (encoded : ByteArray) = Base64.getDecoder().decode( encoded )
actual fun now () = Instant.now( Clock.systemUTC() ).toEpochMilli()

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

actual fun libsodiumDir () = Paths.get( "build", "lib", "libsodium" ).toString()
