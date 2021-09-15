package pen

import kotlinx.serialization.KSerializer

expect inline fun <reified T>serializeToFile (obj : T, filename : String, serializer : KSerializer<T>) : Boolean
expect inline fun <reified T>deserializeFromFile (filename : String, serializer : KSerializer<T>) : T?
