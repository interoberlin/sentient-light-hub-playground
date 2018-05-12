package berlin.intero.sentientlighthubplayground.controller

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
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

    /**
     * Publishes a given message to a topic
     *
     * @param mqttServerURI MQTT server to connect to
     * @param topic MQTT topic to publish to
     * @param messageString message to be sent
    */
    fun publish(mqttServerURI: String, topic: String, messageString: String) {
        log.fine("MQTT publish")

        // Connect to MQTT broker
        val client = MqttClient(mqttServerURI, MqttClient.generateClientId())
        client.connect()

        // Build message
        val message = MqttMessage(messageString.toByteArray())

        // Publish mesage
        log.info("MQTT publish ${topic} : ${messageString}")
        client.publish(topic, message)

        // Disconnect from MQTT broker
        log.info("MQTT client disconnect")
        client.disconnect()
    }

    /**
     * Subscribes a given topic from MQTT server
     *
     * @param mqttServerURI MQTT server to connect to
     * @param topic MQTT topic to subscribe
     */
    fun subscribe(mqttServerURI: String, topic: String) {
        log.fine("MQTT subscribe")

        val client = MqttClient(mqttServerURI, MqttClient.generateClientId())
        client.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                log.info("MQTT message arrived $topic ${String(message?.getPayload()!!)}")
            }

            override fun connectionLost(cause: Throwable?) {
                log.info("MQTT connection lost")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                log.info("MQTT delivery complete")

            }
        })

        // Connect to MQTT broker and subscribe topic
        client.connect()
        client.subscribe(topic)
    }
}
