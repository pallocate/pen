package pen.utils

import pen.par.KContact

object Contacts
{
   val artysan = Artysan.contact
   val patricia = Patricia.contact
   val david = David.contact
   val factory = Factory.contact
   val hospital = Hospital.contact
   val farmlands = Farmlands.contact
   val crowbeach = CrowBeach.contact
   val university = University.contact
   val clothesshop = ClothesShop.contact
   val newsbureau = KContact(9L, KContact.KInfo( "News bureau" ))

   fun toArray () = arrayOf( university, artysan, patricia, david, factory, hospital, farmlands, crowbeach, clothesshop )
}
