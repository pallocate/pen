package pen

import platform.posix.time

actual fun now () = time( null )

/* Stubs */
actual fun randomBytes (size : Int) = ByteArray( size )
actual fun ByteArray.toHex () = "FFFFFF"
actual fun libsodiumPath () = "."

actual fun ed25519Sha3 (seed : ByteArray) : IrohaSignatory = VoidIrohaSignatory
actual object Na
{ actual val sodium : Sodium = VoidSodium }

actual fun loadConf (filename : String) : Map<String, String>
{
   val ret = HashMap<String, String>()

   ret.put( "PLATFORM", "5" )
   ret.put( "MERKLE_TREE", "5" )

   return ret
}
