package pen.eco

data class KItem (
                     /** Product id */
                     val id : Long = 0L,
                     /** Quantity */
                     val qty : Long= 0L
                  )
{
   /** Encodes item to text. */
   fun encode () = "\n" + "[PRODUCT]\nId:         ${id}\nQty:        ${qty}\n"
}