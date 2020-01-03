package pen

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

/** Convertes String to a valid Long. */
fun String.toLong (min : Long = Long.MIN_VALUE, max : Long = Long.MAX_VALUE, coerce : Boolean = false) : Long
{
   var ret = 0L
   val value = toLongOrNull()

   if (value != null)
   {
      if (coerce)
         ret = value.coerceIn( min, max )
      else
         if (value >= min && value <= max)
            ret = value
   }

   return ret
}

/** Convertes String to a valid Int. */
fun String.toInt (min : Int = Int.MIN_VALUE, max : Int = Int.MAX_VALUE, coerce : Boolean = false) : Int
{
   var ret = 0
   val value = toIntOrNull()

   if (value != null)
   {
      if (coerce)
         ret = value.coerceIn( min, max )
      else
         if (value >= min && value <= max)
            ret = value
   }

   return ret
}

fun String.toFloat () : Float
{
   val value = toFloatOrNull()
   return   if (value != null)
               value
            else
               0F
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

   /** A short id that might be useful in debugging. */
   fun shortName (input : ByteArray) : String
   {
      val hash = hash_md5( input )
      val b64 = encodeB64( hash )

      return b64.substring( 0, 5 )
   }

   /** Convertes a Base64 encoded String to a ByteArray. */
   fun decodeB64 (encoded : String) : ByteArray
   {
      log("Decoding Base64 encoded string", Config.trigger( "DECODE" ))
      var ret = ByteArray( 0 )

      try
      { ret = decode_b64(stringToByteArray( encoded )) }
      catch (t : Throwable)
      { log( "Decoding Base64 failed!", Config.trigger( "DECODE" ), LogLevel.ERROR) }

      return ret
   }

   /** Convertes a ByteArray to a Base64 encoded String. */
   fun encodeB64 (input : ByteArray) : String
   {
      log("Encoding string to Base64", Config.trigger( "ENCODE" ))
      var ret = ""

      try
      { ret = byteArrayToString(encode_b64( input )) }
      catch (t : Throwable)
      { log( "Encoding Base64 failed!", Config.trigger( "ENCODE" ), LogLevel.ERROR) }

      return ret
   }

   override fun originName () = "Utils"
}
