package pen.par

import com.sun.jna.Native
import com.sun.jna.Library
import pen.Log
import pen.libsodiumDir
import pen.Constants

interface Na : Sodium, Library

actual fun sodiumInstance () : Sodium
{
   Log.debug( "Loading libsodium" )
   var sodium : Sodium = VoidSodium()

   try
   {
      sodium = Native.load( pathNa(), Na::class.java ) as Na
      //sodium = System.loadLibrary( pathNa() ) as Na
   }
   catch (t : Throwable)
   {
      Log.error( "Loading libsodium failed! (${pathNa()})" )
//      t.printStackTrace()
   }

   return sodium
}

/** Constructs path name to the sodium library depending on OS and architecture. */
internal fun pathNa () : String
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
      ret = libsodiumDir() + Constants.SLASH + "libsodium" + arch + extension
   }
   catch (e : Exception)
   { Log.error( "SodiumName- Could not determine OS/architecture" ) }

   return ret
}
