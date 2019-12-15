package pen.eco

class KQuantableProduct (
                           var qty : Long        = 0L,
                           id : Long             = 0L,
                           name : String         = "",
                           desc : String         = "",
                           amount : Float        = 0F,
                           prefix : String       = "",
                           unit : String         = "",
                           down : Int            = 0,
                           up : Int              = 0,
                           absolute : Long       = 0L,
                           price : Long          = 0L,
                           sensetive : String    = "",
                           analogue : String     = ""
                        ) : KProduct( id, name, desc, amount, prefix, unit, down, up, absolute, price, sensetive, analogue )
{

   constructor (quantity : Long = 0L, product : KProduct) : this
   (
      quantity,
      product.id,
      product.name,
      product.desc,
      product.amount,
      product.prefix,
      product.unit,
      product.down,
      product.up,
      product.absolute,
      product.price,
      product.sensetive,
      product.analogue
   ) {}

   constructor (item : KItem) : this( id = item.id ) { qty = item.qty }

   override fun encode () : String
   {
      var ret = super.encode()
      ret += "Qty:        ${qty}\n"

      return ret
   }
}
