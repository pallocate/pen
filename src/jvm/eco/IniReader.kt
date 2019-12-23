package pen.eco

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import pen.Config
import pen.LogLevel.WARN
import pen.Loggable

   /** Reads and parses a 'ini' file and passes the the different sections to the implementing class as HashMap´s. */
interface IniReader : Loggable
{
   abstract val HEADER_KEY : String
   abstract val ITEM_KEY : String
   abstract fun headerSection (map : HashMap<String, String>)
   abstract fun itemSection (map : HashMap<String, String>)
   private fun noSection (map : HashMap<String, String>) {}                     // Default function, do nothing

   fun read (filename : String)
   {
      log("reading ini-file \"$filename\"", Config.trigger( "SAVE_LOAD" ))

      try
      {
         var lines : List<String> = Files.readAllLines(Paths.get( filename ))

         lines = lines + "[]"                                                   // Added to the end to make the last section execute
         parse( lines.listIterator() )
      }
      catch (e : Exception)
      { log("read file failed!", Config.trigger( "SAVE_LOAD" ), WARN) }
   }

   private fun parse (iter : ListIterator<String>)
   {
      val map = HashMap<String, String>()
      var funCall : (HashMap<String, String>) -> Unit = this::noSection         // A function variable
      var line : String

      while (iter.hasNext())
      {
         line = iter.next().trim()                                              // Read a line from the list
         val commentAt = line.indexOf( '#' )

         if (commentAt >= 0)
            line = line.substring( 0, commentAt ).trim()                        // Strip off comments

         if (line != "")
         {
//            line = line.filterNot { it <= 180.toChar() }                        // Filter out some unsecure characters

            if (line.startsWith( '[' ) && line.endsWith( ']' ))                 // It´s a section
            {
               funCall( map )                                                   // Invoke function
               line = line.substring( 1, line.length - 1 )

               if (line == HEADER_KEY)
                  funCall = this::headerSection                                 // Point function variable to header function
               else
                  if (line == ITEM_KEY)
                     funCall = this::itemSection                                // Point function variable to item function
                  else
                     funCall = this::noSection                                  // Point function variable to nothing
            }
            else                                                                // It´s not a section
            {
               val delimiterAt = line.indexOf( ':' )
               if (line.length > delimiterAt + 1 && delimiterAt > 0)
               {
                  val key = line.substring( 0, delimiterAt ).trim().toLowerCase() // Extract key
                  val value = line.substring( delimiterAt + 1 ).trim()            // Extract value
                  map.put( key, value )
               }
            }
         }
      }
   }
   override fun originName () = "IniReader"
}
