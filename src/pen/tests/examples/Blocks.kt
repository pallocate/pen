package pen.tests.examples

import pen.eco.planning.Constants
import pen.eco.common.FileOutput
import pen.eco.common.NoOutputStream
import pen.eco.planning.Proposal
import pen.eco.planning.Block
import pen.eco.planning.Blob
import pen.eco.planning.Product
import pen.eco.planning.VerseProduct
import pen.eco.planning.codecs.XMLBlockEncoder
import pen.eco.planning.codecs.BlockEncoder

object Blocks
{
   val SIGNATURE = "0123456789012345678901234567890123456789012345678901234567890123".toByteArray()

   fun testProposal () : Proposal
   {
      val ret                                 = Proposal()
      ret.header.apply(
      {
         level                                = 3
         flags                                = Constants.IS_PROPOSAL
         id                                   = 212
         link                                 = 0L
         iteration                            = 0
         version                              = 1
         timestamp                            = 0L
      })

      val product = Product()
      product.id = 150131600L
      product.qty = 12

      val verseProduct = VerseProduct()
      verseProduct.apply(
      {
         id = 27131505L
         qty = 1
         name = "Pneumatic drill"
         desc = "Heavy pneumatic drill"
         amount = 1F
         unit = "piece"
         down = 5
         up = 5
         price = 100000L
         analogue = "false"
      })

      /* Adding a Product and a VerseProduct */
      ret.children.add( product )
      ret.children.add( verseProduct )

      return ret
   }

   /** A simple block tree built layer-upon-layer. This is the normal way of building the block trees. */
   fun testReferenceBlock () : Block            //         b0
   {                                            //      __/|\___
      val b0 = Block()                          //     b1  b2   b3
      val b1 = Block()                          //     |       /|\.
      val b2 = Block()                          //     b4     p p p
      val b3 = Block()                          //    /|\.
      val b4 = Block()                          //   p p p

      b2.apply(
      {
         header.id = 2L
         header.timestamp = 1234L
         signature = SIGNATURE
      })
      b3.apply(
      {
         header.id = 3L
         header.setFlag( Constants.IS_PROPOSAL )
         header.timestamp = 1234L
         children.add( Product() )
         children.add( Product() )
         children.add( Product() )
      })
      b4.apply(
      {
         val product = Product()
         product.id = 7
         children.add( product )
         children.add( Product() )
         children.add( Product() )
         header.id = 4L
         header.setFlag( Constants.IS_PROPOSAL )
         header.timestamp = 1234L
      })
      b1.apply(
      {
         header.id = 1L
         header.timestamp = 1234L
         children.add( Blob(SIGNATURE + BlockEncoder( b4 ).encode()) )
      })
      b0.apply(
      {
         header.id = 0L
         header.timestamp = 1234L
         children.add( Blob(SIGNATURE + BlockEncoder( b1 ).encode()) )
         children.add( Blob(SIGNATURE + BlockEncoder( b2 ).encode()) )
         children.add( Blob(SIGNATURE + BlockEncoder( b3 ).encode()) )
      })
//      planning.Utils.writeBlockXML( b0, "testReferenceBlock.xml" )
      return b0
   }

   /** Same tree but not flattened by blobbing. */
   fun testBlockTree () : Block
   {
      val b0 = Block()
      val b1 = Block()
      val b2 = Block()
      val b3 = Block()
      val b4 = Block()

      b0.apply(
      {
         header.id = 0L
         header.timestamp = 1234L
         signature = SIGNATURE                                                  // Root node signature
         b0.children.add( b1 )
         b0.children.add( b2 )
         b0.children.add( b3 )
      })
      b1.apply(
      {
         header.id = 1L
         header.timestamp = 1234L
         signature = SIGNATURE
         children.add( b4 )
      })
      b2.apply(
      {
         header.id = 2L
         header.timestamp = 1234L
         signature = SIGNATURE
      })
      b3.apply(
      {
         header.id = 3L
         header.setFlag( Constants.IS_PROPOSAL )
         header.timestamp = 1234L
         signature = SIGNATURE
         children.add( Product() )
         children.add( Product() )
         children.add( Product() )
      })
      b4.apply(
      {
         val product = Product()
         product.id = 7
         children.add( product )
         children.add( Product() )
         children.add( Product() )
         header.id = 4L
         header.setFlag( Constants.IS_PROPOSAL )
         header.timestamp = 1234L
         signature = SIGNATURE
      })

      return b0
   }

   /** Same tree but mixed mode, blocks and blobs. */
   fun testBlockBlobTree () : Block
   {
      val b0 = Block()
      val b1 = Block()
      val b2 = Block()
      val b3 = Block()
      val b4 = Block()

      b1.apply(
      {
         header.id = 1L
         header.timestamp = 1234L
         signature = SIGNATURE
      })
      b2.apply(
      {
         header.id = 2L
         header.timestamp = 1234L
         signature = SIGNATURE
      })
      b3.apply(
      {
         header.id = 3L
         header.setFlag( Constants.IS_PROPOSAL )
         header.timestamp = 1234L
         signature = SIGNATURE
         children.add( Product() )
         children.add( Product() )
         children.add( Product() )
      })
      b4.apply(
      {
         val product = Product()
         product.id = 7
         children.add( product )
         children.add( Product() )
         children.add( Product() )
         header.id = 4L
         header.setFlag( Constants.IS_PROPOSAL )
         header.timestamp = 1234L
      })

      b1.children.add( Blob(SIGNATURE + BlockEncoder( b4 ).encode()) )
      b0.apply(
      {
         header.id = 0L
         header.timestamp = 1234L
         signature = SIGNATURE
         children.add( b1 )
         children.add( Blob(SIGNATURE + BlockEncoder( b2 ).encode()) )
         children.add( b3 )
      })

      return b0
   }
}
