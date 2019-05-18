package pen.eco.common

import java.util.Date
import java.text.SimpleDateFormat
import java.io.File
import java.io.FileOutputStream

interface Loggable
{
   abstract fun loggingName () : String

   fun log (message : String, execute : Boolean = true, severity : Log.Level = Log.Level.DEBUG)
   {
      if (execute)
         Log.log( loggingName() + "- " + message, severity )
   }
   fun log (message : () -> String, execute : Boolean = true, severity : Log.Level = Log.Level.DEBUG)
   {
      if (execute)
         Log.log( loggingName() + "- " + message(), severity )
   }
}

/** A simple logger. */
object Log
{
   enum class Level { QUIET, DEBUG, INFO, WARN, ERROR, CRITICAL }

   /** At what minimum level to log events. */
   var level = Level.WARN
   var listener : LogListener = NoLogListener()
   private var fileOutputStream : FileOutputStream? = null

   fun debug (messageFunction : () -> String, execute : Boolean = true)
   {
      if (execute)
         log( messageFunction(), Level.DEBUG )
   }
   fun info (messageFunction : () -> String, execute : Boolean = true)
   {
      if (execute)
         log( messageFunction(), Level.INFO )
   }
   fun warn (messageFunction : () -> String, execute : Boolean = true)
   {
      if (execute)
         log( messageFunction(), Level.WARN )
   }

   fun debug (msg : String) = log( msg, Level.DEBUG )
   fun info (msg : String) = log( msg, Level.INFO )
   fun warn (msg : String) = log( msg, Level.WARN )
   fun err (msg : String) = log( msg, Level.ERROR )
   fun critical (msg : String) = log( msg, Level.CRITICAL )

   internal fun log (message : String, severity : Level)
   {
      if (severity >= level && level != Level.QUIET)
      {
         val event = Event( message, severity )
         listener.eventLogged( event )
      }
   }

   /** A log event. */
   class Event (val message : String, val severity : Level = Level.INFO)
   {
      init
      {
         val dateFormat = SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" )
         val text = dateFormat.format( Date() ) + " ${severity.name}: $message\n"

         try
         {
            if (fileOutputStream == null)
            {
               val file = File( Config.LOG_FILE )
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

      override fun toString () = message
   }
}
