package pen.net.kad

   /* Networking constants */
object Constants
{
   val IS_TESTING                                 = true                        // Affects the servers simulated latency
   /** Name of stored service node files. */
   const val SERVICE_NODE_FILE                    = "kademliaNode.json"
   /** Name of stored routing table files. */
   const val ROUTING_TABLE_FILE                   = "routingtable.json"
   /** Name of stored node files. */
   const val NODE_FILE                            = "node.json"
   /** Name of stored dht files. */
   const val DHT_FILE                             = "dht.json"

   const val RESTORE_INTERVAL                     = (60*1000).toLong()          // (milliseconds)
   const val RESPONCE_TIMEOUT                     = 2000L
   const val OPERATION_TIMEOUT                    = 2000L
   const val MAX_CONCURRENT_MESSAGES_TRANSITING   = 10
   const val K                                    = 5
   const val STALE                                = 1
//   const val REPLACEMENT_CACHE_SIZE               = 3
}
