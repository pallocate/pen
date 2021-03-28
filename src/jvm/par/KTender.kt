package pen.par

import kotlinx.serialization.Serializable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import pen.KCrypto
import pen.Voidable
import pen.serializeToFile
import pen.deserializeFromFile
import pen.eco.KProposal
import pen.eco.KProposalEncoder
import pen.eco.KProposalDecoder

@Serializable
class KTender (val proposal : KProposal, val conceder : Long, val proposer : Long) : Voidable
{
   companion object
   {
      fun read (filename : String, crypto : KCrypto = KCrypto.void()) : KTender
      {
         var ret = KTender.void()
         try
         {
            if (crypto.isVoid())
               ret = deserializeFromFile<KTender>( filename, KTender.serializer() )!!
            else
            {
               /* Read from file */
               val input = DataInputStream(FileInputStream( filename ))
               val conceder = input.readLong()
               val proposer = input.readLong()
               val encryptedProposal = ByteArray( input.available() ).also {input.read( it )}

               /* Decrypt and deserialize proposal */
               val binaryProposal = crypto.decrypt( encryptedProposal )
               val byteArrayInputStream = ByteArrayInputStream( binaryProposal )
               val proposal = KProposalDecoder( byteArrayInputStream ).decodeSerializableValue( KProposal.serializer() )
               ret = KTender( proposal, conceder, proposer )
            }
         }
         catch (e : Exception)
         {println( "Read tender failed!" )}

         return ret
      }

      fun void () = KTender( KProposal.void(), 0L, 0L )
   }

   fun write (filename : String, crypto : KCrypto = KCrypto.void())
   {
      if (crypto.isVoid())
         serializeToFile<KTender>( this, filename, KTender.serializer() )
      else
      {
         /* Serialize and encrypt proposal */
         val byteArrayOutputStream = ByteArrayOutputStream()
         KProposalEncoder( byteArrayOutputStream ).encodeSerializableValue( KProposal.serializer(), proposal )
         val encryptedProposal = crypto.encrypt( byteArrayOutputStream.toByteArray() )

         /* Write to file */
         val output = DataOutputStream(FileOutputStream( filename ))
         output.writeLong( conceder )
         output.writeLong( proposer )
         output.write( encryptedProposal )
      }
   }

   override fun isVoid () = proposal.isVoid()
}
