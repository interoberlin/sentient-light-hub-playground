package berlin.intero.sentientlighthubplayground.tasks

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.MqttController
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class MQTTPublishAsyncTask : Runnable {
    var topic = ""
    var value = ""

    companion object {
        val log: Logger = Logger.getLogger(MQTTPublishAsyncTask::class.simpleName)
        val mqttController = MqttController.getInstance()
    }

    override fun run() {
        log.info("-- MQTT PUBLISH TASK")

        // Publish values
        mqttController.publish(SentientProperties.MQTT_SERVER_URI,
                "${SentientProperties.TOPIC_BASE}/$topic", value)
    }
}
