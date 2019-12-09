package pen.eco.types

import pen.eco.types.PlaceHolder

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

class WrongVersionException : Exception()

data class KTransaction (val productId : Long, val signature : ByteArray, val userKey : ByteArray) {}

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
