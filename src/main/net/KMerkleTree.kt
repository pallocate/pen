package pen.net

import pen.Loggable
import pen.Config
import pen.Crypto
import pen.Hashable

/** A simple binary indexed merkle tree implementation, uses an array as backing structure. The initial capacity is two MerkleLeaf´s. *//*
 _                                                        _
|                         16                               |
|            8                           24                |
|       4          12             20             28        |
|    2    6     10    14      18     22      26      30    |
|_0 1 3  5 7   9 11  13 15  17 19  21  23  25  27  29  31 _|
  ^
  Index zero is not used. */

/* TODO: imutabillity or freeze() or ThreadLocal */
open class KMerkleTree : Loggable
{
   var storage : Array<MerkleNode> = arrayOf( NoMerkleNode(), NoMerkleNode(), KMerkleNode(), NoMerkleNode() )
   var rootIdx = 2                                                              // Root index
   var lastIdx = -1                                                             // Index of last added leaf

   /** Calculates hashes recursively.
     * @param nodeIdx Up to what node to calculate hashes. */
   fun hashes (nodeIdx : Int = rootIdx, updateHashes : Boolean = false) : ByteArray
   {
      log("Calculates merkle tree hashes", Config.trigger( "MERKLE_TREE" ))
      return hashesHelper( nodeIdx, nodeIdx, updateHashes )
   }
   private fun hashesHelper (nodeIdx : Int, leftmost : Int, updateHashes : Boolean) : ByteArray
   {
      var ret = ByteArray( 0 )
      val node = storage[nodeIdx]
      val leftIdx = nodeIdx - leftmost/2
      val rightIdx = nodeIdx + leftmost/2

      if (nodeIdx >= 1 && nodeIdx < storage.size)
      {
         if (nodeIdx % 2 == 0)                                                  // Non leafs are evenly indexed
         {
            if (storage[leftIdx] is KMerkleNode)
               ret = hashesHelper( leftIdx, leftmost/2, updateHashes )          // Dive down left branch

            if (storage[rightIdx] is KMerkleNode)
               ret = ret + hashesHelper( rightIdx, leftmost/2, updateHashes )   // Dive down right branch
         }
         else                                                                   // Leafs are oddly indexed
            if (node is Hashable)
               ret = node.hash()

         if (ret.size > 0)
            ret = Crypto.digest( ret )

         if (updateHashes && node is KMerkleNode)
            node.storedHash = ret
      }

      return ret
   }

   fun add (merkleNode : KMerkleNode)
   {
      log("Adding ${merkleNode::class.simpleName} to merkle tree", Config.trigger( "MERKLE_TREE" ))
      lastIdx += 2

      if (lastIdx > rootIdx*2)
         doubleCapacity()

      storage[lastIdx] = merkleNode
   }

   private fun doubleCapacity ()
   {
      log("Doubling merkle tree capacity", Config.trigger( "MERKLE_TREE" ))
      rootIdx = rootIdx*2
      var i = 0
      val tmp = Array<MerkleNode>(rootIdx*2, { if (i++ % 2 == 0) KMerkleNode() else NoMerkleNode() }) // Create a new array

      for (n in 0 until storage.size)
         tmp[n] = storage[n]                                                    // Copy from existing array

      storage = tmp
   }

   override fun toString () : String
   {
      val stringBuilder = StringBuilder()

      for (node in storage)
         stringBuilder.append( node::class.simpleName + ", " )

      return stringBuilder.toString()
   }

   override fun originName () = "KMerkleTree"
}
