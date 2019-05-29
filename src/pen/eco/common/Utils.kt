package pen.eco.common

import java.util.Base64
import java.math.BigInteger
import java.security.MessageDigest
import pen.eco.KSettings
import pen.eco.Log

object Utils
{
   /** Convertes a Base64 encoded String to a ByteArray. */
   fun B64StringToByteArray (encoded : String) : ByteArray
   {
      Log.debug( {"Decoding Base64 encoded string"}, KSettings.COMMON_CONVERSIONS )
      var ret = ByteArray( 0 )

      try
      { ret = Base64.getDecoder().decode( encoded.toByteArray() ) }
      catch (e : Exception)
      { Log.warn( {"Decoding Base64 failed!"}, KSettings.COMMON_CONVERSIONS ) }

      return ret
   }

   /** Convertes a ByteArray to a Base64 encoded String. */
   fun byteArrayToB64String (input : ByteArray) : String
   {
      Log.debug( {"Encoding ByteArray to Base64"}, KSettings.COMMON_CONVERSIONS )
      return Base64.getEncoder().encodeToString( input )
   }

   /** Convertes a ByteArray to a hex encoded String. */
   fun byteArrayToHexString (input : ByteArray) : String
   {
      Log.debug( {"Encoding ByteArray to hex"}, KSettings.COMMON_CONVERSIONS )

      val bi = BigInteger( 1, input )
      return String.format( "%0" + (input.size shl 1) + "X", bi )
   }

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

   /** Returns a small name/id, useful for debugging. */
   fun smallId (input : ByteArray) : String
   {
      val hash = java.security.MessageDigest.getInstance( "MD5" ).digest( input )
      val b64 = Base64.getEncoder().encodeToString( hash )

      return b64.substring( 0, 5 )
   }
}
