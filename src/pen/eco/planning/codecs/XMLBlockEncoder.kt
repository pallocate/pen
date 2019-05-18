package pen.eco.planning.codecs

import java.io.ByteArrayOutputStream
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter
import pen.eco.common.Log
import pen.eco.common.BlockNode
import pen.eco.planning.Block
import pen.eco.planning.Blob
import pen.eco.planning.Product
import pen.eco.common.Utils

class XMLBlockEncoder (val block : Block) : Encoder
{
   private var xsw : XMLStreamWriter? = null

   /** Encodes the block tree to XML. */
   override fun encode () : ByteArray
   {
      Log.debug( "Encoding block to XML" )
      var baos = ByteArrayOutputStream()

      try
      {
         xsw = XMLOutputFactory.newInstance().createXMLStreamWriter( baos )
         xsw?.writeStartDocument( "utf-8", "1.0" )

         encodeBlockNode( block )

         xsw?.writeEndDocument()
      }
      catch (e : Exception)
      { Log.err( "XML encoding failed!" ) }

      return baos.toByteArray()
   }

   /** Recursive function to encode XML block nodes. */
   private fun encodeBlockNode (blockNode : BlockNode)
   {
      if (blockNode is Blob)
      {
         xsw?.apply(
         {
            writeStartElement( "blob" )
            writeAttribute( "size", blockNode.size.toString() )
            writeStartElement( "hash" )
            writeCharacters( Utils.byteArrayToB64String( blockNode.hash ) )
            writeEndElement()
            writeStartElement( "publicKey" )
            writeCharacters( Utils.byteArrayToB64String( blockNode.publicKey ) )
            writeEndElement()
            writeStartElement( "signature" )
            writeCharacters( Utils.byteArrayToB64String( blockNode.signature ) )
            writeEndElement()

            writeEndElement()
         })
      }
      else
         if (blockNode is Product)
         {
            xsw?.apply(
            {
               writeStartElement( "product" )
               writeAttribute( "id", blockNode.id.toString() )
               writeAttribute( "qty", blockNode.qty.toString() )
               writeEndElement()
            })
         }
         else
            if (blockNode is Block)
            {
               xsw?.writeStartElement( "block" )
               encodeMeta( blockNode )

               val children = blockNode.children
               for (child in children)
                  encodeBlockNode( child )                                             // Recurse down the tree

               xsw?.writeEndElement()
            }
   }

   /** Encodes block meta data. */
   private fun encodeMeta (block : Block)
   {
      xsw?.writeStartElement( "header" )
      xsw?.writeAttribute( "size", block.header.size.toString() )
      xsw?.writeAttribute( "level", block.header.level.toString() )
      xsw?.writeAttribute( "flags", block.header.flags.toString() )
      xsw?.writeAttribute( "id", block.header.id.toString() )
      xsw?.writeAttribute( "link", block.header.link.toString() )
      xsw?.writeAttribute( "year", block.header.year.toString() )
      xsw?.writeAttribute( "iteration", block.header.iteration.toString() )
      xsw?.writeAttribute( "version", block.header.version.toString() )
      xsw?.writeAttribute( "timestamp", block.header.timestamp.toString() )
      xsw?.writeEndElement()

      xsw?.writeStartElement( "hash" )
      xsw?.writeCharacters( Utils.byteArrayToB64String( block.hash ) )
      xsw?.writeEndElement()

      xsw?.writeStartElement( "publicKey" )
      xsw?.writeCharacters( Utils.byteArrayToB64String( block.publicKey ) )
      xsw?.writeEndElement()

      xsw?.writeStartElement( "signature" )
      xsw?.writeCharacters( Utils.byteArrayToB64String( block.signature ) )
      xsw?.writeEndElement()
   }
}
