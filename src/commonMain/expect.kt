package pen

expect fun now () : Long
expect fun randomBytes (size : Int) : ByteArray
expect fun ByteArray.toHex () : String
expect fun libsodiumPath () : String

expect fun ed25519Sha3 (seed : ByteArray) : IrohaSignatory
expect object Na
{ val sodium : Sodium }
