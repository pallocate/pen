package pen.eco.voting

import pen.eco.common.Log
import pen.eco.planning.Proposal
import pen.eco.planning.VerseProduct

/** Details about the votatation. */
interface Motion
{ override fun toString () : String }

/** A simple question, that can be expressed in a sentence or two. */
class SimpleMotion (val question : String) : Motion
{ override fun toString () = question }

/** Consumption motion of a council.
  * @param consumption The proposed consumption.
  * @param premise Why the proposal should be approved. */
class ConsumptionMotion (val consumption : Proposal, val premise : Premise) : Motion
{
   /** The numer of credits requested for consumption. */
   fun value () : Long
   {
      Log.debug( "Calculating consumption value" )
      var credits = 0L

      for (child in consumption.children)
         if (child is VerseProduct)
            credits += (child.qty*child.price)
         else
            Log.warn( "Wrong type! (expected VerseProduct)" )

      return credits
   }

   override fun toString () = "Proposal of value: ${value()}"
}
