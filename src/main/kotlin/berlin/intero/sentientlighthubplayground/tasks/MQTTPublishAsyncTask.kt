package berlin.intero.sentientlighthubplayground.tasks

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.MqttController
import berlin.intero.sentientlighthubplayground.controller.SSEController
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
        val log: Logger = Logger.getLogger(MQTTPublishAsyncTask::class.simpleName)
        val mqttController = MqttController.getInstance()
        val sseController = SSEController.getInstance()
    }

    override fun run() {
        log.info("-- MQTT PUBLISH TASK")
        log.info("topic/value $topic / $value")

        val event = SensorEvent(topic, value, Date())

        // Publish values
        mqttController.publish(SentientProperties.MQTT_SERVER_URI, event.topic, event.value)
        sseController.sendJSON(Gson().toJson(event))
    }
}
