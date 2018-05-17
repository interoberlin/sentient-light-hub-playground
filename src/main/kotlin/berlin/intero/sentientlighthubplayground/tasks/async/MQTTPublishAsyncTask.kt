package berlin.intero.sentientlighthubplayground.tasks.async

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.MqttController
import berlin.intero.sentientlighthubplayground.controller.SseController
import berlin.intero.sentientlighthubplayground.model.SensorEvent
import com.google.gson.Gson
import org.springframework.stereotype.Component
import java.util.*
import java.util.logging.Logger

@Component
class MQTTPublishAsyncTask : Runnable {

    var topic = ""
    var value = ""

    companion object {
        private val log: Logger = Logger.getLogger(MQTTPublishAsyncTask::class.simpleName)
    }

    override fun run() {
        log.info("-- MQTT PUBLISH TASK")
        log.info("topic/value $topic / $value")

        val event = SensorEvent(topic, value, Date())

        // Publish values
        MqttController.publish(SentientProperties.MQTT_SERVER_URI, event.topic, event.value)
        SseController.sendJSON(Gson().toJson(event))
    }
}
