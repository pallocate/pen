package pen.eco

import pen.Tagged

/** SI-system prefixes. */
enum class Prefix : Tagged
{
   NONE  { override fun tag () = "" },
   ATTO  { override fun tag () = "a" },
   FEMTO { override fun tag () = "f" },
   PICO  { override fun tag () = "p" },
   NANO  { override fun tag () = "n" },
   MICRO { override fun tag () = "μ" },
   MILLI { override fun tag () = "m" },
   CENTI { override fun tag () = "c" },
   DECI  { override fun tag () = "d" },
   HECTO { override fun tag () = "h" },
   DECA  { override fun tag () = "da" },
   KILO  { override fun tag () = "k" },
   MEGA  { override fun tag () = "M" },
   GIGA  { override fun tag () = "G" },
   TERA  { override fun tag () = "T" },
   PETA  { override fun tag () = "P" },
   EXA   { override fun tag () = "E" }
}

/** SI-system units, plus some additional units */
enum class Units : Tagged
{
   NONE    { override fun tag () = "" },

   // SI units and derived
   GRAM    { override fun tag () = "g" },
   LITRE   { override fun tag () = "l" },
   SECOND  { override fun tag () = "s" },
   WATT    { override fun tag () = "W" },
   METRE   { override fun tag () = "m" },
   METRE_2 { override fun tag () = "m2" },
   METRE_3 { override fun tag () = "m3" },

   // Additional units
   PIECE   { override fun tag () = "pc" },
   PACKAGE { override fun tag () = "p" },
   PALLET  { override fun tag () = "pl" },
   BALE    { override fun tag () = "bale" },
   TONNE   { override fun tag () = "tonne" },
   HECTARE { override fun tag () = "ha" },
   JOULE   { override fun tag () = "J" },
   CALORIE { override fun tag () = "Cal" },

   MINUTE  { override fun tag () = "min" },
   HOUR    { override fun tag () = "h" },
   DAY     { override fun tag () = "d" }
}
