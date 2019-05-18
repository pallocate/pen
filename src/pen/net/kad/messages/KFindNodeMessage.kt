package pen.net.kad.messages

import pen.eco.common.Log
import pen.eco.common.DebugValue
import pen.eco.common.Config.getSettings
import pen.net.kad.node.KNode
import pen.net.kad.node.KNodeId

/** A message sent to other nodes requesting the K-Closest nodes to a key sent in this message
     * @param origin The KNode from which the message is coming from
     * @param lookup The key for which to lookup nodes for */
class KFindNodeMessage () : Message
{
   var origin                    = KNode()
   var lookupId                  = KNodeId()

   init
   { Log.debug( {"<FIND_NODE>"}, getSettings().getValue( DebugValue.MESSAGE_FIND_NODE )) }

   constructor (origin : KNode, lookupId : KNodeId) : this()
   {
      this.origin = origin
      this.lookupId = lookupId
   }

   override fun code () = Codes.FIND_NODE
}
