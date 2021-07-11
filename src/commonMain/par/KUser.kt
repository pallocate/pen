
package pen.par

import pen.Voidable
import kotlinx.serialization.Serializable

@Serializable
class KUser (val me : KMe, val language : String = "English") : Voidable
{
   companion object
   { fun void () = KUser(KMe( KContact.void() )) }

   val relations = ArrayList<KRelation>()

   fun findRelation (id : Long) = relations.find {
      relation -> id == relation.other.id
   } ?: KRelation.void()

   override fun isVoid () = me.contact.isVoid()
   override fun toString () = "${me.contact.info.name}"
}
