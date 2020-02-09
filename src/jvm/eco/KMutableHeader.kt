package pen.eco

import pen.Constants
import kotlinx.serialization.Serializable

/** A mutable proposal header. */
@Serializable
class KMutableHeader(
                        /** User id */
                        override var id : Long                                  = 0L,
                        /** The year of the planning process. */
                        override var year : Int                                 = 0,
                        /** Iteration number of the planning process. */
                        override var iteration : Int                            = 0,
                        /** Consumption or production target. */
                        override var target : Target                            = Target.UNDEFINED,
                       /** Epoch seconds timestamp. */
                        override var timestamp : Long                           = 0L,
                        /** Spec version. */
                        override val version : Int                              = Constants.VERSION
                     ) : Header
{
   constructor (header : KHeader) : this( header.id, header.year, header.iteration, header.target, header.timestamp ) {}

   fun vanilla ()
   {
      id = 0L
      year = 0
      iteration = 0
      target = Target.UNDEFINED
      timestamp = 0L
   }

   fun toKHeader () = KHeader( id, year, iteration, target, timestamp )
}
