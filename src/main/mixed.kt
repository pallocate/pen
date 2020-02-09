package pen

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
