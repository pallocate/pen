package pen.eco.types.proposal

import pen.eco.Constants

/** Proposal header. */
open class KHeader (
                     /** Total size of the block. */
                     open val size : Long                              = 0L,
                     /** Federative level in the economy. */
                     open val level : Int                              = 0,
                     /** Flags */
                     open val flags : Int                              = 0,
                     /** User id */
                     open val id : Long                                = 0L,
                     /** Link to an account by the user, stored externally. */
                     open val link : Long                              = 0L,
                     /** The year of the planning process. */
                     open val year : Int                               = 0,
                     /** Iteration number of the planning process. */
                     open val iteration : Int                          = 0,
                     /** Spec version. */
                     open val version : Int                            = Constants.VERSION,
                     /** Epoch seconds timestamp. */
                     open val timestamp : Long                         = 0L
                  )
{
   /** Makes a copy of this header. */
   fun copy () = KHeader( size, level, flags, id, link, year, iteration, version, timestamp )

   /** If this is a production proposal, otherwise consumption. */
   fun isProduction () : Boolean
   { return isFlagSet( Constants.IS_PRODUCTION ) }
   /** If this is a proposal (only has Product´s as children). */
   fun isProposal () : Boolean
   { return isFlagSet( Constants.IS_PROPOSAL ) }

   /** A string representing the state of the planning process. */
   fun progression () : String = year.toString() + ":" + iteration.toString()
   /** Returns a string that can be used to identify the block header in a log file. */
   fun idString () : String = progression() + " (" + id + ")"

   /** Tests status of the specified flag.
     * @return True if the flag is set. */
   fun isFlagSet (flag : Int) : Boolean = (flags and flag > 0)

   /** Encodes header to text. */
   override fun toString () : String
   {
      var ret = "[PROPOSAL]\n"

      ret += ("Version:    ${version}\n")
      ret += ("Year:       ${year}\n")
      ret += ("Iteration:  ${iteration}\n")

      if (isProduction())
         ret += ("Tree:       Production\n")
      else
         ret += ("Tree:       Consumption\n")
      ret += ("Level:      ${level}\n")

      if (link != 0L)
         ret += ("Link:       ${link}\n")

      return ret
   }

}
