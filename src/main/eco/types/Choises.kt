package pen.eco.types

/** Ten choises, to be used in votings.
  * @param description Description of the alterantive. */
enum class Choises (var description : String = "")
{
   ALT_1( "Dismiss" ),
   ALT_2( "Approve" ),
   ALT_3( "Blank" ),
   ALT_4, ALT_5, ALT_6, ALT_7, ALT_8, ALT_9, ALT_10;

   operator fun inc () : Choises
   {
      val next = (ordinal + 1).coerceAtMost( ALT_10.ordinal )
      return values()[next]
   }
}

/** @param choiseRange Range of possible choises. */
open class Choise (selection : Choises = Choises.ALT_1)
{
   open fun choiseRange () : ClosedRange<Choises> = Choises.ALT_1..Choises.ALT_2

   var selection : Choises = Choises.ALT_1
      set(choise : Choises)
      {
         field = choise.coerceAtMost( choiseRange().endInclusive )
      }

   init
   {
      this.selection = selection
   }
}

class ChoiseOfThree (selection : Choises = Choises.ALT_1) : Choise( selection )
{
   override fun choiseRange () = Choises.ALT_1..Choises.ALT_3
}


class ChoiseOfFuor (selection : Choises = Choises.ALT_1) : Choise( selection )
{
   override fun choiseRange () = Choises.ALT_1..Choises.ALT_4
}

class ChoiseOfFive (selection : Choises = Choises.ALT_1) : Choise( selection )
{
   override fun choiseRange () = Choises.ALT_1..Choises.ALT_5
}
