package pen.eco

import kotlinx.serialization.Serializable
import pen.Constants

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
   /** What economic function the proposal belong to, consumption or production. */
   val target : Target
   /** Epoch seconds timestamp. */
   val timestamp : Long

   /** Progress of the planning process in the form "year:iteration[-P|-C]. Optional initial P or C
     * indicates Production or Consumption. */
   fun progression (pc : Boolean = false) : String
   {
      val stringBuilder = StringBuilder()

      stringBuilder.append( year.toString() )
      stringBuilder.append( ":" + iteration )
      if (pc) stringBuilder.append( target.char() )

      return stringBuilder.toString()
   }

   /** Returns a string that can be used to identify the block header in a log file. */
   fun idString () : String = progression( true ) + " (" + id + ")"

   /** Encodes header to "ini" text. */
   fun encode () : String
   {
      val stringBuilder = StringBuilder()

      stringBuilder.append( "[PROPOSAL]\n" )
      stringBuilder.append( "Version:    ${version}\n" )

      if (id != 0L)
         stringBuilder.append( "Id:         ${id}\n" )
      if (year != 0)
         stringBuilder.append( "Year:       ${year}\n" )
      if (iteration != 0)
         stringBuilder.append( "Iteration:  ${iteration}\n" )
      if (level != 0)
         stringBuilder.append( "Level:      ${level}\n" )
      if (target != Target.UNDEFINED)
         stringBuilder.append( "Function:   ${target}\n" )
      if (timestamp != 0L)
         stringBuilder.append( "Timestamp:  ${timestamp}\n" )

      return stringBuilder.toString()
   }
}

/** Proposal header. */
@Serializable
class KHeader (
                  override val version : Int                            = Constants.VERSION,
                  override val id : Long                                = 0L,
                  override val year : Int                               = 0,
                  override val iteration : Int                          = 0,
                  override val level : Int                              = 0,
                  override val target : Target              = Target.UNDEFINED,
                  override val timestamp : Long                         = 0L
               ) : Header
{
   /** Makes a copy of this header. */
   fun copy () = KHeader( version, id, year, iteration, level, target, timestamp )
}
