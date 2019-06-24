package pen.eco

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

actual object Filer
{
   actual inline fun <reified T : Filable>read (name : String) : Filable
   {
      Log.debug({ "Filer- reading object" }, Config.flag( "SERIALIZE" ))

      val klaxon = KlaxonFactory.forClass( T::class.simpleName )
      val fileReader = FileReader( "${name}.json" )
      var parseResult : T? = null

      try
      {
         parseResult = klaxon.parse<T>( fileReader )
      }
      catch (e : Exception)
      {Log.err( "Filer- Klaxon failed! ${e.message}" )}

      return if (parseResult != null)
                parseResult
             else
             {
                Log.warn( "Serializer- reading object failed!" )
                NoFilable()
             }
   }

   actual fun write (filable : Filable, name : String)
   {
      Log.debug({ "Filer- writing object" }, Config.flag( "SERIALIZE" ))

      val klaxon = KlaxonFactory.forClass( filable::class.simpleName )
      val fileWriter = FileWriter( "${name}.json" )

      fileWriter.write(klaxon.toJsonString( filable ))
      fileWriter.close()
   }
}
