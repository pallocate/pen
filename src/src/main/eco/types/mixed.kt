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
