package pen.eco

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue

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

interface Settings
class NoSettings : Settings
interface SettingsValue

interface PluginManager
// Authenticator

