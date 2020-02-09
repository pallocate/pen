package pen.eco

import kotlinx.serialization.Serializable
import pen.Constants

interface Header
{
   /** User id */
   val id : Long
   /** The year of the planning process. */
   val year : Int
   /** Iteration number of the planning process. */
   val iteration : Int
   /** What economic function the proposal belong to, consumption or production. */
   val target : Target
   /** Epoch seconds timestamp. */
   val timestamp : Long
   /** Spec version. */
   val version : Int

   /** Progress of the planning process in the form "year:iteration. */
   fun progression () = year.toString() +  ":" + iteration

   /** Returns a string that can be used to identify the block header in a log file. */
   fun idString () : String = progression() + " (" + id + ")"

   /** Encodes header to json. */
   fun encode () : String
   {

      return ""
   }
}

/** Proposal header. */
@Serializable
class KHeader (
                  override val id : Long               = 0L,
                  override val year : Int              = 0,
                  override val iteration : Int         = 0,
                  override val target : Target         = Target.UNDEFINED,
                  override val timestamp : Long        = 0L,
                  override val version : Int           = Constants.VERSION
               ) : Header
{
   /** Makes a copy of this header. */
   fun copy () = KHeader( id, year, iteration, target, timestamp )
}
