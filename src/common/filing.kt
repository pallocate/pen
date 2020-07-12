package pen

import kotlinx.serialization.KSerializer

expect inline fun <reified T : Any>serializeToFile (obj : T, filename : String, serializer : KSerializer<T>) : Boolean
expect inline fun <reified T : Any>deserializeFromFile (filename : String, serializer : KSerializer<T>) : T?
