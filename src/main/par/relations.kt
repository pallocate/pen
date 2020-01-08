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
data class KRelation (var other : KContact = KContact(), var target : Target = Target.UNDEFINED, var roles : ArrayList<Role> = ArrayList<Role>()) : Relation
{ override fun toString () = "${other.name} (${target.char()})" }
