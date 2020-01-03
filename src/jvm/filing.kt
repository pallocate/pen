package pen

import java.nio.file.Paths
import java.nio.file.Files
import java.io.FileWriter
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*
import pen.par.KMember
import pen.par.KCouncil

actual object Filer : Loggable
{
   val EXTENSION = ".json"

   actual inline fun <reified T : Any>write (serialFun : () -> KSerializer<T>, obj : T, name : String)
   {
      log("writing object", Config.trigger( "SAVE_LOAD" ))
      val json = Json( JsonConfiguration.Stable )
      val fileWriter = FileWriter( name + EXTENSION )

      try
      {
         val jsonString = json.stringify( serialFun(), obj )
         fileWriter.write( jsonString )
         fileWriter.close()
      }
      catch (e : Exception)
      { Log.error( e.message!! )}
   }

   actual inline fun <reified T : Any>read (serialFun : () -> KSerializer<T>, name : String) : T?
   {
      log( "reading object", Config.trigger( "SAVE_LOAD" ))
      val json = Json( JsonConfiguration.Stable )
      val ret : T? =null

      try
      {
         val path = Paths.get( name + EXTENSION )
         val jsonBytes = Files.readAllBytes( path )
         val jsonString = String( jsonBytes )
         val ret = Json.parse( serialFun(), jsonString )
      }
      catch (e : Exception)
      {log( "Reading object failed! (${e.message})", Config.trigger( "SAVE_LOAD" ), LogLevel.ERROR )}

      return ret
   }

   override fun originName () = "Filer"
}
