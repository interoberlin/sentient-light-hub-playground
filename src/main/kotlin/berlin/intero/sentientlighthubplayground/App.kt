package berlin.intero.sentientlighthubplayground

import berlin.intero.sentientlighthubplayground.tasks.async.SentientPropertiesAsyncTask
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.logging.Logger


@SpringBootApplication
@EnableScheduling
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
    val log = Logger.getLogger(App::class.simpleName)

    log.info("Sentient Light Hub Playground")

    // Call SentientPropertiesAsyncTask
    SimpleAsyncTaskExecutor().execute(SentientPropertiesAsyncTask())
}
