package pen.eco

import java.io.Writer
import java.io.Reader

interface Convertable
class NoConvertable : Convertable

expect object Serializer
{
   inline fun <reified T : Convertable>read (reader : Reader) : Convertable
   fun write (convertable : Convertable, writer : Writer)
}
