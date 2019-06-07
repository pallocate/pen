package pen.eco

import java.io.FileReader
import java.io.FileWriter
import com.beust.klaxon.Converter
import pen.eco.types.Convertable
import pen.eco.types.NoConverter

interface Settings
class NoSettings : Settings

interface SettingsValue

class KSettings () : Settings, Convertable
{
   companion object
   {
      val SERIALIZE                               = false
      val COMMON_PLACE_HOLDER                     = true
      val COMMON_CONVERSIONS                      = false
      val COMMON_FILES                            = true

      fun loadFromFile (filename : String) : KSettings
      {
         var ret = KSettings()

         try
         {
            val fileReader = FileReader( filename )
            val parseResult = Serializer.read<KSettings>( fileReader )

            if (parseResult != null && parseResult is KSettings)
               ret = parseResult
            else
               Log.err( "KSettings- load file failed!" )
         }
         catch (e : Exception)
         { Log.err( "KSettings- load file failed! ${e.message}" ) }

         return ret
      }
   }

   val debug = hashMapOf<String, Boolean>( "KAD_CREATE" to false, "KAD_SAVE_LOAD" to true, "SERIALIZE" to true )
   val supported_plugins : Map<String, String> = HashMap<String, String>()

   fun getValue (flag : SettingsValue) : Boolean
   {
      var ret = false

      if (flag is DebugValue)
      {
         val tmp = debug.get( flag.name )

         if (tmp != null)
            ret = tmp
      }

      return ret
   }

   fun saveToFile (filename : String)
   {
      val fileWriter = FileWriter( filename )
      Serializer.write( this, fileWriter )
   }

   override fun getConverters () = Array<Converter>( 0, {NoConverter()} )
}
