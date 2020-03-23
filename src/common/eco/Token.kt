package pen.eco

import pen.Log
import pen.Crypto
import pen.Constants
import pen.PasswordProvider
import pen.now

sealed class Token (val id : Long, val value : Int, val version : Int, val timestamp : Long, val issuer : Long,
val issuenceSignature : ByteArray, val userKey : ByteArray = ByteArray( 0 ), val userKeySignature : ByteArray =
ByteArray( 0 ), val account : Long = 0L, val accountSignature : ByteArray = ByteArray( 0 ))
class NoToken : Token(0L, 0, 0, 0L, 0L, ByteArray( 0 ))

/** A issued but not yet usable token. */
class KIssuedToken (id : Long, value : Int, issuer : Long, issuenceSignature : ByteArray) :
Token( id, value, Constants.VERSION, now(), issuer, issuenceSignature ) {}

/** A usable credit token. */
class KCreditToken (issuedToken : KIssuedToken, userKey : ByteArray, userKeySignature : ByteArray) : Token( issuedToken.id,
issuedToken.value, issuedToken.version, issuedToken.timestamp, issuedToken.issuer, issuedToken.issuenceSignature,
userKey, userKeySignature ) {}

/** A used token. */
class KUsedToken (creditToken : KCreditToken, account : Long, accountSignature : ByteArray) : Token( creditToken.id,
creditToken.value, creditToken.version, creditToken.timestamp, creditToken.issuer, creditToken.issuenceSignature,
creditToken.userKey, creditToken.userKeySignature, account, accountSignature ) {}

/** A accounted token. */
class KAccountedToken (usedToken : KUsedToken) : Token( usedToken.id, usedToken.value, usedToken.version,
usedToken.timestamp, usedToken.issuer, usedToken.issuenceSignature, usedToken.userKey, usedToken.userKeySignature,
usedToken.account, usedToken.accountSignature ) {}

// val issuenceData = encodeLong( id ) + encodeInt( value ) + encodeLong( issuer ) + encodeLong( timestamp )
// issuenceSignature = Crypto.signatureOf( issuenceData, passwordProvider, pkcSalt )
