package pen

expect fun now () : Long

expect fun ByteArray.toHex () : String

expect fun libsodiumPath () : String
expect fun ed25519Sha3 (seed : ByteArray) : IrohaSigner

expect object Na
{ val sodium : Sodium }
