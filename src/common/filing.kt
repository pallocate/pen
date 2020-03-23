package pen

import kotlinx.serialization.KSerializer

expect inline fun <reified T : Any>writeObject (obj : T, serializerFunction : () -> KSerializer<T>, filename : String) : Boolean
expect inline fun <reified T : Any>readObject (serializerFunction : () -> KSerializer<T>, filename : String) : T?
