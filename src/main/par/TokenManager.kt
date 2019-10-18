package pen.par

import pen.eco.types.PasswordProvider

interface TokenManager
{
   fun issueToken (value : Int, votation : Votation, councilId : Long, passwordProvider : PasswordProvider, pkc_salt : ByteArray) : Long
}
class NoTokenManager : TokenManager
{
   override fun issueToken (value : Int, votation : Votation, councilId : Long, passwordProvider : PasswordProvider, pkc_salt : ByteArray) = 0L
}
