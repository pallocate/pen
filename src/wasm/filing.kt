package pen

/** Skeleton code for web assembly. */
actual object Filer
{
   actual inline fun <reified T : Filable>read (name : String) = NoFilable() as Filable
   actual fun write (filable : Filable, name : String) {}
}
