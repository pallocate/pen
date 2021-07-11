package pen

import java.io.File
import java.io.FileReader
import java.nio.file.Paths
import java.security.SecureRandom
import java.time.Clock
import java.time.Instant
import java.util.Properties
import org.apache.commons.codec.binary.Hex

actual fun now () = Instant.now( Clock.systemUTC() ).toEpochMilli()

actual fun randomBytes (size : Int) = ByteArray( size ).also {SecureRandom().nextBytes( it )}

actual fun ByteArray.toHex () : String = Hex.encodeHexString( this )

actual fun loadConf (filename : String) : Map<String, String>
{
   val ret = HashMap<String, String>()

   try
   {
      val properties = Properties().apply { load(FileReader( filename )) }
      
      properties.forEach { entry -> 
         ret.put(entry.key as String, entry.value as String) 
      }
   }
   catch (e : Exception)
   {println( "Loading \"${filename}\" failed!" )}

   return ret
}

actual fun libsodiumPath () = Paths.get( "build", "lib", "libsodium", "libsodium" ).toString()
actual fun ed25519Sha3 (seed : ByteArray) : IrohaSigner = KIrohaSigner( seed )
