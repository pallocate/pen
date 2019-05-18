package pen.eco.common

import java.io.Writer
import java.io.Reader
import com.beust.klaxon.Klaxon

object KSerializer
{
   inline fun <reified T : Convertable>read (reader : Reader) : Convertable
   {
      Log.debug({"KSerializer- reading object"}, KSettings.SERIALIZE )
      var klaxon = Klaxon()

      for (converter in T::class.java.newInstance().getConverters())
         klaxon = klaxon.converter( converter )

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

   fun write (serialisable : Convertable, writer : Writer)
   {
      Log.debug( {"KSerializer- writing object"}, KSettings.SERIALIZE )
      var klaxon = Klaxon()

      for (converter in serialisable.getConverters())
         klaxon = klaxon.converter( converter )

      writer.write(klaxon.toJsonString( serialisable ))
      writer.flush()
   }
}
