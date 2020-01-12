package pen

import java.nio.file.Paths
import java.nio.file.Files
import java.io.FileWriter
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*
import pen.par.KMember
import pen.par.KCouncil

/** Serializes object and writes it to file */
actual inline fun <reified T : Any>writeObject (obj : T, serializerFunction : () -> KSerializer<T>, filename : String) : Boolean
{
   Log.debug( "Writing object" )// , Config.trigger( "SAVE_LOAD" )
   var success = false
   val json = Json( JsonConfiguration.Stable )
   val fileWriter = FileWriter( filename )

   try
   {
      val jsonString = json.stringify( serializerFunction(), obj )
      fileWriter.write( jsonString )
      fileWriter.close()
      success = true
   }
   catch (e : Exception)
   {Log.error( "Writing object failed! (${e.message})" )}

   return success
}

/** Reads json from file and deserializes it to a object */
actual inline fun <reified T : Any>readObject (serializerFunction : () -> KSerializer<T>, filename : String) : T?
{
   Log.debug(  "Reading object file \"$filename\"" )
   val json = Json( JsonConfiguration.Stable )
   var ret : T? =null

   try
   {
      val path = Paths.get( filename )
      val jsonBytes = Files.readAllBytes( path )
      val jsonString = String( jsonBytes )
      ret = json.parse( serializerFunction(), jsonString )
   }
   catch (e : Exception)
   { Log.error( "Reading object file failed!" ) }

   return ret
}
