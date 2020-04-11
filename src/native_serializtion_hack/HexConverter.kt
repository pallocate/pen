package kotlinx.serialization.internal

object HexConverter {
    fun printHexBinary (data : ByteArray) = data.joinToString( "" ) { it.toInt().and( 0xFF ).toString( 16 ).padStart( 2, '0' )}
} 
