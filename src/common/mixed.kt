package pen

import kotlinx.serialization.internal.HexConverter

/** Implementing classes should provide a password every time the password function is called. */
interface PasswordProvider
{ fun password () : String }
class NoPasswordProvider : PasswordProvider, PlaceHolder
{ override fun password () = string( "NoPasswordProvider" ) }

interface Identifiable
{ val id : Long }
class NoIdentifiable : Identifiable
{ override val id = 0L }

interface Hashable
{ abstract fun hash () : ByteArray }

interface Tagged
{ fun tag () : String }

class WrongVersionException : Exception()

enum class KeyType
{ SYMETRIC, PUBLIC, SECRET }

/** Convertes a ByteArray to a hex encoded String. */
@kotlinx.serialization.InternalSerializationApi
fun ByteArray.toHex () = HexConverter.printHexBinary( this )

fun Long.coerceToInt () = this.coerceIn( -2147483648L, 2147483647L ).toInt()

/** Convertes Long to a ByteArray. */
fun Long.toByteArray () : ByteArray
{
   val BYTE_MASK = 0xFFL
   val ret = ByteArray( 8 )

   for (i in 0..7)
      ret[7 - i] = (this shr (8*i) and BYTE_MASK).toByte()

   return ret
}
/** Filters out possibly unsafe characters. */
fun String.safePath () = filter  {
                                    ('0'..'9').contains( it ) ||
                                    ('A'..'Z').contains( it ) ||
                                    ('a'..'z').contains( it ) ||
                                    it > 191.toChar()
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
