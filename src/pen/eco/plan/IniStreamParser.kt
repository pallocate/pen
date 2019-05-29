package pen.eco.plan

import java.io.InputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import pen.eco.Log

/** Parses ini from a InputStream. */
interface IniStreamParser
{
   abstract val HEADER_KEY : String
   abstract val ITEM_KEY : String
   /** @param map A HashMap of lower case value key pairs. */
   abstract fun headerSection (map : HashMap<String, String>)
   /** @param map A HashMap of lower case value key pairs. */
   abstract fun itemSection (map : HashMap<String, String>)
   private fun noSection (map : HashMap<String, String>) {}                     // Default function, do nothing

   /** Reads ini from stream. */
   fun read (inputStream : InputStream) : Boolean
   {
      Log.debug( "Reading ini input stream" )
      var success = true
      val bufferedReader = BufferedReader(InputStreamReader( inputStream ))

      try
      { parse( bufferedReader ) }
      catch (e : Exception)
      {
         Log.err( "Read ini stream failed!" )
         success = false
      }

      return success
   }

   private fun parse (bufferedReader : BufferedReader)
   {
      val map = HashMap<String, String>()
      var funCall : (HashMap<String, String>) -> Unit = this::noSection         // Declare a function variable
      var done = false

      while (!done)
      {
         var line = bufferedReader.readLine()                                   // Read a line
         if (line == null)
         {
            line = "[]"
            done = true
         }

         line = line.trim()

         val commentAt = line.indexOf( '#' )
         if (commentAt >= 0)
            line = line.substring( 0, commentAt ).trim()                        // Strip off comments

         if (line != "")
         {
            if (line.startsWith( '[' ) && line.endsWith( ']' ))                 // It´s a new section
            {
               funCall( map )                                                   // Invoke function
               map.clear()
               line = line.substring( 1, line.length - 1 )

               if (line == HEADER_KEY)
                  funCall = this::headerSection                                 // Point function variable to header function
               else
                  if (line == ITEM_KEY)
                     funCall = this::itemSection                                // Point function variable to item function
                  else
                     funCall = this::noSection                                  // Point function variable to do nothing
            }
            else                                                                // It´s not a new section
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
}
