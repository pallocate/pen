package pen.eco

import com.sun.jna.Native
import pen.eco.Log
import pen.eco.Constants

actual fun sodiumInstance () : Sodium
{
   return Native.loadLibrary( SodiumName().filename, Sodium::class.java ) as Sodium
}

/** Constructs path name to the sodium library depending on OS and architecture. */
class SodiumName
{
   var filename = ""

   init
   {
      try
      {
         val os = System.getProperty( "os.name" )
         val arch = System.getProperty( "sun.arch.data.model" )

         val extension= if (os.contains( "win", true ))
                           ".dll"
                        else
                           if (os.contains( "nix", true ) || os.contains( "nux", true ))
                              ".so"
                           else
                           {
                              Log.err( "SodiumName- Unknown OS" )
                              ""
                           }
         filename = Constants.LIBSODIUM_DIR + Constants.SLASH + "libsodium" + arch + extension
      }
      catch (e : Exception)
      { Log.err( "SodiumName- Could not determine OS/architecture" ) }
   }
}


