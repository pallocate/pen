package pen.par

import java.io.Serializable

/** Represents the different roles a participant can have in the economy. */
interface Role
{
   /** The name associated to this role */
   var name : String
   /** The icon file associated to this role */
   var icon : String
}
/** A default value or unset role. */
class NoRole : Role { override var name = ""; override var icon = "" }

/** An participant in the planning process. */
abstract class Participant : Role, Serializable
{ var me = Me() }

/** Other roles in the economy. */
abstract class Other : Role, Serializable
{ override var icon = Constants.ICONS_DIR + "emblem-generic.png" }
//class Trustee : Other() {}
class CouncilSigner : Other()
{ override var name = "Council signer" }

class DataController : Other()
{ override var name = "Data controller" }
class DataSubject : Other(), Serializable
{ override var name = "Data subject" }
