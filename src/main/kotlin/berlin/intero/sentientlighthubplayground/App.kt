package berlin.intero.sentientlighthubplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.logging.Logger


@SpringBootApplication
// @EnableScheduling
class SentientLightHubPlaygroundApplication

fun main(args: Array<String>) {
    runApplication<SentientLightHubPlaygroundApplication>(*args)
    val log = Logger.getLogger(SentientLightHubPlaygroundApplication::class.simpleName)

    log.info("Sentient Light Hub Playground")
}
