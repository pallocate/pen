package pen.par

import kotlinx.serialization.Serializable
import pen.Voidable
import pen.serializeToFile
import pen.deserializeFromFile
import pen.eco.KProductQuantities

@Serializable
class KTender (val productQuantities : KProductQuantities, val conceder : Long, val proposer : Long) : Voidable
{
   companion object
   {
      fun read (filename : String) : KTender
      {
         var ret = KTender.void()
         try
         { ret = deserializeFromFile<KTender>( filename, KTender.serializer() )!! }
         catch (e : Exception)
         {println( "Read tender failed!" )}

         return ret
      }

      fun void () = KTender( KProductQuantities.void(), 0L, 0L )
   }

   fun write (filename : String) = try {
      serializeToFile<KTender>( this, filename, KTender.serializer() )
   }
   catch (e : Exception)
   {println( "Write tender failed!" )}
   
   override fun isVoid () = productQuantities.isVoid()
}
