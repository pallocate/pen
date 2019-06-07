package pen.eco.types

import pen.eco.Log
import pen.eco.common.Crypto
import pen.eco.types.PasswordProvider

/** @param nrOfOptions Nr of options, DISMISS incluced. */
class Choise (var selected : Int = DISMISS, val nrOfOptions : Int = 2)
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
      signature = Crypto.signatureOf( toString().toByteArray(), passwordProvider, pkc_salt )
   }

   override fun toString () : String
   { return "" + voteID + votationID + choise.selected }
}
