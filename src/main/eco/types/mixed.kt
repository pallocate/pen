package pen.eco.types

import pen.eco.types.PlaceHolder

/** Implementing classes should provide a password every time the password function is called. */
interface PasswordProvider
{ fun password () : String }
class NoPasswordProvider : PasswordProvider, PlaceHolder
{ override fun password () = string( "NoPasswordProvider.password()" ) }

interface Identifiable
{ val id : Long }
class NoIdentifiable : Identifiable
{ override val id = 0L }

interface Hashable
{ abstract fun hash () : ByteArray }

class WrongVersionException : Exception()
