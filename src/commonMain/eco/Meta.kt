package pen.eco

import kotlinx.serialization.Serializable
import pen.now

/** Product quatities meta information. */
@Serializable
sealed class Meta ()
{ abstract val year : Int }

@Serializable
data class KMeta (override val year : Int = 0) : Meta () {}

/** Proposal meta information. */
@Serializable
data class KProposalMeta_v1 (
   val id : Long               = 0L,
   override val year : Int     = 0,
   val iteration : Int         = 0,
   val target : Target         = Target.UNDEFINED,
   val timestamp : Long        = now()
) : Meta () {}
