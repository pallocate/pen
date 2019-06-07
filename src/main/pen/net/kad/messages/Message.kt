package pen.net.kad.messages

import pen.eco.Log
import pen.net.kad.utils.KInetAddressConverter
import pen.net.kad.utils.KNodeIdConverter

interface Message
{
   fun code () : Byte
}
class NoMessage : Message
{
   override fun code () = 0x00.toByte()
}
