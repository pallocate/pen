@file:kotlinx.serialization.UseSerializers( ByteArraySerialiser::class )

package pen.par

import pen.ByteArraySerialiser
import kotlinx.serialization.Serializable

@Serializable
data class KContactInfo (
   val name : String = "",
   val publicKey : ByteArray = ByteArray( 0 ),
   val icon : String = "",
   val group : String = ""
) {}

@Serializable
data class KContact (val id : Long, val info : KContactInfo = KContactInfo())
{
   companion object
   {fun void () = KContact( 0L, KContactInfo() )}

   fun isVoid () = (id == 0L)
}
//@Serializable (with=ByteArraySerialiser::class)
