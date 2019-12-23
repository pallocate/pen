package pen

import java.net.InetAddress
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Converter
import pen.Utils
import pen.par.*

/*
class KRoleConverter : Converter
{
   companion object
   {
      var first = true
   }

   override fun canConvert (cls : Class<*>) = (cls == Role::class.java || cls == Roles::class.java)

   override fun toJson (value : Any) : String
   {
      val stringBuilder = StringBuilder( "" )

      if (value is Roles)
      {
         if (!first)
            stringBuilder.append( "," )
         first = false

         stringBuilder.append( " ${value.ordinal}" )
      }

      return stringBuilder.toString()
   }

   override fun fromJson (jv : JsonValue) : Roles
   {
      var ret : Role = NoRole()

      val value = jv.objString( "name" )
      val v = jv.objString( "icon" )


      ret = when (value)
      {
         "Test paricipant" ->
            TestParticipant()
         else -> NoRole()
      }

      return ret
   }
}
*/

class KByteArrayConverter : Converter
{
   override fun canConvert (cls : Class<*>) = (cls == ByteArray::class.java)
   override fun toJson (value : Any) : String
   {
      var ret = ""
      if (value is ByteArray)
         ret = "{ \"bytes\" : \"${Utils.encodeB64( value )}\" }"

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
