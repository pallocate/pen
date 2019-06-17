package pen.eco.types

import java.time.Instant
import pen.eco.Log
import pen.eco.Utils
import pen.eco.common.Crypto
import pen.eco.Constants
import pen.eco.types.PasswordProvider

sealed class Token
class NoToken : Token()

/** Implementation of a credit token. */
class KToken (val id : Long, val value : Int, val issuer : Long, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : Token()
{
   /** The possible states of the token. */
   enum class States { ISSUED, USABLE, USED, ACCOUNTED }

   val version                                  = Constants.VERSION
   val timestamp                                = Instant.now().getEpochSecond()
   /** Council issuence signature. */
   val issuenceSignature : ByteArray

   /** The state of the token. */
   var state                                    = States.ISSUED
      private set
   /** User public key. */
   var userKey                                  = ByteArray( 0 )
      private set
   /** Councils user public key signature. */
   var userKeySignature                         = ByteArray( 0 )
      private set
   /** Account number. */
   var account                                  = -1L
      private set
   /** User account number signature. */
   var accountSignature                         = ByteArray( 0 )
      private set

   init
   {
      Log.debug( "Token issuence" )
      val issuenceData = id.toBigInteger().toByteArray() + value.toBigInteger().toByteArray() +
      issuer.toBigInteger().toByteArray() + timestamp.toBigInteger().toByteArray()

      issuenceSignature = Crypto.signatureOf( issuenceData, passwordProvider, pkc_salt )
   }

   /** Sets the public key of the user of the token and sets the state to USABLE. */
   fun defineUser (publicKey : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray)
   {
      Log.debug( "Defining token user" )
      if (state == States.ISSUED)
      {
         userKey = publicKey
         userKeySignature = Crypto.signatureOf( publicKey, passwordProvider, pkc_salt )
         state = States.USABLE
      }
      else
         Log.warn( "Define token user failed! (should be in ISSUED state)" )
   }

   /** Sets the account of the token and sets the state to USED. */
   fun use (productID : Long, passwordProvider : PasswordProvider, pkc_salt : ByteArray)
   {
      Log.debug( "Using token" )
      if (state == States.USABLE)
      {
         account = productID
         accountSignature = Crypto.signatureOf( productID.toBigInteger().toByteArray(), passwordProvider, pkc_salt )
         state = States.USED
      }
      else
         Log.warn( "Use token failed! (should be in USABLE state)" )

   }

   /** Verifies account signature and sets the state to ACCOUNTED. */
   fun account () : Boolean
   {
      Log.debug( "Accounting token" )
      var success = false
      if (state == States.USED)
      {
         if (Crypto.verifySignatureOf( account.toBigInteger().toByteArray(), accountSignature, userKey ))
         {
            state = States.ACCOUNTED
            success = true
         }
         else
            Log.warn( "Accounting token failed! (signature verification failiure)" )
      }
      else
         Log.warn( "Accounting token failed! (should be in USED state)" )

      return success
   }
}
