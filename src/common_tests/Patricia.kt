package pen.tests

import pen.toHex
import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object Patricia
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "patricia" }
   private val salt = "37163236602e39253bc8f013dea1d772acd2cd650cc4aa2d6bbfecd379d05e22".parseAsHex()
   val crypto = KCrypto( passwordProvider, salt )

   fun user () = KUser( KMe(3L, KContactInfo( "Patricia", crypto.pkSignatures().publicKey() ), salt) ).apply {

      relations.add(KRelation( ContactList.artysan, Target.CONSUMPTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( ContactList.factory, Target.PRODUCTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( ContactList.newsbureau, Target.PRODUCTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( ContactList.university, Target.PRODUCTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( ContactList.farmlands, Target.PRODUCTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
            roles.add( Role.COUNCIL_SIGNER )
      })
   }

   fun publicKey () = crypto.pkSignatures().publicKey().toHex()
}
// pk: "ebbd39c028069d05328155a5485ee49a129382c49dffb847e88d20864d492ac0"
