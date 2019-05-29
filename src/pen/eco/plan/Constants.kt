package pen.eco.plan

import pen.eco.Config

object Constants
{
   /* Flags */
   /** If it is a production block, otherwise consumption. */
   const val IS_PRODUCTION                        = 1
   /** If it is a proposal (it only has Product children). */
   const val IS_PROPOSAL                          = 2
   /** If the proposal contains sensetive information. */
   const val HAS_SENSETIVE_DATA                   = 4
   //const val IS_ADJUSTMENT                        = 8

   /** Size of the public key asociated to this block. */
   const val PUBKEY_BYTES                         = 32
   /** Size of block header in bytes. */
   const val HEADER_BYTES                         = 32
   /** Size of block meta information (hash + public key + header + hash) in bytes. */
   const val META_BYTES                           = Config.SIGN_BYTES + Config.HASH_BYTES + HEADER_BYTES + PUBKEY_BYTES

   /* Number of bytes used for different purposes in a Block. */
   const val SIZE_BYTES                           = 6
   const val BLOCK_ID_BYTES                       = 5
   const val LINK_BYTES                           = 6
   const val YEAR_BYTES                           = 2
   const val VERSION_BYTES                        = 2
   const val NR_OF_CHILDREN_BYTES                 = 4
   const val TIMESTAMP_BYTES                      = 4
   const val ONE_BYTE                             = 1
   const val PRODUCT_ID_BYTES                     = 6
   const val PRODUCT_QTY_BYTES                    = 4
}
