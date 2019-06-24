package pen.net.kad.messages

import pen.eco.Log

import pen.eco.Config
import pen.net.kad.node.KNode

/** A message used to connect nodes.
  * When a NodeLookup Request comes in, we respond with a KFindNodeReply */
class KFindNodeReply () : Message
{
   var origin                    = KNode()
   var nodes                     = ArrayList<KNode>()

   init
   { Log.debug( {"<FIND_NODE_REPLY>"}, Config.flag( "MESSAGE_FIND_NODE" )) }

   constructor (origin : KNode, nodes : ArrayList<KNode>) : this()
   {
      this.origin = origin
      this.nodes = nodes
   }

   override fun code () = Codes.FIND_NODE_REPLY
}
