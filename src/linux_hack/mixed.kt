package kotlinx.serialization

annotation class Serializable
annotation class SerialName (val value : String)

interface KSerializer<T>
class NoSerializer : KSerializer<Int>

