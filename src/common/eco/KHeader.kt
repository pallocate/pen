package pen.eco

import kotlinx.serialization.Serializable
import pen.Constants
import pen.now

/** Proposal header. */
@Serializable
data class KHeader (
   val id : Long               = 0L,
   val year : Int              = 0,
   val iteration : Int         = 0,
   val target : Target         = Target.UNDEFINED,
   val timestamp : Long        = now(),
   val version : Int           = Constants.VERSION
) {}

