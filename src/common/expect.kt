package pen

/* Check compabillity(changed to milli seconds) */
expect fun now () : Long
expect fun slash () : String

expect fun ByteArray.toHex () : String

expect fun randomBytes (size : Int) : ByteArray
