package pen.net.kad.messages.receivers

import pen.eco.Log
import pen.eco.types.PlaceHolder

import pen.eco.Config
import pen.net.kad.messages.Message
import pen.net.kad.messages.Codes

/** A receiver waits for incoming messages and perform some action when the message is received */
interface Receiver
{
   fun receive (message : Message, conversationId : Int)

   /** If no reply is received in `MessageServer.TIMEOUT`, KServer calls this method. */
   fun timeout (conversationId : Int) = Log.debug({"message timeout"}, Config.flag( "KAD_RECEIVER_TIMEOUT" ))
}
class NoReceiver : Receiver, PlaceHolder
{
   override fun receive (message : Message, conversationId : Int) = unit( "NoReceiver received a message" )
}
