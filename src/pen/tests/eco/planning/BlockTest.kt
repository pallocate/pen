package pen.tests.eco.planning

import java.io.ByteArrayInputStream
import org.junit.jupiter.api.*
import pen.eco.planning.Block
import pen.eco.planning.codecs.BlockEncoder
import pen.eco.planning.codecs.BlockDecoder
import pen.eco.planning.codecs.BlockTreeEncoder
import pen.tests.examples.Blocks

@DisplayName( "Block test" )
class BlockTest
{
   var referenceBlock = Block()

   @BeforeEach
   fun init ()
   {
      referenceBlock = Blocks.testReferenceBlock()
   }

   @Test
   @DisplayName( "Empty block read-write" )
   fun emptyBlockRW ()
   {
      val emptyBlock = Block()
      val binaryBlock = BlockEncoder( emptyBlock ).encode()

      val newBlock = Block()
      BlockDecoder(newBlock, 0).decode(ByteArrayInputStream( Blocks.SIGNATURE + binaryBlock ))
      Assertions.assertArrayEquals( emptyBlock.hash, newBlock.hash )
      Assertions.assertEquals( emptyBlock.header.size, newBlock.header.size )
   }

   @Test
   @DisplayName( "Proposal read-write" )
   fun proposalRW ()
   {
      var proposal = Blocks.testProposal()
      val binaryProposal = BlockEncoder( proposal ).encode()
      val newBlock = Block()

      BlockDecoder(newBlock, 0).decode(ByteArrayInputStream( Blocks.SIGNATURE + binaryProposal ))
      Assertions.assertArrayEquals( proposal.hash, newBlock.hash )
      Assertions.assertEquals( proposal.header.size, newBlock.header.size )
   }

   @Test
   @DisplayName( "Reference tree read-write" )
   fun blockTreeRW ()
   {
      val blockTree = Blocks.testReferenceBlock()
      val binaryBlockTree = BlockEncoder( blockTree ).encode()
      val newBlock = Block()

      BlockDecoder(newBlock, 0).decode(ByteArrayInputStream( Blocks.SIGNATURE + binaryBlockTree ))
      Assertions.assertArrayEquals( blockTree.hash, newBlock.hash )
      Assertions.assertEquals( blockTree.header.size, newBlock.header.size )
   }

   @Test
   @DisplayName( "Reference tree equals test block tree" )
   fun testBlockTreeEqualsReference ()
   {
      val blockTree = Blocks.testBlockTree()
      val binaryBlockTree = BlockTreeEncoder( blockTree ).encode()
      val binaryReferenceBlock = BlockEncoder( referenceBlock ).encode()

      Assertions.assertArrayEquals( Blocks.SIGNATURE + binaryReferenceBlock, binaryBlockTree )
   }

   @Test
   @DisplayName( "Reference tree equals test block/blob tree" )
   fun testBlockBlobTreeEqualsReference ()
   {
      val blockBlobTree = Blocks.testBlockBlobTree()
      val binaryBlockBlobTree = BlockTreeEncoder( blockBlobTree ).encode()
      val binaryReferenceBlock = BlockEncoder( referenceBlock ).encode()

      Assertions.assertArrayEquals( Blocks.SIGNATURE + binaryReferenceBlock, binaryBlockBlobTree )
   }
}
