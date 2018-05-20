package berlin.intero.sentientlighthubplayground.tasks.async

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.MqttController
import org.eclipse.paho.client.mqttv3.MqttCallback
import java.util.logging.Logger

/**
 * This async task starts subscription of a MQTT topic. Results are passed via a callback.
 *
 * @param topic topic to subscribe
 * @param callback callback for subscription events
 */
class MQTTSubscribeAsyncTask(
        val topic: String,
        val callback: MqttCallback
) : Runnable {

    companion object {
        private val log: Logger = Logger.getLogger(MQTTSubscribeAsyncTask::class.simpleName)
    }

    override fun run() {
        log.info("-- MQTT SUBSCRIBE TASK")
        log.fine("topic $topic")

        MqttController.subscribe(SentientProperties.MQTT_SERVER_URI, topic, callback)
    }
}
