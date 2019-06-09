package pen.eco.common

import java.io.File
import java.util.Base64
import java.security.MessageDigest
import pen.eco.Log
import pen.eco.Config
import pen.eco.Constants

actual fun encode_b64 (bytes : ByteArray) = Base64.getEncoder().encode( bytes )
actual fun decode_b64 (encoded : String) = Base64.getDecoder().decode( encoded.toByteArray() )
actual fun hash_md5 (bytes : ByteArray) = MessageDigest.getInstance( "MD5" ).digest( bytes )

actual fun create_dir (path : String)
{
   Log.debug( {"Creating directory $path"}, Config.flag( "COMMON_FILES" ))

   try
   { (File( path )).mkdirs() }
   catch (e : Exception)
   {Log.warn( "Directory create failed" )}
}
