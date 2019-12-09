package pen.eco

import java.lang.System
import java.util.Date
import java.text.SimpleDateFormat
import java.io.File
import java.io.FileWriter

/** Does the actual logging. */
actual object Logger
{
   private var fileWriter : FileWriter? = null

   actual fun logMessage (message : String, severity : LogLevel)
   {
      if (severity <= Log.level && severity > LogLevel.QUIET)
      {
         val dateFormat = SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" )
         val timestampedMessage = "${dateFormat.format( Date() )} ${severity.name}: $message\n"

         try
         {
            fileWriter?.run {
               val file = File( "app.log" )
               file.createNewFile()
               fileWriter = FileWriter( file, true )
            }

            fileWriter?.run {
               write( timestampedMessage )
               flush()
            }
         }
         catch (e : Exception)
         { System.err.println( timestampedMessage ) }
      }
   }
}
