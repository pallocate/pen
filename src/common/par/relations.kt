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

interface Relation
class NoRelation : Relation

@Serializable
data class KRelation (val other : KContact, val target : Target = Target.UNDEFINED) : Relation
{
   val roles : ArrayList<Role> = ArrayList<Role>()

   fun isUndefined () = (target == Target.UNDEFINED)
   override fun toString () = "${other.name()}" + if (isUndefined())
                                                   ""
                                                else
                                                   " (${target.tag()})"
}
