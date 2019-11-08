package pen.net.kad.messages

import pen.eco.Log

import pen.eco.Config

/** A simple message used for testing the system; Default message constructed if the message type sent is not available */
class KSimpleMessage () : Message
{
   var content = ""

   init
   { Log.debug( {"<SIMPLE>"}, Config.flag( "KAD_MSG_CREATE" )) }

   constructor (content : String) : this()
   {
      this.content = content
   }

   override fun code () = Codes.SIMPLE
}
