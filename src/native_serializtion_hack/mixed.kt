package kotlinx.serialization

annotation class Serializable
annotation class SerialName (val value : String)
annotation class InternalSerializationApi

interface KSerializer<T>
class NoSerializer : KSerializer<Int>

