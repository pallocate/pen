package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object David
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "david" }
   private val salt = "317360b40f32aceff6a3f71d0d6b1fc8cdcebcfff67b16e3348ba5f378b2e549".parseAsHex()
   val crypto = KCrypto( passwordProvider, salt )

   fun user () = KUser( KMe(4L, KContactInfo( "David", crypto.pkSignatures().publicKey() ), salt) ).apply {

      relations.add(KRelation( ContactList.crowbeach, Target.CONSUMPTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( ContactList.university, Target.PRODUCTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( ContactList.hospital, Target.PRODUCTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( ContactList.farmlands, Target.PRODUCTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })
   }

   fun publicKey () = crypto.pkSignatures().publicKey().toHex()
}
// pk: "cf015fa97505062cdcc77d805fc4151b51eb78720a5fd0d8849759be162eb180"
