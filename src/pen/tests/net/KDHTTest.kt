package pen.tests.net

import org.junit.jupiter.api.*
import pen.net.kad.dht.*

@DisplayName( "KDHT test" )
class KDHTTest
{
   val SOME_ID = "ASF456789475DS567461"
   val TEST_DATA = "Test data"
   val dht = KDHT()

   @Test
   @DisplayName( "Local store, content exists" )
   fun storeExists ()
   {
      val entry = KStorageEntry(KContent( SOME_ID, TEST_DATA ))
      val result1 = dht.store( entry )
      val result2 = dht.store( entry )                                          // Content already exists
      Assertions.assertTrue( result1 && !result2 )
   }

   @Test
   @DisplayName( "Local store/retrieve" )
   fun storeRetrieve ()
   {
      val entry = KStorageEntry(KContent( SOME_ID, TEST_DATA ))
      dht.store( entry )

      /* Crashes at KDht retrieve() when serializing(StorageEntry-KStorageEntry?) */
      val retrieved = dht.retrieve( entry.contentMetadata.key, entry.contentMetadata.hashCode() )

      if (retrieved is KStorageEntry)
         Assertions.assertEquals( entry.content.value, retrieved.content.value )
      else
         Assertions.assertTrue( false )
   }
}
