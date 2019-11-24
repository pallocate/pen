package pen.net

import java.io.Serializable
import pen.eco.Log
import pen.eco.Crypto
import pen.eco.types.PasswordProvider

/** An encrypted message. Message will probably move to some other package in the future. */
class Message (var content : ByteArray, val contactId : Long, val messageId : Long, passwordProvider : PasswordProvider,
pkcSalt : ByteArray, othersPublicKey : ByteArray) : Serializable
{
   init
   {
      Log.debug( "Creating message" )
      if (content.size == 0 || contactId <= 0 || messageId <= 0)
         Log.warn( "Create message failed! (inclomlete input)" )
      else
         content = Crypto.pkcEncrypt( content, passwordProvider, pkcSalt, othersPublicKey )
   }

   fun decrypt (passwordProvider : PasswordProvider, pkcSalt : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      Log.debug( "Decrypting message" )
      var ret = ByteArray( 0 )

      ret = Crypto.pkcDecrypt( content, passwordProvider, pkcSalt, othersPublicKey )

      return ret
   }

   override fun toString () : String
   { return String( content ) }
}
