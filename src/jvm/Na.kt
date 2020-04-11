package pen

import com.sun.jna.Native
import com.sun.jna.Library
import pen.Log
import pen.Sodium
import pen.Constants

interface Na : Sodium, Library

actual fun sodiumInstance () : Sodium
{
   Log.debug( "Loading libsodium" )
   var sodium : Sodium = NoSodium()

   try
   {
      sodium = Native.loadLibrary( pathNa(), Na::class.java ) as Na
   }
   catch (t : Throwable)
   { Log.error( "Loading libsodium failed!" ) }

   return sodium
}

/** Constructs path name to the sodium library depending on OS and architecture. */
fun pathNa () : String
{
   var ret = ""

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
                           Log.error( "SodiumName- Unknown OS" )
                           ""
                        }
      ret = Constants.LIBSODIUM_DIR + Constants.SLASH + "libsodium" + arch + extension
   }
   catch (e : Exception)
   { Log.error( "SodiumName- Could not determine OS/architecture" ) }

   return ret
}
