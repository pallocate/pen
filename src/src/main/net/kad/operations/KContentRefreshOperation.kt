package pen.net.kad.operations

import java.io.IOException
import pen.eco.Log

import pen.eco.Config
import pen.net.kad.Constants
import pen.net.kad.KServer
import pen.net.kad.KKademliaNode
import pen.net.kad.messages.Message
import pen.net.kad.dht.KDHT
import pen.net.kad.dht.KStorageEntry
import pen.net.kad.messages.receivers.NoReceiver
import pen.net.kad.messages.KStoreMessage
import pen.net.kad.node.KNode
import pen.net.kad.routing.KRoutingTable

/** Refresh/Restore the data on this node by sending the data to the K-Closest nodes to the data */
class KContentRefreshOperation (private val server : KServer, private val node : KNode, private val routingTable : KRoutingTable, private val dht : KDHT) : Operation
{
   /** For each content stored on this DHT, distribute it to the K closest nodes
     * Also delete the content if this node is no longer one of the K closest nodes
     *
     * We assume that our JKademliaRoutingTable is updated, and we can get the K closest nodes from that table */
   @Throws( IOException::class )
   override fun execute ()
   {
      /* Get a list of all storage entries for content */
      val entries = dht.getStorageEntries()

      /* If a content was last republished before this time, then we need to republish it */
      val minRepublishTime = System.currentTimeMillis()/1000L - Constants.RESTORE_INTERVAL

      /* For each storage entry, distribute it */
      for (e in entries)
      {
         /* Check last update time of this entry and only distribute it if it has been last updated > 1 hour ago */
         if (e.lastRepublished > minRepublishTime)
            continue

         /* Set that this content is now republished */
         e.updateLastRepublished()

         /* Get the K closest nodes to this entries */
         val closestNodes = routingTable.findClosest( e.key, Constants.K )

         /* Create the message */
         val storageEntry = dht.get( e )
         if (storageEntry is KStorageEntry)
         {
            val msg = KStoreMessage( node, storageEntry )

            /*KStoreMessage the message on all of the K-Nodes*/
            for (n in closestNodes)
            {
               /*We don't need to again store the content locally, it's already here*/
               if (!n.equals( node ))                          // Send a contentstore operation to the K-Closest nodes
                  server.sendMessage( n, msg, NoReceiver() )
            }

            /* Delete any content on this node that this node is not one of the K-Closest nodes to */
            try
            {
               if (!closestNodes.contains( node ))
                  dht.remove( e )
            }
            catch (e : Exception)
            {
               /* It would be weird if the content is not found here */
               Log.err( "KContentRefreshOperation(${node})- remove local content failed!" )
            }
         }
         else
            Log.warn({ "KContentRefreshOperation(${node})- entry not found!"}, Config.flag( "CONTENT_PUT_GET" ))
      }
   }
}
