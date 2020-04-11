package pen.tests

import pen.Log

object Assertions
{
   fun assertTrue (condition : Boolean)
   {
      if (!condition)
      {
         Log.error( "Test failed. Expected: true" )
         throw Exception( "Test failed!" )
      }
   }

   fun assertEquals (value : Any, reference : Any)
   {
      if (!(value == reference))
      {
         Log.error( "Test failed. Was: $value, Expected: $reference" )
         throw Exception( "Test failed!" )
      }
   }
}
