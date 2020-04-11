package pen.tests

import pen.jsNow

class KActualImplementationsTests
{
   fun nowSanityTest ()
   {
      val now = jsNow()
      Assertions.assertTrue( now > 1584518400 && now < 1742198400 )
   }
}
