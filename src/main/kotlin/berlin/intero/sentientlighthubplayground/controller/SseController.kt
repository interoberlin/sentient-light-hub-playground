package berlin.intero.sentientlighthubplayground.controller

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.util.concurrent.CopyOnWriteArrayList
import javax.servlet.http.HttpServletResponse


@Controller
@CrossOrigin
object SseController {

    private val log = LoggerFactory.getLogger(SseController::class.java)

    var emitters = CopyOnWriteArrayList<SseEmitter>()

    fun send(value: String) {
        this.send(value, MediaType.TEXT_PLAIN)
    }

    fun sendJSON(json: String) {
        this.send(json, MediaType.APPLICATION_STREAM_JSON)
    }

    fun send(value: String, type: MediaType) {
        log.info("Send to ${emitters.size} emitters $value")
        emitters.forEach { emitter ->
            try {
                val builder = SseEmitter.event()
                        .data(value)
                //       .id("1")
                //       .name("eventName")
                //.reconnectTime(10_000L)
                emitter.send(builder)
            } catch (e: IOException) {
                log.info(e.message)
                e.printStackTrace()
            }
        }
    }

    @GetMapping("/sse")
    fun handleRequest(response: HttpServletResponse): SseEmitter {
        response.setHeader("Cache-Control", "no-store")

        val emitter = SseEmitter(java.lang.Long.MAX_VALUE)

        emitter.onCompletion { emitters.remove(emitter) }
        emitter.onTimeout { emitters.remove(emitter) }

        emitters.add(emitter)

        log.info("HandleRequest ${emitters.size}")

        return emitter
    }
}
