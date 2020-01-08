package pen.eco

import pen.Constants
import kotlinx.serialization.Serializable

/** A mutable proposal header. */
@Serializable
class KMutableHeader(
                        /** Spec version. */
                        override var version : Int                              = Constants.VERSION,
                        /** User id */
                        override var id : Long                                  = 0L,
                        /** The year of the planning process. */
                        override var year : Int                                 = 0,
                        /** Iteration number of the planning process. */
                        override var iteration : Int                            = 0,
                        /** Federative level in the economy. */
                        override var level : Int                                = 0,
                        /** Flags */
                        override var target : Target                            = Target.UNDEFINED,
                       /** Epoch seconds timestamp. */
                        override var timestamp : Long                           = 0L
                     ) : Header
{
   constructor (header : KHeader) : this( header.version, header.id, header.year, header.iteration, header.level, header.target, header.timestamp ) {}

   fun vanilla ()
   {
      version = Constants.VERSION
      id = 0L
      year = 0
      iteration = 0
      level = 0
      target = Target.UNDEFINED
      timestamp = 0L
   }

   fun toKHeader () = KHeader( version, id, year, iteration, level, target, timestamp )
}
