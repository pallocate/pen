package pen.eco.planning.codecs

import java.io.InputStream
import pen.eco.common.Log
import pen.eco.common.Config
import pen.eco.planning.IniStreamParser
import pen.eco.planning.Constants
import pen.eco.planning.Proposal
import pen.eco.planning.VerseProduct
import pen.eco.common.WrongVersionException

class ProposalDecoder (val proposal : Proposal) : Decoder, IniStreamParser
{
   override val HEADER_KEY                      = "PROPOSAL"
   override val ITEM_KEY                        = "PRODUCT"

   /** Decodes a proposal using IniStreamParser. */
   override fun decode (inputStream : InputStream)
   { read( inputStream ) }

   override fun headerSection (map : HashMap<String, String>)
   {
      try
      {
         if (map.getOrElse( "version", {"0"} ).toInt() != Config.VERSION) throw WrongVersionException()
         if (map.getOrElse( "tree", {"consumption"} ) == "production") proposal.header.setFlag( Constants.IS_PRODUCTION )
         proposal.header.apply(
         {
            year = map.getOrElse( "year", {"0"} ).toInt()
            iteration = map.getOrElse( "iteration", {"0"} ).toInt()
            level = map.getOrElse( "level", {"0"} ).toInt()
            link = map.getOrElse( "link", {"0"} ).toLong()
         })
      }
      catch (e : Exception)
      { Log.warn( "Number conversion failed!" ) }

      proposal.header.setFlag( Constants.IS_PROPOSAL )
   }

   override fun itemSection (map : HashMap<String, String>)
   {
      val product = VerseProduct()

      try
      {
         product.apply(
         {
            id = map.getOrElse( "id", {"0"} ).toLong()
            qty = map.getOrElse( "qty", {"0"} ).toLong()
            name = map.getOrElse( "name", {"None"} )
            desc = map.getOrElse( "desc", {"None"} )
            amount = map.getOrElse( "amount", {"0"} ).toFloat()
            prefix = map.getOrElse( "prefix", {"None"} )
            unit = map.getOrElse( "unit", {"None"} )
            down = map.getOrElse( "down", {"0"} ).toInt()
            up = map.getOrElse( "up", {"0"} ).toInt()
            absolute = map.getOrElse( "absolute", {"0"} ).toLong()
            price = map.getOrElse( "price", {"0"} ).toLong()
            analogue = map.getOrElse( "analogue", {"false"} )
         })
      }
      catch (e : Exception)
      { Log.warn( "Number conversion failiure!" ) }

      if (product.id != 0L)
         proposal.children.add( product )
   }
}
