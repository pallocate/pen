package pen.net.kad.messages

import pen.eco.common.Log
import pen.eco.common.DebugValue
import pen.eco.common.Config.getSettings

/** A simple message used for testing the system; Default message constructed if the message type sent is not available */
class KSimpleMessage () : Message
{
   var content = ""

   init
   { Log.debug( {"<SIMPLE>"}, getSettings().getValue( DebugValue.MESSAGE_CREATE )) }

   constructor (content : String) : this()
   {
      this.content = content
   }

   override fun code () = Codes.SIMPLE
}
