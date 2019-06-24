package pen.eco.types

import pen.eco.Log
import pen.eco.Crypto
import pen.eco.Utils
import pen.eco.types.PasswordProvider

/** @param nrOfOptions Nr of options, DISMISS incluced. */
class Choise (val selected : Int, val nrOfOptions : Int = 2)
{
   companion object
   {
      const val DISMISS = 0
      const val APPROVE = 1
   }
}

/** A vote in a votation. */
class KVote (val votationID : Long, val voteID : Long, val choise : Choise, passwordProvider : PasswordProvider, pkc_salt : ByteArray)
{
   val signature : ByteArray

   init
   {
      Log.debug( "Signing vote \"$voteID\"" )
      signature = Crypto.signatureOf(Utils.stringToByteArray( toString() ), passwordProvider, pkc_salt)
   }

   override fun toString () : String
   { return "${voteID}${votationID}${choise.selected}" }
}
