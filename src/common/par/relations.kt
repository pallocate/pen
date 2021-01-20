package pen.par

import kotlinx.serialization.Serializable
import pen.eco.Target

enum class Role
{
   NO_ROLE,
   ACQUAINTANCE,

   /* General roles */
   CONCEDER,
   PROPOSER,

   /* Inter council roles */
   SUPPLIER,
   CUSTOMER,

   /* Data security roles */
   COUNCIL_SIGNER,
   DATA_CONTROLLER,
   DATA_SUBJECT
}

@Serializable
class KRelation (val other : KContact, val target : Target = Target.UNDEFINED)
{
   companion object
   { fun void () = KRelation(KContact( 0L, KContactInfo() )) }

   val roles : ArrayList<Role> = ArrayList<Role>()

   fun isVoid () = (other.id == 0L)
   override fun toString () = "${other.info.name}" + if (target == Target.UNDEFINED) "" else " (${target.tag()})"
}
