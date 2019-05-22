package pen.eco.common

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue

interface Settings
class NoSettings : Settings
interface SettingsValue

interface LogListener
{ fun eventLogged (e : Log.Event) }
class NoLogListener : LogListener
{ override fun eventLogged (e : Log.Event) {} }

/** A node in a merkle tree. */
interface MerkleNode
class NoMerkleNode : MerkleNode {}
open class KMerkleNode : MerkleNode
{var storedHash = ByteArray( 0 )}

interface Hashable
{ abstract fun hash () : ByteArray }

interface Identifiable
{ val id : Long }
class NoIdentifiable : Identifiable
{ override val id = 0L }

/** Implementing classes should provide a password every time the password function is called. */
interface PasswordProvider
{ fun password () : String }
class NoPasswordProvider : PasswordProvider, PlaceHolder
{ override fun password () = string( "NoPasswordProvider.password()" ) }

interface Convertable
{ fun getConverters() : Array<Converter> }
class NoConvertable : Convertable
{override fun getConverters () = Array<Converter>( 0, {NoConverter()} )}

class NoConverter () : Converter, PlaceHolder
{
   private val msg = "NoConverter"
   override fun canConvert (cls : Class<*>) = boolean( msg )
   override fun toJson (value : Any) = string( msg )
   override fun fromJson (jv : JsonValue) = unit( msg )
}

class WrongVersionException : Exception()
