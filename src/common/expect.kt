package pen

/* Check compabillity(changed to milli seconds) */
expect fun now () : Long
expect fun slash () : String

expect fun encode_b64 (bytes : ByteArray) : ByteArray
expect fun decode_b64 (encoded : ByteArray) : ByteArray

expect fun randomBytes (size : Int) : ByteArray

expect fun ByteArray.toHex () : String

/** Directory of the libsodium library. */
expect fun libsodiumDir () : String
