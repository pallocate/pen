package pen.eco

import java.lang.System
import java.util.Date
import java.text.SimpleDateFormat
import java.io.File
import java.io.FileOutputStream

actual fun log (message : String, severity : LogLevel)
{
   if (severity >= Log.level)
      KLogEvent( message, severity )
}

/** A log event. */
data class KLogEvent (val message : String, val severity : LogLevel)
{
   private var fileOutputStream : FileOutputStream? = null

   init
   {
      val dateFormat = SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" )
         val text = dateFormat.format( Date() ) + " ${severity.name}: $message\n"

      try
      {
         if (fileOutputStream == null)
         {
            val file = File( "app.log" )
            file.createNewFile()
            fileOutputStream = FileOutputStream( file, true )
         }

         if (fileOutputStream != null)
            fileOutputStream!!.write( text.toByteArray() )
         else
            System.err.println( text )
      }
      catch (e : Exception)
      { System.err.println( e.message ) }
   }
}
