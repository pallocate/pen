package pen.net.kad.utils

import java.io.InputStream
import java.io.OutputStream
import com.beust.klaxon.Klaxon
import pen.eco.Log
import pen.eco.Config
import pen.eco.KByteArrayConverter
import pen.eco.KInetAddressConverter
import pen.net.kad.messages.Message
import pen.net.kad.messages.NoMessage

object KMessageSerializer
{
   val byteArrayConverter = KByteArrayConverter()
   val inetAddressConverter = KInetAddressConverter()

   inline fun <reified T : Message> read (inputStream : InputStream) : Message
   {
      Log.debug({"KMessageSerializer- reading object"}, Config.flag( "KAD_STREAMING" ))
      var klaxon = Klaxon()

      klaxon = klaxon.converter( byteArrayConverter )
      klaxon = klaxon.converter( inetAddressConverter )

      var parseResult : T? = null

      try
      { parseResult = klaxon.parse<T>( inputStream )}
      catch (e : Exception)
      {Log.warn( "KMessageSerializer- Klaxon failed! ${e.message}" )}

      return if (parseResult != null)
         parseResult
      else
      {
         Log.warn( "KMessageSerializer- reading object failed!" )
         NoMessage()
      }
   }

   fun write (message : Message, outputStream : OutputStream)
   {
      Log.debug({"KMessageSerializer- writing object"}, Config.flag( "KAD_STREAMING" ))
      var klaxon = Klaxon()

      klaxon = klaxon.converter( byteArrayConverter )
      klaxon = klaxon.converter( inetAddressConverter )

      val json = klaxon.toJsonString( message ).toByteArray()

      outputStream.write( json )
      outputStream.flush()
   }
}
