package pen.eco.common

import java.io.Serializable
import pen.eco.common.PasswordProvider
import pen.eco.common.Crypto
import pen.eco.common.Log

/** An encrypted message. */
class Message (var content : ByteArray, val contactID : Long, val messageID : Long, passwordProvider : PasswordProvider,
pkc_salt : ByteArray, othersPublicKey : ByteArray) : Serializable
{
   init
   {
      Log.debug( "Creating message" )
      if (content.size == 0 || contactID <= 0 || messageID <= 0)
         Log.warn( "Create message failed! (inclomlete input)" )
      else
         content = Crypto.pkcEncrypt( content, passwordProvider, pkc_salt, othersPublicKey )
   }

   fun decrypt (passwordProvider : PasswordProvider, pkc_salt : ByteArray, othersPublicKey : ByteArray) : ByteArray
   {
      Log.debug( "Decrypting message" )
      var ret = ByteArray( 0 )

      ret = Crypto.pkcDecrypt( content, passwordProvider, pkc_salt, othersPublicKey )

      return ret
   }

   override fun toString () : String
   { return String( content ) }
}
