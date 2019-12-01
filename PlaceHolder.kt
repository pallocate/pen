package pen.eco.types

import pen.eco.Loggable
import pen.eco.Config
import pen.eco.LogLevel.WARN

/** Contains methods for use by a place holder. */
interface PlaceHolder : Loggable
{
   private inline fun <reified T : Any>logDebug (msg : String, value : Any) : T
   {
      log(msg, Config.trigger( "PLACE_HOLDER" ), WARN)
      return value as T
   }

   fun any (msg : String?, returnValue : Any) = logDebug<Any>( msg!!, returnValue )

   fun unit (msg : String?) = logDebug<Unit>( msg!!, Unit )

   fun boolean (msg : String?, returnValue : Boolean = false) = logDebug<Boolean>( msg!!, returnValue )

   fun int (msg : String?, returnValue : Int = 0) = logDebug<Int>( msg!!, returnValue )

   fun long (msg : String?, returnValue : Long = 0L) = logDebug<Long>( msg!!, returnValue )

   fun double (msg : String?, returnValue : Double = 0.toDouble()) = logDebug<Double>( msg!!, returnValue )

   fun string (msg : String?, returnValue : String = "") = logDebug<String>( msg!!, returnValue )

   fun byteArray (msg : String?, returnValue : ByteArray = ByteArray( 0 )) = logDebug<ByteArray>( msg!!, returnValue )

   override fun originName () = "PlaceHolder"
}
