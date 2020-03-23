package pen.par

/** Alternatives used in votings.
  * @param description Description of the alterantive. */
interface Alternatives
{
   val name : String
   val ordinal : Int

   operator fun inc () : Alternatives
}

enum class NoAlternatives : Alternatives
{
   BLANK;
   override operator fun inc () = this
}

enum class YesNo : Alternatives
{
   BLANK, YES, NO;

   override operator fun inc () : YesNo
   {
      val next = (ordinal + 1).coerceAtMost( NO.ordinal )
      return values()[next]
   }
}

enum class TwoAlternatives : Alternatives
{
   BLANK, ALT_1, ALT_2;

   override operator fun inc () : TwoAlternatives
   {
      val next = (ordinal + 1).coerceAtMost( ALT_2.ordinal )
      return values()[next]
   }
}

enum class ThreeAlternatives : Alternatives
{
   BLANK, ALT_1, ALT_2, ALT_3;

   override operator fun inc () : ThreeAlternatives
   {
      val next = (ordinal + 1).coerceAtMost( ALT_3.ordinal )
      return values()[next]
   }
}
