package pen.par

import kotlinx.serialization.Serializable

@Serializable
class KUser (val me : KMe, val language : String = "English")
{
   companion object
   { fun void () = KUser(KMe( 0L, KContactInfo() )) }

   val relations = ArrayList<KRelation>()

   fun findRelation (id : Long) = relations.find {
      relation -> id == relation.other.id
   } ?: KRelation.void()

   fun isVoid () = (me.id == 0L)
   override fun toString () = "${me.info.name}"
}
