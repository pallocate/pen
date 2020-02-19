package pen.par

/** Alternatives used in votings.
  * @param description Description of the alterantive. */
enum class Alternatives (var description : String = "")
{
   BLANK( "Blank" ),
   ALT_1( "Dismiss" ),
   ALT_2( "Approve" ),
   ALT_3, ALT_4, ALT_5, ALT_6, ALT_7, ALT_8, ALT_9;

   operator fun inc () : Alternatives
   {
      val next = (ordinal + 1).coerceAtMost( ALT_9.ordinal )
      return values()[next]
   }
}

/** @param choiseRange Range of possible choises. */
open class Choise (selection : Alternatives = Alternatives.ALT_1)
{
   open fun choiseRange () : ClosedRange<Alternatives> = Alternatives.BLANK..Alternatives.ALT_2

   var selection : Alternatives = Alternatives.BLANK
      set(choise : Alternatives)
      {
         field =  if (choise <= choiseRange().endInclusive)
                     choise
                  else
                     Alternatives.BLANK
      }

   init
   {
      this.selection = selection
   }
}

class ChoiseOfThree (selection : Alternatives = Alternatives.ALT_1) : Choise( selection )
{
   override fun choiseRange () = Alternatives.BLANK..Alternatives.ALT_3
}


class ChoiseOfFuor (selection : Alternatives = Alternatives.ALT_1) : Choise( selection )
{
   override fun choiseRange () = Alternatives.BLANK..Alternatives.ALT_4
}

class ChoiseOfFive (selection : Alternatives = Alternatives.ALT_1) : Choise( selection )
{
   override fun choiseRange () = Alternatives.BLANK..Alternatives.ALT_5
}
