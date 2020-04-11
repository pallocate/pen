package pen.tests

import pen.Log

object Assertions
{
   fun assertTrue (condition : Boolean)
   {
      if (!condition)
      {
         Log.error( "Test failiure(Expected true)" )
         println( "Test failed!" )
      }
   }
}
