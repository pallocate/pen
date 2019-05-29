package pen.tests.net

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.net.InetAddress
import org.junit.jupiter.api.*
import pen.net.kad.KKademliaNode
import pen.net.kad.dht.KContent
import pen.net.kad.dht.KGetParameter
import pen.net.kad.dht.KStorageEntry
import pen.net.kad.dht.KDHT
import pen.net.kad.node.KNode
import pen.net.kad.node.KNodeId
import pen.net.kad.routing.KRoutingTable
import pen.eco.KSerializer
import pen.net.kad.utils.KSerializableRoutingInfo

@DisplayName( "Serialization of variuos components." )
class ComponentsTests
{
   val OWNER = "Larsen"
   val KEY = ByteArray(20, { 0xFF.toByte() })

   @Test
   @DisplayName( "Serializing KKademliaNode" )
   fun serializingKKademliaNode ()
   {
      /* Creating KKademliaNode. */
      val kServiceNode = KKademliaNode()
      kServiceNode.ownerName = OWNER

      /* Writing KKademliaNode. */
      val fileWriter = FileWriter( "output0.json" )
      KSerializer.write( kServiceNode, fileWriter )

      /* Reading KKademliaNode. */
      val fileReader = FileReader( "output0.json" )
      val deserialized = KSerializer.read<KKademliaNode>( fileReader )

      /* Testing. */
      if (deserialized is KKademliaNode)
         Assertions.assertEquals( OWNER, deserialized.ownerName )
      else
         Assertions.assertTrue( false )
   }

   @Test
   @DisplayName( "Serializing KNode" )
   fun serializingKNode ()
   {
      /* Creating a KNode. */
      val kNodeID = KNodeId( KEY )
      val kNode = KNode( kNodeID, InetAddress.getLocalHost(), 49152 )

      /* Writing KNode. */
      val fileWriter = FileWriter( "output0.json" )
      KSerializer.write( kNode, fileWriter )

      /* Reading KNode. */
      val fileReader = FileReader( "output0.json" )
      val deserialized = KSerializer.read<KNode>( fileReader )

      /* Tests */
      if (deserialized is KNode)
      {
         Assertions.assertArrayEquals( kNodeID.keyBytes, deserialized.nodeId.keyBytes )
         Assertions.assertEquals( kNode.inetAddress.getHostAddress(), deserialized.inetAddress.getHostAddress() )
         Assertions.assertEquals( kNode.port, deserialized.port )
      }
      else
         Assertions.assertTrue( false )
   }

   @Test
   @DisplayName( "Serializing KRoutingTable" )
   fun serializingKRoutingTable ()
   {
      /* Creating KRoutingTable. */
      var kRoutingTable = KRoutingTable()
      val kNode = KNode()
      kNode.nodeId = KNodeId( KEY )
      kRoutingTable.initialize( kNode )

      /* Writing KRoutingTable. */
      val fileWriter = FileWriter( "output1.json" )
      KSerializer.write(KSerializableRoutingInfo( kRoutingTable ), fileWriter)

      /* Reading KRoutingTable. */
      val fileReader = FileReader( "output1.json" )
      val deserialized = KSerializer.read<KSerializableRoutingInfo>( fileReader )

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
   @DisplayName( "Serializing KDHT" )
   fun serializingKDHT ()
   {
      /* Creating KDHT. */
      val kDHT = KDHT()
      kDHT.initialize( OWNER )

      /* Writing KDHT. */
      val fileWriter = FileWriter( "output0.json" )
      KSerializer.write( kDHT, fileWriter )

      /* Reading KDHT. */
      val fileReader = FileReader( "output0.json" )
      val deserialized = KSerializer.read<KDHT>( fileReader )

      /* Testing. */
      if (deserialized is KDHT)
         Assertions.assertEquals( OWNER, deserialized.ownerName )
      else
         Assertions.assertTrue( false )
   }

   @Test
   @DisplayName( "Serializing KStorageEntry" )
   fun serializingKStorageEntry ()
   {
      val PAYLOAD = "Hello world!"

      /* Creating KContent. */
      val kStorageEntry = KStorageEntry(KContent( OWNER, PAYLOAD ))

      /* Writing KContent. */
      val fileWriter = FileWriter( "output0.json" )
      KSerializer.write( kStorageEntry, fileWriter )

      /* Reading KContent. */
      val fileReader = FileReader( "output0.json" )
      val deserialized = KSerializer.read<KStorageEntry>( fileReader )

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
