package berlin.intero.sentientlighthubplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.logging.Logger


@SpringBootApplication
@EnableScheduling
class SentientLightHubPlaygroundApplication

fun main(args: Array<String>) {
    runApplication<SentientLightHubPlaygroundApplication>(*args)
    val log = Logger.getLogger(SentientLightHubPlaygroundApplication::class.simpleName)

    log.info("Sentient Light Hub Playground")
}

data class Foo(val x: String, val y: String)
