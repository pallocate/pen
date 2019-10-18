package pen.eco

import java.math.BigInteger

object HexEncoder
{
   /** Convertes a ByteArray to a hex encoded String. */
   fun encode (input : ByteArray) : String
   {
      var ret =""

      if (input.size > 0)
      {
         val bi = BigInteger( 1, input )
         ret = String.format( "%0" + (input.size shl 1) + "X", bi )
      }
      return ret
   }
}
