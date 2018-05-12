package berlin.intero.sentientlighthubplayground

import berlin.intero.sentientlighthubplayground.tasks.SubscribeValueTask
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.logging.Logger



@SpringBootApplication
@EnableScheduling
class SentientLightHubPlaygroundApplication

fun main(args: Array<String>) {
    runApplication<SentientLightHubPlaygroundApplication>(*args)
    val log = Logger.getLogger(SentientLightHubPlaygroundApplication::class.simpleName)

    log.info("Sentient Light Hub Playground")

    // Run subscription task once
    SimpleAsyncTaskExecutor().execute(SubscribeValueTask())
}
