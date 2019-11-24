package pen.eco.types

import pen.eco.Constants

interface Header
{
   /** Spec version. */
   val version : Int
   /** User id */
   val id : Long
   /** The year of the planning process. */
   val year : Int
   /** Iteration number of the planning process. */
   val iteration : Int
   /** Federative level in the economy. */
   val level : Int
   /** Flags */
   val flags : Int
   /** Epoch seconds timestamp. */
   val timestamp : Long

   /** If this is a production proposal, otherwise consumption. */
   fun isProduction () : Boolean
   { return isFlagSet( Constants.IS_PRODUCTION ) }

   /** Progress of the planning process in the form "year:iteration[-P|-C]. Optional extension letter P or C indicates the Production or Consumption trees. */
   fun progression (treeLetter : Boolean = false) : String
   {
      val progress = year.toString() + ":" + iteration.toString()
      return progress + if (treeLetter)
                        {
                           if (isProduction())
                              "-P"
                           else
                              "-C"
                        }
                        else
                           ""
   }

   /** Returns a string that can be used to identify the block header in a log file. */
   fun idString () : String = progression( true ) + " (" + id + ")"

   /** Tests status of the specified flag.
     * @return True if the flag is set. */
   fun isFlagSet (flag : Int) : Boolean = (flags and flag > 0)

   /** Encodes header to "ini" text. */
   fun encode () : String
   {
      var ret = "[PROPOSAL]\n"

      ret += ("Version:    ${version}\n")

      if (id != 0L)
         ret += ("Id:         ${id}\n")

      ret += ("Year:       ${year}\n")
      ret += ("Iteration:  ${iteration}\n")
      ret += ("Level:      ${level}\n")

      if (isProduction())
         ret += ("Tree:       Production\n")
      else
         ret += ("Tree:       Consumption\n")

      if (timestamp != 0L)
         ret += ("Timestamp:  ${timestamp}\n")


      return ret
   }
}

/** Proposal header. */
class KHeader (
                     override val version : Int                            = Constants.VERSION,
                     override val id : Long                                = 0L,
                     override val year : Int                               = 0,
                     override val iteration : Int                          = 0,
                     override val level : Int                              = 0,
                     override val flags : Int                              = 0,
                     override val timestamp : Long                         = 0L
                  ) : Header
{
   /** Makes a copy of this header. */
   fun copy () = KHeader( version, id, year, iteration, level, flags, timestamp )
}
