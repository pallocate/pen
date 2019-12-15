package pen.net

/** A node in a merkle tree. */
interface MerkleNode
class NoMerkleNode : MerkleNode {}

open class KMerkleNode : MerkleNode
{var storedHash = ByteArray( 0 )}