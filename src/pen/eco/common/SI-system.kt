package pen.eco.common

/** SI-system prefixes. */
object Prefixes
{
   const val NONE                = "None"
   const val EXA                 = "Exa"
   const val PETA                = "Peta"
   const val TERA                = "Tera"
   const val GIGA                = "Giga"
   const val MEGA                = "Mega"
   const val KILO                = "Kilo"
   const val HECTO               = "Hecto"
   const val DECA                = "Deca"
   const val DECI                = "Deci"
   const val CENTI               = "Centi"
   const val MILLI               = "Milli"
   const val MICRO               = "Micro"
   const val NANO                = "Nano"
   const val PICO                = "Pico"
   const val FEMTO               = "Femto"
   const val ATTO                = "Atto"

   val array = arrayOf( EXA, PETA, TERA, GIGA, MEGA, KILO, HECTO, DECA, NONE, DECI, CENTI, MILLI, MICRO, NANO, PICO, FEMTO, ATTO )
   val symbol : HashMap<String, String> = hashMapOf( "Exa" to "E", "Peta" to "P", "Tera" to "T", "Giga" to "G", "Mega" to "M", "Kilo" to "k", "Hecto" to "h", "Deca" to "da",
      "Deci" to "d", "Centi" to "c", "Milli" to "m", "Micro" to "μ", "Nano" to "n", "Pico" to "p", "Femto" to "f", "Atto" to "a" )
}

/** SI-system units, plus some additional units */
object Units
{
   // SI units and derived
   const val GRAM                = "Gram"
   const val LITRE               = "Litre"
   const val SECOND              = "Second"
   const val WATT                = "Watt"
   const val METRE               = "Metre"
   const val METRE_2             = "Metre square"
   const val METRE_3             = "Metre cubic"

   // Additional units
   const val NONE                = "None"
   const val PIECE               = "Piece"
   const val TONNE               = "Tonne"
   const val HECTARE             = "Hectare"
   const val JOULE               = "Joule"
   const val CALORIE             = "Calorie"

   const val MINUTE              = "Minute"
   const val HOUR                = "Hour"
   const val DAY                 = "Day"

   val array = arrayOf( NONE, PIECE, GRAM, LITRE, SECOND, WATT, METRE, METRE_2, METRE_3, TONNE, HECTARE, JOULE, CALORIE, MINUTE, HOUR, DAY )
   val symbol : HashMap<String, String> = hashMapOf( "Piece" to "pc", "Gram" to "g", "Litre" to "l", "Second" to "s", "Watt" to "W", "Metre" to "m", "Metre squared" to "m2",
      "Metre cubic" to "m3", "Tonne" to "tonne", "Hectare" to "ha", "Joule" to "J", "Calorie" to "Cal", "Minute" to "min", "Hour" to "h", "Day" to "d" )
}
