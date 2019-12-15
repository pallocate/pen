package pen

import java.io.FileWriter
import java.io.FileReader
import com.beust.klaxon.Klaxon

object KlaxonFactory
{
   fun forClass (className : String?) : Klaxon
   {
      var ret = Klaxon()

      if (className != null)
         ret = when (className)
         {
            "KNode" ->
               ret.converter( KByteArrayConverter() ).converter( KInetAddressConverter() )
            "KSerializableRoutingInfo" ->
               ret.converter( KByteArrayConverter() ).converter( KInetAddressConverter() )
            "KDHT" ->
               ret.converter( KByteArrayConverter() )
            "KStorageEntry" ->
               ret.converter( KByteArrayConverter() )
            else -> ret
         }

      return ret
   }
}

actual object Filer : Loggable
{
   val EXTENSION = ".json"

   actual inline fun <reified T : Filable>read (name : String) : Filable
   {
      log( "reading object", Config.trigger( "SAVE_LOAD" ))

      val klaxon = KlaxonFactory.forClass( T::class.simpleName )
      val fileReader = FileReader( name + EXTENSION )
      var parseResult : T? = null

      try
      {
         parseResult = klaxon.parse<T>( fileReader )
      }
      catch (e : Exception)
      {log( "Klaxon failed! ${e.message}", Config.trigger( "SAVE_LOAD" ), LogLevel.ERROR )}

      return if (parseResult != null)
                parseResult
             else
             {
                log("reading object failed!", Config.trigger( "SAVE_LOAD" ), LogLevel.WARN)
                NoFilable()
             }
   }

   actual fun write (filable : Filable, name : String)
   {
      log("writing object", Config.trigger( "SAVE_LOAD" ))

      val klaxon = KlaxonFactory.forClass( filable::class.simpleName )
      val fileWriter = FileWriter( name + EXTENSION )

      fileWriter.write(klaxon.toJsonString( filable ))
      fileWriter.close()
   }
   override fun originName () = "Filer"
}
