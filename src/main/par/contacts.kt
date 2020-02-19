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
   abstract val group : String

   fun name () = _name.safePath()
   fun icon () = _icon.safePath()
}
class NoContact : Contact()
{
   final override val contactId = 0L
   final override val publicKey = ByteArray( 0 )
   final override val group = ""
   final override protected val _name = ""
   final override protected val _icon = ""
}

/** Contact information. */
@Serializable
open class KContact (final override val contactId : Long = 0L,
                     @SerialName( "name" )
                     final override protected val _name : String = "",
                     final override val publicKey : ByteArray = ByteArray( 0 ),
                     final override val group : String = "",
                     @SerialName( "icon" )
                     final override protected val _icon : String = "") : Contact()
{
   fun copy () = KContact( contactId, _name, publicKey, group, _icon )
}

@Serializable
class KMe ( final override val contactId : Long = 0L,
            @SerialName( "name" )
            final override protected val _name : String = "",
            final override val publicKey : ByteArray = ByteArray( 0 ),
            final override val group : String = "",
            @SerialName( "icon" )
            final override protected val _icon : String = "") : Contact()
{
   val salt = Crypto.randomBytes( Crypto.saltSize() )

   fun keyMe (passwordProvider : PasswordProvider) =
      if (passwordProvider is NoPasswordProvider)
         this
      else
      {
         val pubKey = Crypto.getKey( passwordProvider, this.salt, Crypto::publicKey )
         KMe( contactId, _name, pubKey, group, _icon )
      }
}
