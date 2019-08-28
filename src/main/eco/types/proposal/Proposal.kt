package pen.eco.types.proposal

abstract class Proposal (open val header : KHeader = KHeader()) {}

class KProposal (header : KHeader, val items : List<KItem>) : Proposal( header )
{
   override fun toString () : String
   {
      var ret = header.toString()

      for (item in items)
         ret += item

      return ret
   }
}
