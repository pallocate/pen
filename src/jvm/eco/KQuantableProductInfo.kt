package pen.eco

class KQuantableProductInfo (id : Long = 0L, name : String = "", desc : String = "", amount : Float = 0F,
prefix : Prefix = Prefix.NONE, unit : Units = Units.NONE, change : Int = 0, price : Long = 0L, sensitive : Boolean = false,
analogue : Boolean = false) : KProductInfo( id, name, desc, amount, prefix, unit, change, price, sensitive, analogue )
{
   var qty = 0L

   constructor (productInfo : KProductInfo, qty : Long = 0L) : this (
      productInfo.id,
      productInfo.name,
      productInfo.desc,
      productInfo.amount,
      productInfo.prefix,
      productInfo.unit,
      productInfo.change,
      productInfo.price,
      productInfo.sensitive,
      productInfo.analogue
   )
   { this.qty = qty }
}
