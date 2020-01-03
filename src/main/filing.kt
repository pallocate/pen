package pen

import kotlinx.serialization.KSerializer

expect object Filer
{
   inline fun <reified T : Any>write (serialFun : () -> KSerializer<T>, obj : T, name : String)
   inline fun <reified T : Any>read (serialFun : () -> KSerializer<T>, name : String) : T?
}

