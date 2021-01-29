package pen.par

import kotlinx.serialization.Serializable
import pen.eco.Target

@Serializable
class KRelation (val other : KContact, val target : Target = Target.UNDEFINED)
{
   companion object
   { fun void () = KRelation(KContact( 0L, KContactInfo() )) }

   val roles : ArrayList<Role> = ArrayList<Role>()

   fun isVoid () = (other.id == 0L)
   override fun toString () = "${other.info.name}" + if (target == Target.UNDEFINED) "" else " (${target.tag()})"
}
