package pen.utils

import pen.parseAsHex
import pen.PasswordProvider
import pen.eco.Target
import pen.par.*

object Artysan
{
   private val passwordProvider = object : PasswordProvider {override fun password () = "artysan" }
   private val salt = "21842d52a0339468907591d6989213e8cc5f53522a0af6ad17966c6891a887a3".parseAsHex()

   val contact = KContact(
      2L,
      KContact.KInfo( "Artysan" ),
      KContact.KAddress(
         "artysan", "commons", "daa12f254464355b1fd4c6cdb5d70e13f11fb00cb9612bd7f919f5ded83f7209".parseAsHex()
      )
   )


   private val me by lazy {KMe( contact, salt )}

   fun irohaSigner () = me.irohaSigner( passwordProvider )

   fun user () = KUser( me ).apply {
      relations.add(KRelation( Contacts.patricia, Target.CONSUMPTION ).apply {
         roles.add( Role.CONCEDER )
         roles.add( Role.DATA_CONTROLLER )
      })
   }
}
