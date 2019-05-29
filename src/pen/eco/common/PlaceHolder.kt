package pen.eco.common

import pen.eco.Log
import pen.eco.KSettings

/** Contains methods for use by a place holder. */
interface PlaceHolder
{
   private inline fun <reified T : Any>logDebug (msg : String?, value : Any) : T
   {
      if (KSettings.COMMON_PLACE_HOLDER)
         Log.warn( "PlaceHolder- \"$msg\"" )

      return value as T
   }

   fun unit (msg : String?) = logDebug<Unit>( msg, Unit )

   fun boolean (msg : String?, returnValue : Boolean = false) = logDebug<Boolean>( msg, returnValue )

   fun int (msg : String?, returnValue : Int = 0) = logDebug<Int>( msg, returnValue )

   fun long (msg : String?, returnValue : Long = 0L) = logDebug<Long>( msg, returnValue )

   fun double (msg : String?, returnValue : Double = 0.toDouble()) = logDebug<Double>( msg, returnValue )

   fun string (msg : String?, returnValue : String = "") = logDebug<String>( msg, returnValue )

   fun byteArray (msg : String?, returnValue : ByteArray = ByteArray( 9 )) = logDebug<ByteArray>( msg, returnValue )
}
