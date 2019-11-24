package pen.net.kad.utils

import java.io.InputStream
import java.io.OutputStream
import com.beust.klaxon.Klaxon
import pen.eco.Loggable
import pen.eco.LogLevel.WARN
import pen.eco.Config
import pen.eco.KByteArrayConverter
import pen.eco.KInetAddressConverter
import pen.net.kad.messages.Message
import pen.net.kad.messages.NoMessage

object KMessageSerializer : Loggable
{
   val byteArrayConverter = KByteArrayConverter()
   val inetAddressConverter = KInetAddressConverter()

   inline fun <reified T : Message> read (inputStream : InputStream) : Message
   {
      log({"KMessageSerializer- reading object"}, Config.trigger( "KAD_STREAMING" ))
      var klaxon = Klaxon()

      klaxon = klaxon.converter( byteArrayConverter )
      klaxon = klaxon.converter( inetAddressConverter )

      var parseResult : T? = null

      try
      { parseResult = klaxon.parse<T>( inputStream )}
      catch (e : Exception)
      { log("KMessageSerializer- Klaxon failed! ${e.message}", Config.trigger( "KAD_STREAMING" ), WARN) }

      return if (parseResult != null)
         parseResult
      else
      {
         log( "KMessageSerializer- reading object failed!", Config.trigger( "KAD_STREAMING" ), WARN)
         NoMessage()
      }
   }

   fun write (message : Message, outputStream : OutputStream)
   {
      log({"KMessageSerializer- writing object"}, Config.trigger( "KAD_STREAMING" ))
      var klaxon = Klaxon()

      klaxon = klaxon.converter( byteArrayConverter )
      klaxon = klaxon.converter( inetAddressConverter )

      val json = klaxon.toJsonString( message ).toByteArray()

      outputStream.write( json )
      outputStream.flush()
   }

   override fun originName () = "KMessageSerializer"
}
