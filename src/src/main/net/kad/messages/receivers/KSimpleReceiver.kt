package pen.net.kad.messages.receivers

import pen.net.kad.messages.Message

/** Default receiver if none other is called */
class KSimpleReceiver : Receiver
{
   override fun receive (message : Message, conversationId : Int) {}
}
