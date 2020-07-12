package pen

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

   /** Decodes a base-64 ByteArray. */
   fun decodeB64 (encoded : ByteArray) : ByteArray
   {
      log("Decoding Base64 encoded string", Config.trigger( "DECODE" ))
      var ret = ByteArray( 0 )

      try
      {ret = decode_b64( encoded )}
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

   /** A short id that might be useful in debugging. */
   fun shortName (input : ByteArray) : String
   {
      val hash = hash_md5( input )
      val base64String = byteArrayToString(encode_b64( hash ))

      return base64String.substring( 0, 5 )
   }

   override fun tag () = "Utils"
}
