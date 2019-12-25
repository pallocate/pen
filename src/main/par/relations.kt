package pen.par


interface Contract
class NoContract : Contract
class KContract () : Contract

enum class Role
{
   /* Represents a unset or empty value */
   NO_ROLE,
   /* Might be useful inbetween people */
   ACQUAINTANCE,

   /* General roles */
   CONSIDER,
   SUBMITTER,
   PRODUCER,
   CONSUMER,

   /* Inter council roles */
   SUPPLIER,
   CUSTOMER,

   /* Data security roles */
   COUNCIL_SIGNER,
   DATA_CONTROLLER,
   DATA_SUBJECT
}

class KRelation ()
{
   var other = KContact()
   var roles : ArrayList<Role> = ArrayList<Role>()

   constructor (other : KContact, roles : ArrayList<Role>) : this()
   {
      this.other = other
      this.roles = roles
   }
}
