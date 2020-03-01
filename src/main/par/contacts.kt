package pen.par

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import pen.Crypto
import pen.safePath
import pen.PasswordProvider
import pen.NoPasswordProvider

abstract class Contact
{
   abstract protected val _name : String
   abstract protected val _icon : String
   abstract val contactId : Long
   abstract val publicKey : ByteArray

   fun name () = _name.safePath()
   fun icon () = _icon.safePath()
}
class NoContact : Contact() {override val contactId=0L;override val publicKey=ByteArray(0);override protected val _name="";override protected val _icon =""}

/** Contact information. */
@Serializable
open class KContact (final override val contactId : Long = 0L,
                     @SerialName( "name" )
                     final override protected val _name : String = "",
                     @SerialName( "icon" )
                     final override protected val _icon : String = "",

                     final override val publicKey : ByteArray = ByteArray( 0 ),
                     val group : String = "") : Contact()
{
   fun copy () = KContact( contactId, _name, _icon, publicKey, group )
}

@Serializable
class KMe ( final override val contactId : Long,
            @SerialName( "name" )
            final override protected val _name : String = "",
            @SerialName( "icon" )
            final override protected val _icon : String = "",

            final override val publicKey : ByteArray = ByteArray( 0 ),
            val salt : ByteArray = ByteArray( 0 )) : Contact()
{
   companion object
   {
      fun factory (contactId : Long = 0L, name : String = "", icon : String = "", passwordProvider : PasswordProvider) : KMe
      {
         val salt = Crypto.randomBytes( Crypto.saltSize() )
         val pubKey = Crypto.getKey( passwordProvider, salt, Crypto::publicKey )
         return KMe( contactId, name, icon, pubKey, salt )
      }
   }
}
