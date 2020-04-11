package pen

/* Adopted JetBrains example code, encoding ByteArray to Base64. */
actual fun encode_b64 (bytes : ByteArray) : ByteArray
{
   val BASE64_ALPHABET: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
   val BASE64_MASK: Byte = 0x3f
   val BASE64_PAD: Char = '='

   fun Int.toBase64(): Char = BASE64_ALPHABET[this]
   fun ByteArray.getOrZero(index: Int): Int = if (index >= size) 0 else get(index).toInt()

   val result = ArrayList<Byte>(4 * bytes.size / 3)
   var index = 0

   while (index < bytes.size) {
      val symbolsLeft = bytes.size - index
      val padSize = if (symbolsLeft >= 3) 0 else (3 - symbolsLeft) * 8 / 6
      val chunk = (bytes.getOrZero(index) shl 16) or (bytes.getOrZero(index + 1) shl 8) or bytes.getOrZero(index + 2)
      index += 3

      for (i in 3 downTo padSize) {
         val char = (chunk shr (6 * i)) and BASE64_MASK.toInt()
         result.add(char.toBase64().toByte())
      }

      repeat(padSize) { result.add(BASE64_PAD.toByte()) }
   }

   return result.toByteArray()
}

/* Stub */
actual fun decode_b64 (encoded : ByteArray) = ByteArray( 0 )

