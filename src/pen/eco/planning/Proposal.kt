package pen.eco.planning

import java.io.File
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths
import pen.eco.common.Log
import pen.eco.common.Crypto
import pen.eco.common.FileInput
import pen.eco.common.PasswordProvider
import pen.eco.common.NoInputStream
import pen.eco.common.FileOutput
import pen.eco.common.NoOutputStream
import pen.eco.planning.codecs.BlockTreeEncoder
import pen.eco.planning.codecs.ProposalDecoder
import pen.eco.planning.codecs.ProposalEncoder

/** A economic proposal. */
class Proposal : Block()
{
   /** Encodes and signes the proposal. */
   override fun signed (passwordProvider : PasswordProvider, salt : ByteArray) : ByteArray
   {
      BlockTreeEncoder.sizesAndHashes( this )                                   // To ensure header info is up to date and it is hashed.
      val text = ProposalEncoder( this ).encode( true )
      return text + Crypto.signText( hash, passwordProvider, salt )
   }

   fun load (filename : String) : Boolean
   {
      var success = false
      val inputStream = FileInput( filename ).getInputStream()
      val proposalDecoder = ProposalDecoder( this )
      Log.debug( "Loading proposal file $filename" )

      if (!(inputStream is NoInputStream))
      {
         success = proposalDecoder.read( inputStream )                          // read() is used to get the success
         inputStream.close()
         if (success)
            Log.info( "Proposal loaded" )
      }

      return success
   }

   fun save (filename : String) : Boolean
   {
      var success = false
      val outputStream = FileOutput( filename ).getOutputStream()
      val proposalEncoder = ProposalEncoder( this )
      Log.debug( "Saving proposal ${idString()}" )

      if (!(outputStream is NoOutputStream))
      {
         try
         {
            outputStream.write( proposalEncoder.encode() )
            success = true
         }
         catch (e : Exception)
         { Log.err( "Save proposal failed!" ) }
         finally
         { outputStream.close() }
      }

      return success
   }
}
