package berlin.intero.sentientlighthubplayground.controller

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.stereotype.Controller
import java.util.logging.Logger

@Controller
object MqttController {

    private val log = Logger.getLogger(MqttController::class.simpleName)

    /**
     * Publishes a given message to a topic
     *
     * @param mqttServerURI MQTT server to connect to
     * @param topic MQTT topic to publish to
     * @param messageString message to be sent
     */
    fun publish(mqttServerURI: String, topic: String, messageString: String) {
        log.fine("Publish")

        // Connect to MQTT broker
        val client = MqttClient(mqttServerURI, MqttClient.generateClientId())
        client.connect()

        // Build message
        val message = MqttMessage(messageString.toByteArray())

        // Publish message
        log.info("Publish ${topic} : ${messageString}")
        client.publish(topic, message)


        // Disconnect from MQTT broker
        log.info("Client disconnect")
        client.disconnect()
    }

    /**
     * Subscribes a given topic from MQTT server
     *
     * @param mqttServerURI MQTT server to connect to
     * @param topic MQTT topic to subscribe
     */
    fun subscribe(mqttServerURI: String, topic: String) {
        subscribe(mqttServerURI, topic, object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                log.info("Message arrived $topic ${String(message?.getPayload()!!)}")
            }

            override fun connectionLost(cause: Throwable?) {
                log.info("Connection lost")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                log.info("Delivery complete")
            }
        })
    }

    /**
     *
     */
    fun subscribe(mqttServerURI: String, topic: String, callback: MqttCallback?) {
        log.fine("MQTT subscribe")

        // Generate client and set callback
        val client = MqttClient(mqttServerURI, MqttClient.generateClientId())
        client.setCallback(callback)

        // Connect to MQTT broker and subscribe topic
        client.connect()
        client.subscribe(topic)
    }
}
