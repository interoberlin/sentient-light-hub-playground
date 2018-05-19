package berlin.intero.sentientlighthubplayground

import berlin.intero.sentientlighthubplayground.model.mapping.Action
import berlin.intero.sentientlighthubplayground.model.mapping.Condition
import berlin.intero.sentientlighthubplayground.model.mapping.Mapping
import com.google.gson.Gson
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

    val action = Action("stripID", "ledID", "value")
    val condition = Condition("checkerboardID", 1)
    val mapping = Mapping(condition, action)
    val mappings = listOf(mapping)

    log.info(Gson().toJson(mappings))
}
