package pen.net.kad.dht

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.NoSuchElementException
import com.beust.klaxon.Converter
import pen.eco.Log.Level.WARN
import pen.eco.Loggable
import pen.eco.DebugValue
import pen.eco.common.Directory
import pen.eco.Config
import pen.eco.Config.getSettings
import pen.eco.Config.SLASH
import pen.eco.KSerializer
import pen.eco.Convertable
import pen.net.kad.KKademliaNode
import pen.net.kad.StorageEntry
import pen.net.kad.NoStorageEntry
import pen.net.kad.ContentNotFoundException
import pen.net.kad.node.KNodeId
import pen.net.kad.utils.KNodeIdConverter

/** Distributed Hash Table implementation. */
class KDHT () : Convertable, Loggable
{
   val contentManager = KContentManager()
   var ownerName = ""

   init
   { log("created", getSettings().getValue( DebugValue.KAD_CREATE )) }

   fun initialize (name : String)
   {
      ownerName = name
      log("initializing", getSettings().getValue( DebugValue.KAD_INITIALIZE ))
   }

   fun store (content : KStorageEntry) : Boolean
   {
      log("adding content [${content.contentMetadata.key.shortName()}]", getSettings().getValue( DebugValue.CONTENT_PUT_GET ), WARN)
      var ret : Boolean

      if (contentManager.contains( content.contentMetadata ))
      {
         val current = contentManager.get( content.contentMetadata )
         if (current is KStorageEntryMetadata)
         {
            current.updateLastRepublished()

            if (current.lastUpdated >= content.contentMetadata.lastUpdated)
               return false                                                     // Content is up to date
            else
               remove( content.contentMetadata )                                // Delete content in order update it
         }
      }

      /** Adds new or updated content. */
      try
      {
         val sEntry = contentManager.put( content.contentMetadata )

         val fileWriter = FileWriter( contentStorageDir( content.contentMetadata.key ) + SLASH + sEntry.hashCode() + ".json" )
         KSerializer.write( content, fileWriter )                                // Store content to file
         fileWriter.close()

         ret = true
      }
      catch (e: Exception)
      {
         log("adding content failed! [${content.contentMetadata.key.shortName()}], ${e.message}", getSettings().getValue( DebugValue.CONTENT_PUT_GET ), WARN)
         ret = false
      }

      return ret
   }

   fun store (content : KContent) = store(KStorageEntry( content ))

   fun retrieve (key : KNodeId, hashCode : Int) : StorageEntry
   {
      var ret : StorageEntry = NoStorageEntry()

      val fileReader = FileReader( contentStorageDir( key ) + SLASH + hashCode + ".json" )
      val readResult = KSerializer.read<KDHT>( fileReader )

      if (readResult is StorageEntry)
         ret = readResult

      return ret
   }

   fun contains (param : KGetParameter) = contentManager.contains( param )

   fun get (entry : KStorageEntryMetadata) : StorageEntry
   {
      var ret : StorageEntry = NoStorageEntry()

      val storageEntry = retrieve( entry.key, entry.hashCode() )
      if (storageEntry is KStorageEntry)
         ret = storageEntry

      return ret
   }

   /* Loads content if any. */
   fun get (param : KGetParameter) : StorageEntry
   {
      var ret : StorageEntry = NoStorageEntry()

      val meta = contentManager.get( param )
      if (meta is KStorageEntryMetadata)
      {
         val storageEntry = retrieve( meta.key, meta.hashCode() )
         if (storageEntry is KStorageEntry)
            ret = storageEntry
      }

      return ret
   }

   fun remove (content : KContent)
   { remove(KStorageEntryMetadata( content )) }

   fun remove (entry : KStorageEntryMetadata)
   {
      val folder = contentStorageDir( entry.key )
      val file = File(folder + SLASH + entry.hashCode() + ".json" )

      contentManager.remove( entry )

      if (file.exists())
         file.delete()
      else
         throw ContentNotFoundException()
   }

   /** The first 2 characters of the content id is used as the directory name. */
   private fun contentStorageDir (nodeId : KNodeId) = KKademliaNode.storageDir(ownerName, nodeId.toString().substring( 0, 2 ))

   fun getStorageEntries () = contentManager.allEntries()
   fun putStorageEntries (ientries : MutableList<KStorageEntryMetadata>)
   {
      for (e in ientries)
         contentManager.put( e )
   }

   override fun loggingName () = "KDHT(${ownerName})"

   @Synchronized
   override fun toString () = contentManager.toString()
   override fun getConverters () = arrayOf( KNodeIdConverter() as Converter )
}
