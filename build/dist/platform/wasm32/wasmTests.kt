import pen.Log
import pen.tests.KActualImplementationTests
import pen.tests.net.KCTBTests

fun wasmTests ()
{
   Log.info( "Starting tests" )

   KActualImplementationsTests().nowSanityTest()
   KCTBTests().addToken()
}

fun main (args: Array<String>)
{
   if (!args.isEmpty())
      wasmTests()
}