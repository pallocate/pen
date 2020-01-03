package pen.eco

data class KTransaction (val productId : Long, val signature : ByteArray, val userKey : ByteArray)

interface InitialCharacter
{ fun char () : Char }

/** What economic function something has in the economy. */
enum class Target : InitialCharacter {
   UNDEFINED { override fun char () = 'U' },
   CONSUMPTION { override fun char () = 'C' },
   PRODUCTION { override fun char () = 'P' };

   companion object
   {
      fun fromInt (int : Int) : Target = values()[int.coerceIn( UNDEFINED.ordinal, PRODUCTION.ordinal )]
   }
}
