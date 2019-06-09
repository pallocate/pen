package pen.eco

import java.lang.System
import java.util.Date
import java.text.SimpleDateFormat
import java.io.File
import java.io.FileWriter

actual fun log (message : String, severity : LogLevel)
{
   if (severity >= Log.level)
      Logger.logMessage( message, severity )
}

/** Does the actual logging. */
internal object Logger
{
   private var fileWriter : FileWriter? = null

   fun logMessage (message : String, severity : LogLevel)
   {
      val dateFormat = SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" )
      val timestampedMessage = "${dateFormat.format( Date() )} ${severity.name}: $message\n"

      try
      {
         if (fileWriter == null)
         {
            val file = File( "app.log" )
            file.createNewFile()
            fileWriter = FileWriter( file, true )
         }
         fileWriter!!.write( timestampedMessage )
      }
      catch (e : Exception)
      { System.err.println( timestampedMessage ) }
   }
}
