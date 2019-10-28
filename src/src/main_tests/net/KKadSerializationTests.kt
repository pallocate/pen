package pen.tests.net

import java.net.InetAddress
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import pen.eco.Filer
import pen.net.kad.KKademliaNode
import pen.net.kad.dht.KContent
import pen.net.kad.dht.KGetParameter
import pen.net.kad.dht.KStorageEntry
import pen.net.kad.dht.KDHT
import pen.net.kad.node.KNode
import pen.net.kad.node.KNodeId
import pen.net.kad.routing.KRoutingTable
import pen.net.kad.utils.KSerializableRoutingInfo

class KKadSerializationTests
{
   val OWNER = "Larsen"
   val KEY = ByteArray(20, { 0xFF.toByte() })

   @Test
   fun `Serializing KKademliaNode` ()
   {
      /* Creating KKademliaNode. */
      val kServiceNode = KKademliaNode()
      kServiceNode.ownerName = OWNER

      /* Writing KKademliaNode. */
      Filer.write( kServiceNode, "dist/output" )

      /* Reading KKademliaNode. */
      val deserialized = Filer.read<KKademliaNode>( "dist/output" )

      /* Testing. */
      if (deserialized is KKademliaNode)
         Assertions.assertEquals( OWNER, deserialized.ownerName )
      else
         Assertions.assertTrue( false )
   }

   @Test
   fun `Serializing KNode` ()
   {
      /* Creating a KNode. */
      val kNodeID = KNodeId( KEY )
      val kNode = KNode( kNodeID, InetAddress.getLocalHost(), 49152 )

      /* Writing KNode. */
      Filer.write( kNode, "dist/output" )

      /* Reading KNode. */
      val deserialized = Filer.read<KNode>( "dist/output" )

      /* Tests */
      if (deserialized is KNode)
      {
         Assertions.assertArrayEquals( KEY, deserialized.nodeId.keyBytes )
         Assertions.assertEquals( kNode.inetAddress.getHostAddress(), deserialized.inetAddress.getHostAddress() )
         Assertions.assertEquals( kNode.port, deserialized.port )
      }
      else
         Assertions.assertTrue( false )
   }

   @Test
   fun `Serializing KRoutingTable` ()
   {
      /* Creating KRoutingTable. */
      var kRoutingTable = KRoutingTable()
      val kNode = KNode()
      kNode.nodeId = KNodeId( KEY )
      kRoutingTable.initialize( kNode )

      /* Writing KRoutingTable. */
      Filer.write(KSerializableRoutingInfo( kRoutingTable ), "dist/output")

      /* Reading KRoutingTable. */
      val deserialized = Filer.read<KSerializableRoutingInfo>( "dist/output" )

      /* Testing. */
      if (deserialized is KSerializableRoutingInfo)
      {
         kRoutingTable = deserialized.toRoutingTable()
         Assertions.assertArrayEquals( KEY, kRoutingTable.node.nodeId.keyBytes )
      }
      else
         Assertions.assertTrue( false )
   }

   @Test
   fun `Serializing KDHT` ()
   {
      /* Creating KDHT. */
      val kDHT = KDHT()
      kDHT.initialize( OWNER )

      /* Writing KDHT. */
      Filer.write( kDHT, "dist/output" )

      /* Reading KDHT. */
      val deserialized = Filer.read<KDHT>( "dist/output" )

      /* Testing. */
      if (deserialized is KDHT)
         Assertions.assertEquals( OWNER, deserialized.ownerName )
      else
         Assertions.assertTrue( false )
   }

   @Test
   fun `Serializing KStorageEntry` ()
   {
      val PAYLOAD = "Hello world!"

      /* Creating KContent. */
      val kStorageEntry = KStorageEntry(KContent( OWNER, PAYLOAD ))

      /* Writing KContent. */
      Filer.write( kStorageEntry, "dist/output" )

      /* Reading KContent. */
      val deserialized = Filer.read<KStorageEntry>( "dist/output" )

      /* Testing. */
      if (deserialized is KStorageEntry)
      {
         Assertions.assertEquals( OWNER, deserialized.content.ownerName )
         Assertions.assertEquals( PAYLOAD, deserialized.content.value )
      }
      else
         Assertions.assertTrue( false )
   }
}
