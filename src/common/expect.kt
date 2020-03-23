package pen

expect fun now () : Long
expect fun slash () : String

expect fun encode_b64 (bytes : ByteArray) : ByteArray
expect fun decode_b64 (encoded : ByteArray) : ByteArray
expect fun hash_md5 (bytes : ByteArray) : ByteArray
expect fun createDir (path : String)
expect fun pluginInstance (className : String) : Plugin
expect fun sodiumInstance () : Sodium
