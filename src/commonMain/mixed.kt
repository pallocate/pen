package pen

/** Implementing classes should provide a password every time the password function is called. */
interface PasswordProvider
{ fun password () : String }
object VoidPasswordProvider : PasswordProvider
{ override fun password () = "" }

interface Tagged
{ fun tag () : String }

interface Voidable
{ fun isVoid () : Boolean }

val VOID_BYTES = ByteArray( 0 )

interface IrohaSigner
{
   fun sign (input : ByteArray) : String
   fun publicKey () : String
}
object VoidIrohaSigner : IrohaSigner
{override fun sign (input : ByteArray)="";override fun publicKey () = ""}

fun Long.coerceToInt () = this.coerceIn( -2147483648L, 2147483647L ).toInt()

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
