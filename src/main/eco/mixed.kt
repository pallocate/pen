package pen.eco

data class KTransaction (val productId : Long, val signature : ByteArray, val userKey : ByteArray)

interface EconomicFunctionLetter
{ fun letter () : Char }

/** What economic function something has in the economy. */
enum class EconomicFunction : EconomicFunctionLetter {
   UNDEFINED { override fun letter () = 'U' },
   CONSUMPTION { override fun letter () = 'C' },
   PRODUCTION { override fun letter () = 'P' };

   companion object
   {
      fun fromInt (int : Int) : EconomicFunction = values()[int.coerceIn( UNDEFINED.ordinal, PRODUCTION.ordinal )]
   }
}
