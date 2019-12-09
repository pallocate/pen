package pen.eco.types

import pen.eco.Constants

/** A mutable block header. */
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
                        override var function : EconomicFunction                = EconomicFunction.UNDEFINED,
                       /** Epoch seconds timestamp. */
                        override var timestamp : Long                           = 0L
                     ) : Header
{
   constructor (header : KHeader) : this( header.version, header.id, header.year, header.iteration, header.level, header.function, header.timestamp ) {}

   fun vanilla ()
   {
      version = Constants.VERSION
      id = 0L
      year = 0
      iteration = 0
      level = 0
      function = EconomicFunction.UNDEFINED
      timestamp = 0L
   }

   fun toKHeader () = KHeader( version, id, year, iteration, level, function, timestamp )
}
