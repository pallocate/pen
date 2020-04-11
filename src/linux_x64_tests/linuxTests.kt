import platform.posix.*
import pen.Log
import pen.tests.KCryptoTests
import pen.tests.KTopLevelFunTests

fun main ()
{
   Log.info( "Starting tests" )

   KTopLevelFunTests().nowSanityTest()
   KCryptoTests().testConstants()
}
