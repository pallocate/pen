package pen.par

import java.io.Serializable
import pen.eco.common.Crypto
import pen.net.Message

/** User serializable data. */
class Persistence : Serializable
{
   /** The different roles in the economy the user has. */
   val roles                                    = ArrayList<Role>()
   /** The recieved messages to this user. */
   val inbox                                    = ArrayList<Message>()
   /** The messages to be sent from this user. */
   val outbox                                   = ArrayList<Message>()
   /** Returns false if this Me is not loaded. */
   var isLoaded                                 = false

   /** Searches the roles for a named contact.
     * @return The contact if it was found, otherwise Nobody */
   fun lookUpContact (name : String) : Contact
   {
      var ret : Contact = UnContact()
      val searchSet = HashMap<String, Contact>()

      for (role in roles)
         if (role is Council)
            searchSet.putAll( role.members )
         else
            if (role is Member)
               searchSet.putAll( role.council() )

      if (searchSet.containsKey( name ))
         ret = searchSet.get( name )!!

      return ret
   }

   /** Returns the list of member roles this user has in the economy. */
   fun memberRoles () : ArrayList<Member>
   {
      val ret = ArrayList<Member>()
      for (role in roles)
         if (role is Member)
             ret.add( role )

      return ret
   }
}
