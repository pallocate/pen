package pen.eco

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import pen.now

@Serializable
sealed class PqMeta ()
{ abstract val year : Int }

@Serializable
@SerialName("pqMeta")
data class KPqMeta (override val year : Int = 0) : PqMeta () {}

/** Proposal meta information, versioning is in the class name for simplicity. */
@Serializable
@SerialName("proposalMeta")
data class KProposalMeta_v1 (
   val id : Long               = 0L,
   override val year : Int     = 0,
   val iteration : Int         = 0,
   val target : Target         = Target.UNDEFINED,
   val timestamp : Long        = now()
) : PqMeta () {}
