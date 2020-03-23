package pen

import kotlinx.serialization.KSerializer

actual inline fun <reified T : Any>writeObject (obj : T, serializerFunction : () -> KSerializer<T>, filename : String) = true
actual inline fun <reified T : Any>readObject (serializerFunction : () -> KSerializer<T>, filename : String) : T? = null

