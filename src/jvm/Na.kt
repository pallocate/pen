package pen

import com.sun.jna.Native
import com.sun.jna.Library
import pen.LogLevel.ERROR

interface SodiumLibrary : Sodium, Library

actual object Na
{
   actual val sodium = loadSodiumLibrary()

   private fun loadSodiumLibrary () : Sodium
   {
      Log.log("Loading Sodium library", Config.trigger( "FILES" ))
      var sodium : Sodium = VoidSodium()

      try
      {
         sodium = Native.load( pathNa(), SodiumLibrary::class.java ) as SodiumLibrary
         //sodium = System.loadLibrary( pathNa() ) as SodiumLibrary
      }
      catch (t : Throwable)
      {
         Log.log( "Loading Sodium library failed! (${pathNa()})", Config.trigger( "FILES" ), ERROR )
   //      t.printStackTrace()
      }

      return sodium
   }

   /** Constructs path name to the sodium library depending on OS and architecture. */
   private fun pathNa () : String
   {
      var ret = ""

      try
      {
         val arch = System.getProperty( "sun.arch.data.model" )

         val extension= System.getProperty( "os.name" ).let {

            if (it.contains( "win", true ))
                  ".dll"
               else
                  if (it.contains( "nix", true ) || it.contains( "nux", true ))
                     ".so"
                  else
                  {
                     Log.error( "SodiumName- Unknown OS" )
                     ""
                  }
         }

         ret = libsodiumPath() + arch + extension
      }
      catch (e : Exception)
      { Log.error( "SodiumName- Could not determine OS/architecture" ) }

      return ret
   }
}
