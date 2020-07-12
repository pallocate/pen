package pen

import kotlinx.serialization.KSerializer

actual inline fun <reified T : Any>serializeToFile (obj : T, filename : String, serializer : KSerializer<T>) = true
actual inline fun <reified T : Any>deserializeFromFile (filename : String, serializer : KSerializer<T>) : T? = null
