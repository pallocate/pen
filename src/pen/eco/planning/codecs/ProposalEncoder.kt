package pen.eco.planning.codecs

import pen.eco.common.Log
import pen.eco.planning.Proposal
import pen.eco.planning.VerseProduct

class ProposalEncoder (val proposal : Proposal) : Encoder
{
   /** Encodes a proposal to text.
     * @return The text proposal as a ByteArray */
   override fun encode () : ByteArray = encode( false )

   /** Encodes a proposal to text.
     * @param strip Whether to strip "unnecessary" information or not.
     * @return The text proposal as a ByteArray */
   fun encode (strip : Boolean) : ByteArray
   {
      Log.debug( "Encoding proposal" )
      var ret = encodeHeader( strip )

      for (child in proposal.children)
         if (child is VerseProduct)
            ret += encodeVerseProduct( child, strip )

      return ret
   }

   /** Encodes a VerseProduct to text.
     * @param strip If "unnecessary" data should be stripped off, like when proposal is being submitted.
     * @return Returns The product as a ByteArray of text. */
   fun encodeVerseProduct (verseProduct : VerseProduct, strip : Boolean) : ByteArray
   {
      var ret = ("\n" + "[PRODUCT]\n").toByteArray()

      ret += ("Id:         " + verseProduct.id.toString() + "\n").toByteArray()
      ret += ("Qty:        " + verseProduct.qty.toString() + "\n").toByteArray()

      if (!strip)
      {
         ret += ("Name:       " + verseProduct.name + "\n").toByteArray()
         ret += ("Desc:       " + verseProduct.desc + "\n").toByteArray()

         if (verseProduct.amount != 0F)
            ret += ("Amount:     " + verseProduct.amount.toString() + "\n").toByteArray()
         if (verseProduct.prefix != "" && verseProduct.prefix != "None")
            ret += ("Prefix:     " + verseProduct.prefix + "\n").toByteArray()
         if (verseProduct.unit != "" && verseProduct.unit != "None")
            ret += ("Unit:       " + verseProduct.unit + "\n").toByteArray()
         if (verseProduct.down != 0)
            ret += ("Down:       " + verseProduct.down.toString() + "\n").toByteArray()
         if (verseProduct.up != 0)
            ret += ("Up:         " + verseProduct.up.toString() + "\n").toByteArray()
         if (verseProduct.absolute != 0L)
            ret += ("Absolute:   " + verseProduct.absolute.toString() + "\n").toByteArray()

         ret += ("Price:      " + verseProduct.price.toString() + "\n").toByteArray()

         if (verseProduct.sensetive != "" && verseProduct.sensetive != "false")
            ret += ("Sensetive:   " + verseProduct.sensetive + "\n").toByteArray()

         if (verseProduct.analogue != "" && verseProduct.analogue != "false")
            ret += ("Analogue:   " + verseProduct.analogue + "\n").toByteArray()
      }

      return ret
   }

   /** Encodes header to text.
     * @param strip If "unnecessary" data should be stripped off, like when proposal is being submitted.
     * @return The header as a ByteArray of text. */
   private fun encodeHeader (strip : Boolean) : ByteArray
   {
      var ret = "[PROPOSAL]\n".toByteArray()

      ret += ("Version:    " + proposal.header.version.toString() + "\n").toByteArray()
      ret += ("Year:       " + proposal.header.year.toString() + "\n").toByteArray()
      ret += ("Iteration:  " + proposal.header.iteration.toString() + "\n").toByteArray()

      if (proposal.isProduction())
         ret += ("Tree:       Production" + "\n").toByteArray()
      else
         ret += ("Tree:       Consumption" + "\n").toByteArray()
      ret += ("Level:      " + proposal.header.level.toString() + "\n").toByteArray()

      if (proposal.header.link != 0L)
         ret += ("Link:       " + proposal.header.link.toString() + "\n").toByteArray()

      return ret
   }
}
