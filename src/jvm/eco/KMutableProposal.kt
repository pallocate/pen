package pen.eco

import java.io.File
import java.nio.file.Files
import pen.Log
import pen.Constants
import pen.WrongVersionException

class KMutableProposal  (
                           val header : KMutableHeader = KMutableHeader(),
                           val products : ArrayList<KQuantableProduct> = ArrayList<KQuantableProduct>( 0 )
                        ) : IniReader
{
   override val HEADER_KEY                      = "PROPOSAL"
   override val ITEM_KEY                        = "PRODUCT"

   constructor (proposal : KProposal) : this(KMutableHeader( proposal.header ))
   {
      for (item in proposal.items)
         products += KQuantableProduct( item )
   }

   override fun headerSection (map : HashMap<String, String>)
   {
      try
      {
         if (map.getOrElse( "version", {"0"} ).toInt() != Constants.VERSION) throw WrongVersionException()

         with( header ) {
            year = map.getOrElse( "year", {"0"} ).toInt()
            iteration = map.getOrElse( "iteration", {"0"} ).toInt()
            level = map.getOrElse( "level", {"0"} ).toInt()
            target = Target.fromInt(map.getOrElse( "target", {"0"} ).toInt())         }
      }
       catch (e : Exception)
      { Log.error( "Wrong version!" ) }
   }

   override fun itemSection (map : HashMap<String, String>)
   {
      try
      {
         val qty = map.getOrElse( "qty", {"0"} ).toLong()
         val id = map.getOrElse( "id", {"0"} ).toLong()
         val name = map.getOrElse( "name", {"None"} )
         val desc = map.getOrElse( "desc", {"None"} )
         val amount = map.getOrElse( "amount", {"0"} ).toFloat()
         val prefix = map.getOrElse( "prefix", {"None"} )
         val unit = map.getOrElse( "unit", {"None"} )
         val down = map.getOrElse( "down", {"0"} ).toInt()
         val up = map.getOrElse( "up", {"0"} ).toInt()
         val absolute = map.getOrElse( "absolute", {"0"} ).toLong()
         val price = map.getOrElse( "price", {"0"} ).toLong()
         val analogue = map.getOrElse( "analogue", {"false"} )

         if (id != 0L)
         {
            val product = KQuantableProduct( qty, id, name, desc, amount, prefix, unit, down, up, absolute, price, analogue )
            products.add( product )
         }
      }
      catch (e : Exception)
      { Log.warn( "Number conversion failiure!" ) }
   }

   fun toKProposal () : KProposal
   {
      val items = ArrayList<KItem>()

      for (item in items)
         items.add(KItem( item.qty, item.id ))

      return KProposal( header.toKHeader(), items )
   }

   fun load (filename : String) = read( filename )

   fun save (filename : String) : Boolean
   {
      var success = false
      Log.debug( "Saving proposal " )

      try
      {
         Files.write(File( filename ).toPath(), encode().toByteArray())
         success = true
      }
      catch (e : Exception)
      { Log.error( "Proposal save failiure!" ) }

      return success
   }

   fun encode () : String
   {
      var ret = header.encode()

      for (product in products)
         ret += ( product.encode() )

      return ret
   }
}
