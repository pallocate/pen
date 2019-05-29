package pen.eco.common

/** A node in a merkle tree. */
interface MerkleNode
class NoMerkleNode : MerkleNode {}
open class KMerkleNode : MerkleNode
{var storedHash = ByteArray( 0 )}

interface Hashable
{ abstract fun hash () : ByteArray }

interface Identifiable
{ val id : Long }
class NoIdentifiable : Identifiable
{ override val id = 0L }

/** Implementing classes should provide a password every time the password function is called. */
interface PasswordProvider
{ fun password () : String }
class NoPasswordProvider : PasswordProvider, PlaceHolder
{ override fun password () = string( "NoPasswordProvider.password()" ) }

class WrongVersionException : Exception()
