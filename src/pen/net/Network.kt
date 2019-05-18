package pen.net

import pen.eco.common.Identifiable
import pen.eco.common.NoIdentifiable
import pen.eco.common.Message

object Network
{
   val stuff = ArrayList<Identifiable>()
   val nrOfCouncils = 1000

   fun publish (identifiable : Identifiable) = stuff.add( identifiable )
   fun findStuff (id : Long) : Identifiable
   {
      var ret : Identifiable = NoIdentifiable()
      val result = stuff.find({ it.id == id })

      if (result != null)
         ret = result

      return ret
   }

   fun send (message : Message) {}

   var counter = 0L
   fun generateID () = ++counter
}
