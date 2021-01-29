package pen.par

interface Crypto

enum class Role
{
   NO_ROLE,
   ACQUAINTANCE,

   /* General roles */
   CONCEDER,
   PROPOSER,

   /* Inter council roles */
   SUPPLIER,
   CUSTOMER,

   /* Data security roles */
   COUNCIL_SIGNER,
   DATA_CONTROLLER,
   DATA_SUBJECT
}

