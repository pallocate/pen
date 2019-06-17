package pen.eco

import java.io.Writer
import java.io.Reader
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

actual object Serializer
{
   actual inline fun <reified T : Convertable>read (reader : Reader) : Convertable
   {
      Log.debug({ "Serializer- reading object" }, Config.flag( "SERIALIZE" ))
      val klaxon = KlaxonFactory.forClass( T::class.simpleName )

      var parseResult : T? = null
      try
      {
         parseResult = klaxon.parse<T>( reader )
      }
      catch (e : Exception)
      {Log.err( "Serializer- Klaxon failed! ${e.message}" )}

      return if (parseResult != null)
                parseResult
             else
             {
                Log.warn( "Serializer- reading object failed!" )
                NoConvertable()
             }
   }

   actual fun write (convertable : Convertable, writer : Writer)
   {
      Log.debug({ "Serializer- writing object" }, Config.flag( "SERIALIZE" ))
      val klaxon = KlaxonFactory.forClass( convertable::class.simpleName )

      writer.write(klaxon.toJsonString( convertable ))
      writer.flush()
   }
}
