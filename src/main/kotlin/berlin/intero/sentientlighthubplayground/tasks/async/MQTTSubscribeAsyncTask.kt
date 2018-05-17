package berlin.intero.sentientlighthubplayground.tasks.async

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.MqttController
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class MQTTSubscribeAsyncTask : Runnable {
    var callback: MqttCallback? = null
    var topic: String? = null

    companion object {
        val log: Logger = Logger.getLogger(MQTTSubscribeAsyncTask::class.simpleName)
        val mqttController = MqttController.getInstance()
    }

    override fun run() {
        log.info("-- MQTT SUBSCRIBE TASK")
        log.info("topic $topic")

        if (topic != null) {
            if (callback != null) {
                mqttController.subscribe(SentientProperties.MQTT_SERVER_URI,
                        topic as String, callback)
            } else {
                mqttController.subscribe(SentientProperties.MQTT_SERVER_URI,
                        topic as String)
            }
        }
    }
}

