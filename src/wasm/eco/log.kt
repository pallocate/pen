package pen.eco

/** Skeleton code for web assembly. */
actual fun log (message : String, severity : LogLevel) = println( "${severity.name}: $message" )
