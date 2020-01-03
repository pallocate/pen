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
class KRelation () : Relation
{
   var other = KContact()
   var target = Target.UNDEFINED
   var roles : ArrayList<Role> = ArrayList<Role>()

   constructor (other : KContact, target : Target, roles : ArrayList<Role>) : this()
   {
      this.other = other
      this.target = target
      this.roles = roles
   }

   override fun toString () = "${other.name} (${target.char()})"
}
