package pen.net.kad

import pen.eco.common.Config.CONFIG_DIR
import pen.eco.common.Config.SLASH
import pen.eco.common.Directory

   /* Networking constants */
object Constants
{
   val IS_TESTING                                 = true                        // Affects the servers simulated latency
   const val RESTORE_INTERVAL                     = (60*1000).toLong()          // (milliseconds)
   const val RESPONCE_TIMEOUT                     = 2000L
   const val OPERATION_TIMEOUT                    = 2000L
   const val MAX_CONCURRENT_MESSAGES_TRANSITING   = 10
   const val K                                    = 5
   const val STALE                                = 1
//   const val REPLACEMENT_CACHE_SIZE               = 3

   /** Name of stored kademlia node file. */
   const val KAD_FILE                             = "kad.json"
   /** Name of stored routing table file. */
   const val ROUTING_TABLE_FILE                   = "routingtable.json"
   /** Name of stored node file. */
   const val NODE_FILE                            = "node.json"
   /** Name of stored dht file. */
   const val DHT_FILE                             = "dht.json"
}
