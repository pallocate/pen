package pen.net.kad.messages

import pen.eco.Log

interface Message
{
   fun code () : Byte
}
class NoMessage : Message
{
   override fun code () = 0x00.toByte()
}
