package pen.tests.eco.planning

import java.io.ByteArrayInputStream
import java.util.Random
import org.junit.jupiter.api.*
import pen.eco.common.WrongVersionException
import pen.eco.planning.Constants
import pen.eco.planning.Block
import pen.eco.planning.Product
import pen.eco.planning.codecs.BlockEncoder
import pen.eco.planning.codecs.BlockDecoder
import pen.tests.examples.Blocks

@DisplayName( "BlockDecoder test" )
class BlockDecoderTest
{
   @Test
   @DisplayName( "Fuzzing 100" )
   fun fuzz100 ()
   {
      val random = Random()
      val targetBlock = Block()
      val blockDecoder = BlockDecoder( targetBlock, 0 )

      for (i in 0..99)
      {
         val fuzz = ByteArray( random.nextInt( 2000 ) )
         random.nextBytes( fuzz )
         try
         { blockDecoder.decode(ByteArrayInputStream( fuzz )) }
         catch (e : WrongVersionException) {}
         catch (e : Exception)
         {Assertions.assertTrue( false )}
      }
   }

   @Test
   @DisplayName( "Using valid input" )
   fun usingValidInput ()
   {
      val targetBlock = Block()
      val encodedBlock = Blocks.SIGNATURE + BlockEncoder( Blocks.testProposal() ).encode()
      BlockDecoder( targetBlock, 0 ).decode(ByteArrayInputStream( encodedBlock ))

      Assertions.assertEquals( 3, targetBlock.header.level )
      Assertions.assertEquals( Constants.IS_PROPOSAL, targetBlock.header.flags )
      Assertions.assertArrayEquals( Blocks.SIGNATURE, targetBlock.signature )

      if (targetBlock.children.size > 1)
      {
         var blockNode = targetBlock.children[0]
          if (blockNode is Product)
            Assertions.assertEquals( 150131600L, blockNode.id )

         blockNode = targetBlock.children[1]
          if (blockNode is Product)
            Assertions.assertEquals( 27131505L, blockNode.id )
      }
   }
}
