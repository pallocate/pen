package pen.eco

/** Should derive a short five letter name for debugging. */
expect fun encode_b64 (bytes : ByteArray) : ByteArray
expect fun decode_b64 (encoded : ByteArray) : ByteArray
expect fun hash_md5 (bytes : ByteArray) : ByteArray
expect fun create_dir (path : String)

/** Convertes a ByteArray to a hex encoded String. */
fun ByteArray.toHex () = this.joinToString( "" ) { it.toInt().and( 0xFF ).toString( 16 ).padStart( 2, '0' )}

object Utils : Loggable
{
   fun encodeLong (long : Long) = encodeArbitrary( 8, long )
   fun encodeInt (int : Int) = encodeArbitrary( 4, int.toLong() )

   /** Convertes a arbitrary sized integer (Long type) to a ArrayList of Bytes.
     * @param nrOfBytes Nr of bytes in the resulting array.
     * @param integer The arbitrary sized integer as a Long.
     * @return The resulting ArrayList. */
   fun encodeArbitrary (nrOfBytes : Int, integer : Long) : ByteArray
   {
      val BYTE_MASK = 0xFFL
      var ret = ByteArray( 0 )

      if (integer > pow2(nrOfBytes*8 - 1))
         Log.warn( "Long value to large" )
      else
         for (i in (nrOfBytes - 1) downTo 0)
            ret += (integer shr (8*i) and BYTE_MASK).toByte()

      return ret
   }

   /** Calculates two to the power of something. */
   fun pow2 (exponent : Int) : Int
   {
      var ret = 0

      if (exponent >= 0)
      {
         ret = 1
         for (n in 1..exponent)
            ret *= 2
      }

      return ret
   }

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
      log("Decoding Base64 encoded string", Config.flag( "PLATFORM" ))
      var ret = ByteArray( 0 )

      try
      { ret = decode_b64(stringToByteArray( encoded )) }
      catch (t : Throwable)
      { log( "Decoding Base64 failed!", Config.flag( "PLATFORM" ), LogLevel.ERROR) }

      return ret
   }

   /** Convertes a ByteArray to a Base64 encoded String. */
   fun encodeB64 (input : ByteArray) : String
   {
      log("Encoding string to Base64", Config.flag( "PLATFORM" ))
      var ret = ""

      try
      { ret = byteArrayToString(encode_b64( input )) }
      catch (t : Throwable)
      { log( "Encoding Base64 failed!", Config.flag( "PLATFORM" ), LogLevel.ERROR) }

      return ret
   }

   /** Returns a String(non-null). */
   fun safeString (string : String?) = if (string == null) "" else string

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

   override fun originName () = "Utils"
}
