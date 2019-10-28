package pen.par

import java.io.Serializable

/** A type of of Contact to represent a unset value. */
class UnContact : Contact()
/** Contact information. */
open class Contact (var contactID : Long = 0L, var publicKey : ByteArray = ByteArray( 0 ), var group : String = "") : Serializable {}
