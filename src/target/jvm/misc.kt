package pen.eco

import java.lang.System
import java.io.File
import java.util.Base64

actual fun slash () = File.separator
actual fun user_home () = System.getProperty( "user.home" )
