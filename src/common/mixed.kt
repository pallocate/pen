package pen

/** Implementing classes should provide a password every time the password function is called. */
interface PasswordProvider
{ fun password () : String }
object NoPasswordProvider : PasswordProvider
{ override fun password () = "" }

interface Tagged
{ fun tag () : String }

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
