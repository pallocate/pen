package pen.eco

import pen.Tagged

/** What economic function something has in the economy. */
enum class Target : Tagged {
   UNDEFINED { override fun tag () = "U" },
   CONSUMPTION { override fun tag () = "C" },
   PRODUCTION { override fun tag () = "P" };

   companion object
   {
      fun fromInt (int : Int) : Target = values()[int.coerceIn( UNDEFINED.ordinal, PRODUCTION.ordinal )]
   }
}

fun String.toFloat () : Float
{
   val value = toFloatOrNull()
   return   if (value != null)
               value
            else
               0F
}

fun String.toBool () =  if (this == "true")
                              true
                           else
                              false

fun String.toUnit () =  when (this.uppercase())
                        {
                           Units.GRAM.name -> Units.GRAM
                           Units.LITRE.name -> Units.LITRE
                           Units.SECOND .name -> Units.SECOND
                           Units.WATT.name -> Units.WATT
                           Units.METRE.name -> Units.METRE
                           Units.METRE_2.name -> Units.METRE_2
                           Units.METRE_3.name -> Units.METRE_3

                           Units.PIECE.name -> Units.PIECE
                           Units.TONNE.name -> Units.TONNE
                           Units.HECTARE.name -> Units.HECTARE
                           Units.JOULE.name -> Units.JOULE
                           Units.CALORIE.name -> Units.CALORIE

                           Units.MINUTE.name -> Units.MINUTE
                           Units.HOUR.name -> Units.HOUR
                           Units.DAY.name -> Units.DAY
                           else -> Units.NONE
                        }

fun String.toPrefix () =   when (this.uppercase())
                           {
                              Prefix.ATTO.name -> Prefix.ATTO
                              Prefix.FEMTO.name -> Prefix.FEMTO
                              Prefix.PICO.name -> Prefix.PICO
                              Prefix.NANO.name -> Prefix.NANO
                              Prefix.MICRO.name -> Prefix.MICRO
                              Prefix.MILLI.name -> Prefix.MILLI
                              Prefix.CENTI.name -> Prefix.CENTI
                              Prefix.DECI.name -> Prefix.DECI
                              Prefix.HECTO.name -> Prefix.HECTO
                              Prefix.KILO.name -> Prefix.KILO
                              Prefix.MEGA.name -> Prefix.MEGA
                              Prefix.GIGA.name -> Prefix.GIGA
                              Prefix.TERA.name -> Prefix.TERA
                              Prefix.PETA.name -> Prefix.PETA
                              Prefix.EXA.name -> Prefix.EXA
                              else -> Prefix.NONE
                           }
