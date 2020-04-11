package pen.tests

import pen.now

class KTopLevelFunTests
{
   fun nowSanityTest ()
   {
      Assertions.assertTrue( now() > 1584518400 && now() < 1742198400 )
   }
}
