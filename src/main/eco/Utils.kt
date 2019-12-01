package pen.eco

/** Should derive a short five letter name for debugging. */
expect fun encode_b64 (bytes : ByteArray) : ByteArray
expect fun decode_b64 (encoded : ByteArray) : ByteArray
expect fun hash_md5 (bytes : ByteArray) : ByteArray
expect fun createDir (path : String)
expect fun now () : Long

/** Convertes a ByteArray to a hex encoded String. */
fun ByteArray.toHex () = this.joinToString( "" ) { it.toInt().and( 0xFF ).toString( 16 ).padStart( 2, '0' )}

/** Convertes Long to a ByteArray. */
fun Long.toByteArray () : ByteArray
{
   val BYTE_MASK = 0xFFL
   val ret = ByteArray( 8 )

   for (i in 0..7)
      ret[7 - i] = (this shr (8*i) and BYTE_MASK).toByte()

   return ret
}

object Utils : Loggable
{
   fun stringToByteArray (string : String) : ByteArray
   {
      val charArray = string.toCharArray()
      val arraySize = charArray.size
      val byteArray = ByteArray( arraySize )

      for (a in 0 until arraySize)
         byteArray[a] = charArray[a].toByte()

      return byteArray
   }

   fun byteArrayToString (byteArray : ByteArray) : String
   {
      val arraySize = byteArray.size
      val charArray = CharArray( arraySize )
      for (a in 0 until arraySize)
      {
         val byte = byteArray[a]
         charArray[a] = if (byte < 128) byte.toChar() else 0.toChar()
      }

      return String( charArray )
   }

   /** Returns a small name/id, useful for debugging. */
   fun shortName (input : ByteArray) : String
   {
      val hash = hash_md5( input )
      val b64 = encodeB64( hash )

      return b64.substring( 0, 5 )
   }

   /** Convertes a Base64 encoded String to a ByteArray. */
   fun decodeB64 (encoded : String) : ByteArray
   {
      log("Decoding Base64 encoded string", Config.trigger( "PLATFORM" ))
      var ret = ByteArray( 0 )

      try
      { ret = decode_b64(stringToByteArray( encoded )) }
      catch (t : Throwable)
      { log( "Decoding Base64 failed!", Config.trigger( "PLATFORM" ), LogLevel.ERROR) }

      return ret
   }

   /** Convertes a ByteArray to a Base64 encoded String. */
   fun encodeB64 (input : ByteArray) : String
   {
      log("Encoding string to Base64", Config.trigger( "PLATFORM" ))
      var ret = ""

      try
      { ret = byteArrayToString(encode_b64( input )) }
      catch (t : Throwable)
      { log( "Encoding Base64 failed!", Config.trigger( "PLATFORM" ), LogLevel.ERROR) }

      return ret
   }

   /** Returns a String(non-null). */
   fun safeString (string : String?) = if (string == null) "" else string

   /** Convertes String to a valid Int. */
   fun stringToInt (input : String, min : Int = Int.MIN_VALUE, max : Int = Int.MAX_VALUE, coerce : Boolean = false) : Int
   {
      var output = 0
      val value = input.toIntOrNull()

      if (value != null)
      {      
         if (coerce)
            output = value.coerceIn( min, max )
         else
            if (value >= min && value <= max)
               output = value
      }

      return output
   }

   /** Convertes String to a valid Long. */
   fun stringToLong (input : String, min : Long = Long.MIN_VALUE, max : Long = Long.MAX_VALUE, coerce : Boolean = false) : Long
   {
      var output = 0L
      val value = input.toLongOrNull()

      if (value != null)
      {
         if (coerce)
            output = value.coerceIn( min, max )
         else
            if (value >= min && value <= max)
               output = value                                                      
      }

      return output
   }

   /** Convertes String to a valid Float. */
   fun stringToFloat (input : String) : Float
   {
      var output = 0F
      val value = input.toFloatOrNull()

      if (value != null)
         output = value

      return output
   }

   override fun originName () = "Utils"
}
