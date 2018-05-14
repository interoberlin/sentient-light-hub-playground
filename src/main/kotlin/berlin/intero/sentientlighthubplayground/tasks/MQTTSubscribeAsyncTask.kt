package berlin.intero.sentientlighthubplayground.tasks

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.MqttController
import berlin.intero.sentientlighthubplayground.controller.SentientController
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class MQTTSubscribeAsyncTask : Runnable {
    var callback: MqttCallback? = null

    companion object {
        val log: Logger = Logger.getLogger(MQTTSubscribeAsyncTask::class.simpleName)
        val sentientController = SentientController.getInstance()
        val mqttController = MqttController.getInstance()
    }

    init {
        sentientController.loadConfig()
    }

    override fun run() {
        log.info("-- MQTT SUBSCRIBE TASK")

        val intendedDevices = sentientController.config?.devices

        // Iterate over intended devices
        intendedDevices?.forEach { intendedDevice ->

            // Subscribe values
            intendedDevice.sensors.forEach { s ->
                if (callback != null) {
                    mqttController.subscribe(SentientProperties.MQTT_SERVER_URI,
                            "${SentientProperties.TOPIC_SENSOR}/${s.checkerboardID}", callback)
                } else {
                    mqttController.subscribe(SentientProperties.MQTT_SERVER_URI,
                            "${SentientProperties.TOPIC_SENSOR}/${s.checkerboardID}")
                }
            }
        }
    }
}
