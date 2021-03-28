package pen.tests

import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object Patricia
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "patricia" }
   private val salt = "37163236602e39253bc8f013dea1d772acd2cd650cc4aa2d6bbfecd379d05e22".parseAsHex()

   val contact = KContact(
      3L,
      KContact.KInfo( "Patricia" ),
      KContact.KAddress(
         "patricia", "artysan", "da3d4511c9d8d2f88b5cfd99c4b17ff699de5972e74ebf6cd6d45fa275844297".parseAsHex()
      )
   )


   private val me by lazy {KMe( contact, salt )}

   fun irohaSigner () = me.irohaSigner( passwordProvider )

   fun user () = KUser( me ).apply {

      relations.add(KRelation( Contacts.artysan, Target.CONSUMPTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( Contacts.factory, Target.PRODUCTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( Contacts.newsbureau, Target.PRODUCTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( Contacts.university, Target.PRODUCTION ).apply {
         roles.add( Role.PROPOSER )
         roles.add( Role.DATA_SUBJECT )
      })

      relations.add(KRelation( Contacts.farmlands, Target.PRODUCTION ).apply {
            roles.add( Role.PROPOSER )
            roles.add( Role.DATA_SUBJECT )
            roles.add( Role.COUNCIL_SIGNER )
      })
   }
}
