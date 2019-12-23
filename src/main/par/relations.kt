package pen.par


interface Contract
class NoContract : Contract

interface Role
{
   val id : Long
   val contract : Contract
}
enum class Roles : Role
{
   NO_ROLE { override val id : Long = 0; override val contract : Contract = NoContract() },
   MEMBER { override val id : Long = 0; override val contract : Contract = NoContract() },
   COUNCIL { override val id : Long = 0; override val contract : Contract = NoContract() },
   CONSUMER { override val id : Long = 0; override val contract : Contract = NoContract() },
   PRODUCER { override val id : Long = 0; override val contract : Contract = NoContract() },
   COUNCIL_SIGNER { override val id : Long = 0; override val contract : Contract = NoContract() },
   DATA_CONTROLLER { override val id : Long = 0; override val contract : Contract = NoContract() },
   DATA_SUBJECT { override val id : Long = 0; override val contract : Contract = NoContract() }
}

interface Relation
{
   val other : KContact
   val roles : ArrayList<Role>
}
class NoRelation : Relation
{
   override val other = KContact()
   override val roles = ArrayList<Role>()
}
class KRelation () : Relation
{
   override var other = KContact()
   override var roles : ArrayList<Role> = ArrayList<Role>()

   constructor (other : KContact, roles : ArrayList<Role>) : this()
   {
      this.other = other
      this.roles = roles
   }
}
