package pen.eco

import java.lang.System
import java.io.File
import java.io.FileReader
import java.util.Properties
import java.util.Base64

actual fun slash () = File.separator
actual fun user_home () = System.getProperty( "user.home" )

actual fun loadConf (filename : String) : Map<String, String>
{
   val properties = Properties()
   val resultMap = HashMap<String, String>()

   try
   {
      properties.load(FileReader( filename ))

      for (key in properties.keys())
         resultMap.put(key as String, properties.get( key ) as String)
   }
   catch (e : Exception) {}

   return resultMap
}
