package pen.net.bc.mt

import java.time.Instant

actual fun epoch_second () = Instant.now().getEpochSecond()
