package pen.eco.types.proposal

data class KItem (
                     /** Product id */
                     val id : Long = 0L,
                     /** Quantity */
                     val qty : Long= 0L
                  )
{
   /** Encodes item to text. */
   override fun toString () = "\n" + "[PRODUCT]\nId:         ${id}\nQty:        ${qty}\n"
}
