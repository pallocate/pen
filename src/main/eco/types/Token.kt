package pen.eco.types

import pen.eco.Log
import pen.eco.Utils.encodeLong
import pen.eco.Utils.encodeInt
import pen.eco.Crypto
import pen.eco.Constants
import pen.eco.types.PasswordProvider

expect fun epoch_second () : Long

sealed class Token (val id : Long, val value : Int, val version : Int, val timestamp : Long,
val issuer : Long, val issuenceSignature : ByteArray, val userKey : ByteArray = ByteArray( 0 ),
val userKeySignature : ByteArray = ByteArray( 0 ), val account : Long = 0L, val accountSignature : ByteArray = ByteArray( 0 ))

class NoToken : Token(0L, 0, 0, 0L, 0L, ByteArray( 0 ))

/** A issued but not yet usable token. */
class KIssuedToken (id : Long, value : Int, issuer : Long, issuenceSignature : ByteArray) :
Token( id, value, Constants.VERSION, epoch_second(), issuer, issuenceSignature )
{
   init
   {Log.debug( "token issued [$id]" )}

   /** Sets the public key of the user of the token. */
   fun defineUser (publicKey : ByteArray, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : Token
   {
      val signature = Crypto.signatureOf( publicKey, passwordProvider, pkc_salt )

      return   if (signature.size == Crypto.publicSigningKeySize())
                  KCreditToken( this, publicKey, signature )
               else
               {
                  Log.warn( "define user failed!" )
                  NoToken()
               }
   }
}

/** A usable credit token. */
class KCreditToken (ist : KIssuedToken, userKey : ByteArray, userKeySignature : ByteArray) :
Token( ist.id, ist.value, ist.version, ist.timestamp, ist.issuer, ist.issuenceSignature,
userKey, userKeySignature )
{
   init
   {Log.debug( "token usable [$id]" )}

   /** Sets the account of the token and sets the state to USED. */
   fun use (productID : Long, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : Token
   {
      Log.debug( "using token [$id]" )
      val signature = Crypto.signatureOf(encodeLong( productID ), passwordProvider, pkc_salt)

      return   if (signature.size == Crypto.publicSigningKeySize())
                  KUsedToken( this, productID, signature )
               else
               {
                  Log.warn( "use token failed!" )
                  NoToken()
               }
   }
}

/** A used token. */
class KUsedToken (ct : KCreditToken, account: Long, accountSignature : ByteArray) : Token(
ct.id, ct.value, ct.version, ct.timestamp, ct.issuer, ct.issuenceSignature, ct.userKey,
ct.userKeySignature, account, accountSignature )
{
   fun account (userKey : ByteArray) : Token
   {
      Log.debug( "accounting token [$id]" )

      return   if (Crypto.verifySignatureOf( encodeLong( account ), accountSignature, userKey ))
                  KAccountedToken( this )
               else
               {
                  Log.warn( "accounting token failed!" )
                  NoToken()
               }
   }
}

/** A accounted token. */
class KAccountedToken (ut : KUsedToken) : Token( ut.id, ut.value, ut.version, ut.timestamp,
ut.issuer, ut.issuenceSignature, ut.userKey, ut.userKeySignature, ut.account, ut.accountSignature)
{
   init
   {Log.debug( "token accounted [$id]" )}
}
// val issuenceData = encodeLong( id ) + encodeInt( value ) + encodeLong( issuer ) + encodeLong( timestamp )
// issuenceSignature = Crypto.signatureOf( issuenceData, passwordProvider, pkc_salt )
