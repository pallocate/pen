package pen

import pen.Loggable
import pen.Config
import pen.LogLevel.WARN

/** Contains methods for use by a place holder. */
interface PlaceHolder : Loggable
{
   private inline fun <reified T : Any>logDebug (msg : String, value : Any) : T
   {
      log(msg, Config.trigger( "PLACE_HOLDER" ), WARN)
      return value as T
   }

   fun unit (origin : String) = logDebug<Unit>( origin, Unit )

   fun boolean (origin : String) = logDebug<Boolean>( origin, false )

   fun int (origin : String) = logDebug<Int>( origin, -1 )

   fun long (origin : String) = logDebug<Long>( origin, -1L )

   fun double (origin : String) = logDebug<Double>( origin, -1.toDouble() )

   fun string (origin : String) = logDebug<String>( origin, "" )

   fun byteArray (origin : String) = logDebug<ByteArray>( origin, ByteArray( 0 ) )

   override fun originName () = "PlaceHolder"
}
