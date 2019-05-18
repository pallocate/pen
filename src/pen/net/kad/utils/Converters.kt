package pen.net.kad.utils

import java.io.StringReader
import java.io.StringWriter
import java.net.InetAddress
import com.beust.klaxon.JsonValue
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Converter
import pen.eco.common.Utils
import pen.eco.common.PlaceHolder
import pen.net.kad.routing.KRoutingTable
import pen.net.kad.node.KNodeId

class KNodeIdConverter : Converter
{
   override fun canConvert (cls : Class<*>) = cls == KNodeId::class.java
   override fun toJson (value : Any) : String
   {
      var ret = ""
      if (value is KNodeId)
         ret = "{ \"keyBytes\" : \"${Utils.byteArrayToB64String( value.keyBytes )}\" }"

      return ret
   }

   override fun fromJson (jv : JsonValue) : KNodeId
   {
      val ret = KNodeId()
      ret.keyBytes = Utils.B64StringToByteArray( jv.objString( "keyBytes" ) )

      return ret
   }
}

class KInetAddressConverter : Converter
{
   override fun canConvert (cls : Class<*>) = cls == InetAddress::class.java
   override fun toJson (value : Any) : String
   {
      var ret = ""
      if (value is InetAddress)
         ret = "{ \"inetAdress\" : \"${value.getHostAddress()}\" }"

      return ret
   }

   override fun fromJson (jv : JsonValue) : InetAddress
   {
      var ret = InetAddress.getByName( "127.0.1.1" )
      try
      {
         val tmp = InetAddress.getByName( jv.objString( "inetAdress" ) )
         ret = tmp
      }
      catch (e : Exception) {}

      return ret
   }
}
