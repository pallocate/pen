package pen.eco

import java.io.Writer
import java.io.Reader
import kotlin.reflect.KClass
import com.beust.klaxon.Klaxon
import pen.eco.types.Convertable
import pen.eco.types.NoConvertable

object Serializer
{
   inline fun <reified T : Convertable>read (reader : Reader) : Convertable
   {
      Log.debug({ "KSerializer- reading object" }, Config.flag( "SERIALIZE" ))
      var klaxon = Klaxon()
      var convertable : Convertable = NoConvertable()

      try
      { convertable = T::class.java.newInstance() }                             // Try to create an instnce
      catch (e: Exception) {}

      if (convertable !is NoConvertable)
         for (converter in convertable.getConverters())
            klaxon = klaxon.converter( converter )                              // Add converters from the instance

      var parseResult : T? = null

      try
      {
         parseResult = klaxon.parse<T>( reader )
      }
      catch (e : Exception)
      {Log.err( "KSerializer- Klaxon failed! ${e.message}" )}

      return if (parseResult != null && parseResult is Convertable)
         parseResult
      else
      {
         Log.warn( "KSerializer- reading object failed!" )
         NoConvertable()
      }
   }

   fun write (convertable : Convertable, writer : Writer)
   {
      Log.debug({ "KSerializer- writing object" }, Config.flag( "SERIALIZE" ))
      var klaxon = Klaxon()

      for (converter in convertable.getConverters())
         klaxon = klaxon.converter( converter )

      writer.write(klaxon.toJsonString( convertable ))
      writer.flush()
   }
}
