package berlin.intero.sentientlighthubplayground.tasks.async

import berlin.intero.sentientlighthubplayground.controller.SseController
import berlin.intero.sentientlighthubplayground.model.SensorEvent
import com.google.gson.Gson
import java.util.*
import java.util.logging.Logger

/**
 * This async task sends a value via server-side events
 *
 * @param topic topic
 * @param value value
 */
class SSESendAsyncTask(
        val topic: String,
        val value: String
) : Runnable {

    companion object {
        private val log: Logger = Logger.getLogger(SSESendAsyncTask::class.simpleName)
    }

    override fun run() {
        log.info("-- SSE SEND TASK")
        log.fine("topic/value $topic / $value")

        val event = SensorEvent(topic, value, Date())

        // Send value as JSON
        SseController.sendJSON(Gson().toJson(event))
    }
}
