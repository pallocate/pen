package pen.eco.common

import pen.eco.KSettings
import pen.eco.Log

expect fun encode_b64 (bytes : ByteArray) : ByteArray
expect fun decode_b64 (encoded : String) : ByteArray
expect fun hash_md5 (bytes : ByteArray) : ByteArray
expect fun create_dir (path : String)

/** Convertes a ByteArray to a hex encoded String. */
fun ByteArray.toHex () : String
{
   Log.debug( {"Encoding ByteArray to hex"}, KSettings.COMMON_CONVERSIONS )
   var ret = ""

   forEach {
      ret += String.format( "%02x", it )
   }

   return ret
}

object Utils
{
   /** Convertes String to a valid Int. */
   fun stringToInt (input : String) : Int
   {
      var output = 0
      val convertedValue = input.toIntOrNull()

      if (convertedValue != null)
         output = convertedValue

      return output
   }

   /** Convertes String to a valid Long. */
   fun stringToLong (input : String) : Long
   {
      var output = 0L
      val convertedValue = input.toLongOrNull()

      if (convertedValue != null)
         output = convertedValue

      return output
   }

   /** Convertes String to a valid Float. */
   fun stringToFloat (input : String) : Float
   {
      var output = 0F
      val convertedValue = input.toFloatOrNull()

      if (convertedValue != null)
         output = convertedValue

      return output
   }

   /** Returns a valid String. */
   fun safeString (string : String?) : String
   {
      var ret = ""
      if (string != null)
         ret = string

      return ret
   }

   /** Convertes a Base64 encoded String to a ByteArray. */
   fun decodeB64 (encoded : String) : ByteArray
   {
      Log.debug( {"Decoding Base64 encoded string"}, KSettings.COMMON_CONVERSIONS )
      var ret = ByteArray( 0 )

      try
      {ret = decode_b64( encoded )}
      catch (e : Exception)
      { Log.warn( {"Decoding Base64 failed!"}, KSettings.COMMON_CONVERSIONS ) }

      return ret
   }

   /** Convertes a ByteArray to a Base64 encoded String. */
   fun encodeB64 (input : ByteArray) = String(encode_b64( input ))

   /** Returns a small name/id, useful for debugging. */
   fun smallId (input : ByteArray) : String
   {
      val hash = hash_md5( input )
      val b64 = String(encode_b64( hash ))

      return b64.substring( 0, 5 )
   }
}
