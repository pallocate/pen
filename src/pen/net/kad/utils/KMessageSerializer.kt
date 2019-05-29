package pen.net.kad.utils

import java.io.InputStream
import java.io.OutputStream
import com.beust.klaxon.Klaxon
import pen.eco.Log
import pen.eco.DebugValue
import pen.eco.Config.getSettings
import pen.net.kad.messages.Message
import pen.net.kad.messages.NoMessage

object KMessageSerializer
{
   val kNodeIdConverter = KNodeIdConverter()
   val kInetAddressConverter = KInetAddressConverter()

   inline fun <reified T : Message> read (inputStream : InputStream) : Message
   {
      Log.debug({"KMessageSerializer- reading object"}, getSettings().getValue( DebugValue.STREAMING ))
      var klaxon = Klaxon()

      klaxon = klaxon.converter( kNodeIdConverter )
      klaxon = klaxon.converter( kInetAddressConverter )

      var parseResult : T? = null

      try
      { parseResult = klaxon.parse<T>( inputStream )}
      catch (e : Exception)
      {Log.warn( "KMessageSerializer- Klaxon failed! ${e.message}" )}

      return if (parseResult != null && parseResult is Message)
         parseResult
      else
      {
         Log.warn( "KMessageSerializer- reading object failed!" )
         NoMessage()
      }
   }

   fun write (message : Message, outputStream : OutputStream)
   {
      Log.debug({"KMessageSerializer- writing object"}, getSettings().getValue( DebugValue.STREAMING ))
      var klaxon = Klaxon()

      klaxon = klaxon.converter( kNodeIdConverter )
      klaxon = klaxon.converter( kInetAddressConverter )

      val json = klaxon.toJsonString( message ).toByteArray()

      outputStream.write( json )
      outputStream.flush()
   }
}
