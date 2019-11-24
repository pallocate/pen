package pen.net.kad

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException
import java.util.Random
import java.util.Timer
import java.util.TimerTask
import pen.eco.LogLevel.INFO
import pen.eco.LogLevel.WARN
import pen.eco.LogLevel.ERROR
import pen.eco.Loggable
import pen.net.kad.messages.MessageFactory
import pen.net.kad.messages.Message
import pen.eco.Config
import pen.net.kad.NodeMessageListener
import pen.net.kad.NoNodeMessageListener
import pen.net.kad.messages.Codes
import pen.net.kad.messages.KFindNodeMessage
import pen.net.kad.messages.KFindNodeReply
import pen.net.kad.messages.receivers.ReceiverFactory
import pen.net.kad.messages.receivers.Receiver
import pen.net.kad.messages.receivers.NoReceiver
import pen.net.kad.node.KNode
import pen.net.kad.utils.KMessageSerializer

/** The server that handles sending and receiving messages between nodes on the Kad Network.
     * @param port The port to listen on
     * @param receiverFactory Factory used to create receivers
     * @param node Local node on which this server runs on
     * @param stats A stats to manage the server statistics */
class KServer () : Loggable
{
   /** Maximum size of a Datagram Packet */
   private val DATAGRAM_BUFFER_SIZE = 64*1024                                   // 64KB

   private val tasks = HashMap<Int, TimerTask>()                                // Keep track of scheduled tasks
   private val receivers = HashMap<Int, Receiver>()
   private val timer = Timer( true )                                            // Schedule future tasks
   private var socket : DatagramSocket? = null

   var isRunning : Boolean = false
   var localNode : KKademliaNode? = null
   var nodeMessageListener : NodeMessageListener = NoNodeMessageListener()

   init
   { log( "created", Config.trigger( "KAD_CREATE" ), INFO)}

   fun initialize (localNode : KKademliaNode, port : Int)
   {
      this.localNode = localNode
      log("initializing", Config.trigger( "KAD_INITIALIZE" ))

      try
      {socket = DatagramSocket( port )}
      catch (e : Exception)
      { log("create socket failed!", Config.trigger( "KAD_CREATE" ), ERROR) }

      if (socket != null)
      {
         startListener()                                                        // Start listening for incoming requests in a new thread
         isRunning = true
      }
   }

   /** Starts the listener to listen for incoming messages */
   private fun startListener ()
   {
      object : Thread()
      {
         override fun run ()
         { listen() }
      }.start()
   }

   /** Sends a message.
     * @param responseReceiver The receiver to handle the response message.
     * @return The conversation ID. */
   @Synchronized
   fun sendMessage (recipient : KNode, message : Message, responseReceiver : Receiver) : Int
   {
      var conversationID = 0

      if (isRunning)
      {
         conversationID = Random().nextInt()
         if (responseReceiver !is NoReceiver)
         {
            // Setting up the receiver
            log("putting receiver", Config.trigger( "KAD_SERVER_RECEIVERS" ))
            receivers.put( conversationID, responseReceiver )
            val task = TimeoutTask( conversationID, responseReceiver )
            timer.schedule( task, Constants.RESPONCE_TIMEOUT )
            tasks.put( conversationID, task )
         }
         sendMessage( recipient, message, conversationID )
      }
      else
         log("server down!", Config.trigger( "KAD_SERVER_RECEIVERS" ), ERROR)

      return conversationID
   }

   private fun sendMessage (recipient : KNode, message : Message, conversationID : Int)
   {
      try
      {
         ByteArrayOutputStream().use( { baus -> DataOutputStream( baus ).use(
         {
            /* Setting up the message. */
            it.writeInt( conversationID )
            baus.write( message.code().toInt() )
            KMessageSerializer.write( message, baus )
            val msg = baus.toByteArray()

            if (DATAGRAM_BUFFER_SIZE >= msg.size)
            {
               /* Creating and sending the packet. */
               val pkt = DatagramPacket( msg, 0, msg.size )
               pkt.setSocketAddress( recipient.getSocketAddress() )
               socket?.send( pkt )

               /* Updating stats. */
               Stats.sentData( msg.size.toLong() )

               /* Monitoring find node sent. */
               if (message is KFindNodeMessage && nodeMessageListener !is NoNodeMessageListener)
                  nodeMessageListener.findMessageSent()
            }
         })})
      }
      catch (e : Exception)
      { log("send message failed!", Config.trigger( "KAD_MSG_CREATE" ), WARN) }
   }

   /** Replies to a received message. */
   @Synchronized
   fun reply (recipient : KNode, message : Message, conversationID : Int)
   {
      if (isRunning)
         sendMessage( recipient, message, conversationID )
      else
         log("server down!", Config.trigger( "KAD_SERVER_INTERNAL" ), ERROR)
   }

   /** Listens for incoming messages in a separate thread. */
   private fun listen ()
   {
      log("listening to messages", Config.trigger( "KAD_SERVER_INTERNAL" ))
      try
      {
         while (isRunning)                                                      // Wait for a packet
         {
            val buffer = ByteArray( DATAGRAM_BUFFER_SIZE )
            val packet = DatagramPacket( buffer, buffer.size )

            socket?.receive( packet )
            Stats.receivedData( packet.getLength().toLong() )                   // Update stats

            if (Constants.IS_TESTING)
            {
               val pause = packet.getLength()/100                               // Simulate network latency

               try
               {Thread.sleep( pause.toLong() )}
               catch (e: InterruptedException) {}
            }

            /* Packet received. */
            ByteArrayInputStream( packet.getData(), packet.getOffset(), packet.getLength() ).use( { bais -> DataInputStream( bais ).use(
            {
               val conversationID : Int = it.readInt()
               val messageCode : Byte = bais.read().toByte()
               val message = MessageFactory.createMessage( messageCode, bais )

               /* Monitoring find node reply received. */
               if (message is KFindNodeReply && nodeMessageListener !is NoNodeMessageListener)
                  nodeMessageListener.findReplyReceived()

               /* Getting a receiver for this message. */
               var rec : Receiver? = null
               if (receivers.containsKey( conversationID ))
               {
                  /* If there is a reciever in the receivers to handle this. */
                  synchronized( this,
                  {
                     rec = receivers.remove( conversationID )
                     val task = tasks.remove( conversationID ) as TimerTask
                     task.cancel()
                  })
               }
               else
               {
                  /* There is currently no receivers, try to get one. */
                  checkNotNull( localNode )
                  rec = ReceiverFactory.createReceiver( messageCode, localNode!! )
               }

               rec?.receive( message, conversationID )
            })})
         }
      }
      catch (e : IOException)
      { log("message listening failed!", Config.trigger( "KAD_SERVER_INTERNAL" ), WARN) }
      finally
      {
         socket?.close()
         this.isRunning = false
      }
   }

   /** Remove a conversation receiver */
   @Synchronized
   private fun unregister (conversationID : Int)
   {
      log("unregistring receiver/task", Config.trigger( "KAD_SERVER_INTERNAL" ))
      receivers.remove( conversationID )
      tasks.remove( conversationID )
   }

   /** Stops listening and shuts down the server */
   @Synchronized
   fun shutdown ()
   {
      log("shutting down!", Config.trigger( "KAD_SERVER_INTERNAL" ), INFO)
      isRunning = false
      socket?.close()
      timer.cancel()
   }

   fun printReceivers ()
   {
      println( "Server receivers.." )
      for (r in receivers.keys)
         println( "Receiver ($r): ${receivers[r]}" )
   }

   override fun originName () = if (localNode == null)
                                    "KServer"
                                 else
                                    "KServer(${localNode!!.getNode()})"

   /** Task that gets called by a separate thread if a timeout for a receiver occurs.
     * When a reply arrives this task must be canceled using the `cancel()`
     * method inherited from `TimerTask`. In this case the caller is
     * responsible for removing the task from the `tasks` map. */
   internal inner class TimeoutTask (private val conversationID: Int, private val receiver: Receiver) : TimerTask()
   {
      override fun run()
      {
         log("TimeoutTask running", Config.trigger( "KAD_SERVER_INTERNAL" ))

         if (isRunning)
         {
            try
            {
               unregister( conversationID )
               receiver.timeout( conversationID )
            }
            catch (e : IOException)
            { log( "TimeoutTask failed! (${e.message})", Config.trigger( "KAD_SERVER_INTERNAL" ), WARN) }
         }
      }
   }
}
