package pen.net.kad.dht

import pen.eco.LogLevel.INFO
import pen.eco.LogLevel.WARN
import pen.eco.Loggable
import pen.eco.DebugValue
import pen.eco.Config.getSettings
import pen.net.kad.StorageEntryMetadata
import pen.net.kad.NoStorageEntryMetadata
import pen.net.kad.node.KNodeId

class KContentManager : Loggable
{
   val entries = HashMap<KNodeId, ArrayList<KStorageEntryMetadata>>()

   fun put (content : KContent) = put(KStorageEntryMetadata( content ))

   fun put (entry : KStorageEntryMetadata) : StorageEntryMetadata
   {
      log("putting content [${entry.key.shortName()}]", getSettings().getValue( DebugValue.CONTENT_PUT_GET ))
      log({"content: {owner: ${entry.ownerName}}, {type: ${entry.type}}, {key: \"${entry.key}\"}"}, getSettings().getValue( DebugValue.CONTENT_INFO ))
      var ret : StorageEntryMetadata = NoStorageEntryMetadata()

      if (!entries.containsKey( entry.key ))
      {
         entries.put( entry.key, ArrayList<KStorageEntryMetadata>() )
      }

      /* If this entry doesn't already exist, then we add it */
      if (contains( entry ))
         log("content already exist [${entry.key.shortName()}]", getSettings().getValue( DebugValue.CONTENT_PUT_GET ), INFO)
      else
      {
         entries.get( entry.key )?.add( entry )
         ret = entry
      }

      return ret
   }

   @Synchronized
   fun contains (entry : KStorageEntryMetadata) = contains(KGetParameter( entry ))

   @Synchronized
   fun contains (content : KContent) = contains(KGetParameter( content ))

   @Synchronized
   fun contains (kGetParameter : KGetParameter) : Boolean
   {
      var ret = false

      if (entries.containsKey( kGetParameter.key) )
      {
         /* Content with this key exist, check if any match the rest of the search criteria */
         for (e in entries.get( kGetParameter.key )!!)
         {
            /* If any entry satisfies the given parameters, return true */
            if (e.satisfiesParameters( kGetParameter ))
               ret = true
         }
      }

      return ret
   }

   fun get (md : KStorageEntryMetadata) = get(KGetParameter( md ))
   fun get (kGetParameter : KGetParameter) : StorageEntryMetadata
   {
      log("getting entry [${kGetParameter.key.shortName()}]", getSettings().getValue( DebugValue.CONTENT_PUT_GET ))
      log({"entry info: {owner: ${kGetParameter.ownerName}}, {type: ${kGetParameter.type}}, {key: \"${kGetParameter.key}\"}"}, getSettings().getValue( DebugValue.CONTENT_INFO ))
      var ret : StorageEntryMetadata = NoStorageEntryMetadata();

FUN@  {
         if (entries.containsKey( kGetParameter.key ))
         {
            /* Content with this key exist, check if any match the rest of the search criteria */
            for (entry in entries.get( kGetParameter.key )!!)
               /* If any entry satisfies the given parameters, return true */
               if (entry.satisfiesParameters( kGetParameter ))
               {
                  ret = entry
                  return@FUN
               }

            /* If we got here, means we didn't find any entry */
            log("entry not found! [${kGetParameter.key.shortName()}]", getSettings().getValue( DebugValue.CONTENT_PUT_GET ), WARN)
         }
         else
            log("no entry found for key! \"${kGetParameter.key.shortName()}\"", getSettings().getValue( DebugValue.CONTENT_PUT_GET ), INFO)
      }

      return ret
   }

   @Synchronized
   fun allEntries () : ArrayList<KStorageEntryMetadata>
   {
      val entriesRet = ArrayList<KStorageEntryMetadata>()

      for (entrySet in entries.values)
         if (!entrySet.isEmpty())
            entriesRet.addAll( entrySet )

      return entriesRet
   }

   fun remove (content : KContent) = remove(KStorageEntryMetadata( content ))
   fun remove (entry : KStorageEntryMetadata)
   {
      log("removing entry [${entry.key.shortName()}]", getSettings().getValue( DebugValue.CONTENT_PUT_GET ))
      if (contains( entry ))
         entries.get( entry.key )?.remove( entry )
      else
         log("remove entry failed! [${entry.key.shortName()}]", getSettings().getValue( DebugValue.CONTENT_PUT_GET ), WARN)
   }
   override fun loggingName () = "KContentManager"

   @Synchronized
   override fun toString () : String
   {
      val sb = StringBuilder( "Stored Content: \n" )
      var count = 0

      for (es in entries.values)
      {
         if (entries.size < 1)
            continue

         for (e in es)
         {
            sb.append( ++count )
            sb.append( ". " )
            sb.append( e.toString() )
            sb.append( "\n" )
         }
      }

      sb.append( "\n" )
      return sb.toString()
   }
}
