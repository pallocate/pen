package pen

expect object Filer
{
   inline fun <reified T : Filable>read (name : String) : Filable
   fun write (filable : Filable, name : String)
}

interface Filable
class NoFilable : Filable

