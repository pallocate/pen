package pen.par

import kotlinx.serialization.Serializable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import pen.serializeToFile
import pen.deserializeFromFile
import pen.eco.KProposal
import pen.eco.KProposalEncoder
import pen.eco.KProposalDecoder

//expect fun readTender () : 

@Serializable
class KTender (val proposal : KProposal, val conceder : Long, val proposer : Long)
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
               val dataInputStream = DataInputStream(FileInputStream( filename ))
               val conceder = dataInputStream.readLong()
               val proposer = dataInputStream.readLong()

               val binaryProposal = crypto.aes().decryptFromStream( dataInputStream )
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
         val dataOutputStream = DataOutputStream(FileOutputStream( filename ))
         dataOutputStream.writeLong( conceder )
         dataOutputStream.writeLong( proposer )

         val byteArrayOutputStream = ByteArrayOutputStream()
         KProposalEncoder( byteArrayOutputStream ).encodeSerializableValue( KProposal.serializer(), proposal )

         crypto.aes().encryptToStream( byteArrayOutputStream.toByteArray(), dataOutputStream )
      }
   }
}

