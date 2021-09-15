package pen

fun String.toByteArray () : ByteArray
{
   val charArray = toCharArray()
   val arraySize = charArray.size
   val byteArray = ByteArray( arraySize )

   for (a in 0 until arraySize)
      byteArray[a] = charArray[a].toByte()

   return byteArray
}

fun ByteArray.toString () : String
{
   val arraySize = size
   val charArray = CharArray( arraySize )
   for (a in 0 until arraySize)
   {
      val byte = this[a]
      charArray[a] = if (byte < 128) byte.toChar() else 0.toChar()
   }

   return charArray.concatToString()
}
