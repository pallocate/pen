package pen.eco

enum class DebugValue : SettingsValue
{
   PLUGIN_MANAGER,

   /* Kademlia stuff */
   KAD_CREATE,
   KAD_SAVE_LOAD,
   KAD_INITIALIZE,
   KAD_BOOTSTRAP,
   KAD_INTERNAL,

   CONTACT_CONNECT,
   CONTACT_PUT,                                                                 // Lots of logging here
   CONTACT_GET,
   CONTACT_INFO,                                                                // Lots of logging here

   CONTENT_FIND,
   CONTENT_PUT_GET,
   CONTENT_INFO,

   MESSAGE_CREATE,
   MESSAGE_FIND_NODE,                                                           // Lots of logging here
   RECEIVER_TIMEOUT,

   SERVER_RECEIVERS,                                                            // Lots of logging here
   SERVER_INTERNAL,

   STREAMING                                                                    // Lots of logging here
}
