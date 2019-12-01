package pen.eco

import java.net.InetAddress
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Converter
import pen.eco.Utils

class KByteArrayConverter : Converter
{
   override fun canConvert (cls : Class<*>) = (cls == ByteArray::class.java)
   override fun toJson (value : Any) : String
   {
      var ret = ""
      if (value is ByteArray)
      {
         ret = "{ \"bytes\" : \"${Utils.encodeB64( value )}\" }"
         pen.eco.Log.error( "Encoded value: " +  value )
      }

      return ret
   }

   override fun fromJson (jv : JsonValue) = Utils.decodeB64(jv.objString( "bytes" ))
}

class KInetAddressConverter : Converter
{
   override fun canConvert (cls : Class<*>) = (cls == InetAddress::class.java)
   override fun toJson (value : Any) : String
   {
      var ret = ""
      if (value is InetAddress)
         ret = "{ \"inetAdress\" : \"${value.getHostAddress()}\" }"

      return ret
   }

   override fun fromJson (jv : JsonValue) : InetAddress
   {
      var ret = InetAddress.getLocalHost()
      try
      {
         val tmp = InetAddress.getByName( jv.objString( "inetAdress" ) )
         ret = tmp
      }
      catch (e : Exception) {}

      return ret
   }
}
