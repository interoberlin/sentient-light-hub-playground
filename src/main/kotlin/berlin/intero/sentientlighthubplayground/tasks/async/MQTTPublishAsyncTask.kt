package berlin.intero.sentientlighthubplayground.tasks.async

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.MqttController
import berlin.intero.sentientlighthubplayground.model.SensorEvent
import java.util.*
import java.util.logging.Logger

/**
 * This async task publishes a given value to a MQTT topic
 *
 * @param topic topic to be published to
 * @param value value to publish
 */
class MQTTPublishAsyncTask(
        val topic: String,
        val value: String
) : Runnable {

    companion object {
        private val log: Logger = Logger.getLogger(MQTTPublishAsyncTask::class.simpleName)
    }

    override fun run() {
        log.info("${SentientProperties.ANSI_GREEN}-- MQTT PUBLISH TASK${SentientProperties.ANSI_RESET}")
        log.fine("topic/value $topic / $value")

        val event = SensorEvent(topic, value, Date())

        // Publish value
        MqttController.publish(SentientProperties.MQTT_SERVER_URI, event.topic, event.value)
    }
}
