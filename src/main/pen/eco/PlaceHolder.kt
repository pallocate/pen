package pen.eco

/** Contains methods for use by a place holder. */
interface PlaceHolder
{
   private inline fun <reified T : Any>logDebug (msg : String?, value : Any) : T
   {
      Log.warn( {"PlaceHolder- \"$msg\""}, Config.flag( "COMMON_PLACE_HOLDER" ) )
      return value as T
   }

   fun unit (msg : String?) = logDebug<Unit>( msg, Unit )

   fun boolean (msg : String?, returnValue : Boolean = false) = logDebug<Boolean>( msg, returnValue )

   fun int (msg : String?, returnValue : Int = 0) = logDebug<Int>( msg, returnValue )

   fun long (msg : String?, returnValue : Long = 0L) = logDebug<Long>( msg, returnValue )

   fun double (msg : String?, returnValue : Double = 0.toDouble()) = logDebug<Double>( msg, returnValue )

   fun string (msg : String?, returnValue : String = "") = logDebug<String>( msg, returnValue )

   fun byteArray (msg : String?, returnValue : ByteArray = ByteArray( 0 )) = logDebug<ByteArray>( msg, returnValue )
}
