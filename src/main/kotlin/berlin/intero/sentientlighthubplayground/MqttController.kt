package berlin.intero.sentientlighthubplayground

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.logging.Logger

class MqttController {
    companion object {
        val log = Logger.getLogger(MqttController::class.simpleName)

        private var inst: MqttController? = null

        fun getInstance(): MqttController {
            if (inst == null) {
                inst = MqttController()
            }

            return inst as MqttController
        }
    }

    fun publish(topic: String, messageString: String) {
        val client = MqttClient("tcp://localhost:8883", MqttClient.generateClientId())
        client.connect()
        val message = MqttMessage()
        message.setPayload(messageString.toByteArray())

        log.info("MQTT publish ${topic} : ${messageString}")
        client.publish(topic, message)

        log.info("MQTT client disconnect")
        client.disconnect()
    }
}