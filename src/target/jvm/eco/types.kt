package pen.eco.types

import java.time.Instant

actual fun epoch_second () = Instant.now().getEpochSecond()
