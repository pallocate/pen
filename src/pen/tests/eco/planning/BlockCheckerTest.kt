package pen.tests.eco.planning

import org.junit.jupiter.api.*
import pen.eco.planning.codecs.BlockTreeEncoder
import pen.eco.planning.BlockChecker
import pen.eco.planning.Block
import pen.tests.examples.Blocks

@DisplayName( "BlockChecker test" )
class BlockCheckerTest
{
   var block = Block()

   @Test
   @DisplayName( "Proposal check" )
   fun proposalCheck ()
   {
      block = Blocks.testProposal()
      BlockTreeEncoder.sizesAndHashes( block )
      Assertions.assertTrue(BlockChecker.check( block, 0 ))
      Assertions.assertTrue(BlockChecker.check( block, 2))
   }

   @Test
   @DisplayName( "Block check" )
   fun blockCheck ()
   {
      block = Blocks.testReferenceBlock()
      BlockTreeEncoder.sizesAndHashes( block )
      Assertions.assertTrue(BlockChecker.check( block, 0 ))
      Assertions.assertTrue(BlockChecker.check( block, 2))
   }

   @Test
   @DisplayName( "Block/Blob check" )
   fun blockBlobCheck ()
   {
      block = Blocks.testBlockBlobTree()
      BlockTreeEncoder.sizesAndHashes( block )
      Assertions.assertTrue(BlockChecker.check( block, 0 ))
      Assertions.assertTrue(BlockChecker.check( block, 2))
   }
}
