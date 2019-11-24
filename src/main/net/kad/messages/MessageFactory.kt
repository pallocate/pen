package pen.net.kad.messages

import java.io.InputStream
import pen.eco.Loggable

import pen.eco.Config
import pen.net.kad.messages.Message
import pen.net.kad.messages.NoMessage
import pen.net.kad.utils.KMessageSerializer

/** Handles creating messages and receivers */
object MessageFactory : Loggable
{
   fun createMessage (code : Byte, inputStream : InputStream) : Message
   {
      var ret : Message = NoMessage()

      val deserialized = when (code)
      {
         Codes.ACKNOWLEDGE                -> KMessageSerializer.read<KAcknowledgeMessage>( inputStream )
         Codes.CONNECT                    -> KMessageSerializer.read<KConnectMessage>( inputStream )
         Codes.CONTENT                    -> KMessageSerializer.read<KContentMessage>( inputStream )
         Codes.FIND_VALUE                 -> KMessageSerializer.read<KFindValueMessage>( inputStream )
         Codes.FIND_NODE                  -> KMessageSerializer.read<KFindNodeMessage>( inputStream )
         Codes.FIND_NODE_REPLY            -> KMessageSerializer.read<KFindNodeReply>( inputStream )
         Codes.STORE                      -> KMessageSerializer.read<KStoreMessage>( inputStream )
         Codes.SIMPLE                     -> KMessageSerializer.read<KSimpleMessage>( inputStream )
         else                             ->
         {
            log({"Invalid message code: $code"}, Config.trigger( "KAD_MSG_CREATE" ))
            NoMessage()
         }
      }

      if (deserialized is Message)
         ret = deserialized

      return ret
   }
   override fun originName () = "MessageFactory"
}
