package pen.eco.common

import java.io.File
import java.io.FilenameFilter
import java.io.InputStream
import java.io.OutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.Serializable
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import pen.eco.common.PlaceHolder

/** Encapsulates file reading, to avoid null stuff (more Kotlin friendly).
  * @constructor Tries to open a file for input. */
class FileInput (filename : String)
{
   private var stream : FileInputStream? = null

   init
   {
      Log.debug( {"Opening file $filename"}, KSettings.COMMON_FILES )
      try
      { stream = FileInputStream( filename ) }
      catch (e : Exception)
      { Log.warn( "File open failed!" ) }
   }

   /** @return A InputStream that might be a NoInputStream */
   fun getInputStream () : InputStream
   {
      var ret : InputStream = NoInputStream()
      if (stream != null)
         ret = stream!!

      return ret
   }

   fun close () = stream?.close()
}

/** Encapsulates file writing, to avoid null stuff (more Kotlin friendly).
  * @constructor Tries to open a file for output. */
class FileOutput (filename : String)
{
   private var stream : FileOutputStream? = null

   init
   {
      Log.debug( {"Opening file $filename"}, KSettings.COMMON_FILES )
      try
      { stream = FileOutputStream( filename ) }
      catch (e : Exception)
      {Log.warn( "File open failed!" )}
   }

   /** @return A OutputStream that might be a NoOutputStream */
   fun getOutputStream () : OutputStream
   {
      var ret : OutputStream = NoOutputStream()
      if (stream != null)
         ret = stream!!

      return ret
   }

   fun close () = stream?.close()
}
/** A no value, meaning a InputStream could not be opened. */
class NoInputStream : InputStream(), PlaceHolder
{
   override fun read () : Int = int( "NoInputStream.read()" )
   override fun close () = unit( "NoInputStream.close()" )
}
/** A no value, meaning a OutputStream could not be opened. */
class NoOutputStream : OutputStream(), PlaceHolder
{
   override fun write (i : Int) = unit( "NoOutputStream.write()" )
   override fun close () = unit( "NoOutputStream.close()" )
}

object Directory
{
   /** Returns directory listing.
     * @param extension File name extension. */
   fun list (directory : String, extension : String = "") : Array<String>
   {
      var ret = Array<String>( 0, {""} )
      try
      {
         val dir = File( directory )
         if (dir.exists() && dir.isDirectory())
            if (extension == "")
               ret = dir.list()
            else
               ret = dir.list(NameFilter( extension ))
      }
      catch (e : Exception) {}

      return ret
   }

   fun create (path : String) : String
   {
      Log.debug( {"Creating directory $path"}, KSettings.COMMON_FILES )
      var ret = ""

      try
      {
         (File( path )).mkdirs()
         ret = path
      }
      catch (e : Exception)
      {Log.warn( "Directory create failed" )}

      return ret
   }

   class NameFilter (val extension : String) : FilenameFilter
   {override fun accept (dir : File, name : String) : Boolean = name.endsWith( "." + extension )}
}
