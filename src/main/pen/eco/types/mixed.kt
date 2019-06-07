package pen.eco.types

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import pen.eco.PlaceHolder

/** Implementing classes should provide a password every time the password function is called. */
interface PasswordProvider
{ fun password () : String }
class NoPasswordProvider : PasswordProvider, PlaceHolder
{ override fun password () = string( "NoPasswordProvider.password()" ) }

interface Convertable
{ fun getConverters() : Array<Converter> }
class NoConvertable : Convertable
{override fun getConverters () = Array<Converter>( 0, {NoConverter()} )}

class NoConverter () : Converter
{
   override fun canConvert (cls : Class<*>) = false
   override fun toJson (value : Any) = ""
   override fun fromJson (jv : JsonValue) {}
}

interface Identifiable
{ val id : Long }
class NoIdentifiable : Identifiable
{ override val id = 0L }

interface Hashable
{ abstract fun hash () : ByteArray }

class WrongVersionException : Exception()
