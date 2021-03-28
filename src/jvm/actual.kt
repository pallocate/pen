package pen

import java.util.Properties
import java.io.File
import java.io.FileReader
import java.nio.file.Paths
import java.time.Clock
import java.time.Instant
import org.apache.commons.codec.binary.Hex

actual fun now () = Instant.now( Clock.systemUTC() ).toEpochMilli()

actual fun ByteArray.toHex () : String = Hex.encodeHexString( this )

actual fun loadConf (filename : String) : Map<String, String>
{
   val properties = Properties()
   val resultMap = HashMap<String, String>()

   try
   {
      properties.load(FileReader( filename ))

      for (key in properties.keys())
         resultMap.put(key as String, properties.get( key ) as String)
   }
   catch (e : Exception)
   {println( "Loading \"${filename}\" failed!" )}

   return resultMap
}

actual fun libsodiumPath () = Paths.get( "build", "lib", "libsodium", "libsodium" ).toString()
actual fun ed25519Sha3 (seed : ByteArray) : IrohaSigner = KIrohaSigner( seed )
