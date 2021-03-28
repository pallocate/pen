package pen

//import java.lang.System
import java.util.Date
import java.text.SimpleDateFormat
import java.io.File
import java.io.FileWriter

/** Does the actual logging. */
actual object LogManager
{
   var logAgent : LogAgent = DefaultLogAgent()

   actual fun logMessage (message : String, severity : LogLevel) = logAgent.logMessage( message, severity )
}

interface LogAgent
{ fun logMessage (message : String, severity : LogLevel) }

class DefaultLogAgent : LogAgent
{
   private var logFilename = System.getenv( "PEN_LOG" ) ?: "app.log"
   private var fileWriter : FileWriter? = null

   override fun logMessage (message : String, severity : LogLevel)
   {
      if (severity <= Log.level && severity > LogLevel.QUIET)
      {
         val dateFormat = SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" )
         val timestampedMessage = "${dateFormat.format( Date() )} ${severity.name}: $message\n"

         try
         {
            if (fileWriter == null)
            {
               val file = File( logFilename )
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
