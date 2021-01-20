package pen

import java.nio.file.Paths
import java.nio.file.Files
import java.io.FileWriter
import kotlinx.serialization.KSerializer
//import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.json.*

/** Reads json from file and deserializes it to a object */
actual inline fun <reified T : Any>deserializeFromFile (filename : String, serializer : KSerializer<T>) : T?
{
   Log.log("Reading object file \"$filename\"", Config.trigger( "SAVE_LOAD" ))
   var ret : T? = null

   try
   {
      val path = Paths.get( filename )
      val jsonString = String(Files.readAllBytes( path ))
      ret = Json.decodeFromString<T>( serializer, jsonString )
   }
   catch (e : Exception)
   {Log.log("Reading object file failed!", Config.trigger( "SAVE_LOAD" ), LogLevel.ERROR)}

   return ret
}

/** Serializes object and writes it to file */
actual inline fun <reified T : Any>serializeToFile (obj : T, filename : String, serializer : KSerializer<T>) : Boolean
{
   Log.log("Writing object", Config.trigger( "SAVE_LOAD" ))
   var success = false
   val fileWriter = FileWriter( filename )

   try
   {
      val jsonString = Json.encodeToString( serializer, obj )
      fileWriter.write( jsonString )
      fileWriter.close()
      success = true
   }
   catch (e : Exception)
   {Log.log("Writing object failed! (${e.message})", Config.trigger( "SAVE_LOAD" ), LogLevel.ERROR)}

   return success
}
