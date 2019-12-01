package pen.eco

interface Filable
class NoFilable : Filable

expect object Filer
{
   inline fun <reified T : Filable>read (name : String) : Filable
   fun write (filable : Filable, name : String)
}
