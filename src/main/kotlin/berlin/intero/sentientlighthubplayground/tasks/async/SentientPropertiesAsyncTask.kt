package berlin.intero.sentientlighthubplayground.tasks.async

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.ConfigurationController
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.core.task.SimpleAsyncTaskExecutor
import java.util.logging.Logger

/**
 * This async task subscribes values from the MQTT broker and changes configuration accordingly
 *
 */
class SentientPropertiesAsyncTask() : Runnable {

    companion object {
        private val log: Logger = Logger.getLogger(SentientPropertiesAsyncTask::class.simpleName)
    }

    init {
        val topic = "${SentientProperties.TOPIC_CONFIGURATION}/#"
        val callback = object : MqttCallback {
            override fun messageArrived(topic: String, message: MqttMessage) {
                val propertyName = topic.split('/').last()
                val value = String(message.payload)

                handleValue(propertyName, value)
            }

            override fun connectionLost(cause: Throwable?) {
                log.severe("Connection lost")
                log.info("Restart connection")

                val mqttSubscribeAsyncTask = MQTTSubscribeAsyncTask(topic, this)
                SimpleAsyncTaskExecutor().execute(mqttSubscribeAsyncTask)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                log.severe("Delivery complete")
            }
        }

        // Call MQTTSubscribeAsyncTask
        val mqttSubscribeAsyncTask = MQTTSubscribeAsyncTask(topic, callback)
        SimpleAsyncTaskExecutor().execute(mqttSubscribeAsyncTask)
    }

    fun handleValue(propertyName: String, value: String) {

        when (propertyName) {
            "characteristic_sensor" -> SentientProperties.CHARACTERISTIC_SENSOR = value
            "characteristic_led" -> SentientProperties.CHARACTERISTIC_LED = value
            "topic_base" -> SentientProperties.TOPIC_BASE = value
            "topic_sensor" -> SentientProperties.TOPIC_SENSOR = value
            "topic_led" -> SentientProperties.TOPIC_LED = value
            "topic_configuration" -> SentientProperties.TOPIC_CONFIGURATION = value

            "gatt_connection_retry" -> SentientProperties.GATT_CONNECTION_RETRY = value.toInt()
            "gatt_connection_idle" -> SentientProperties.GATT_CONNECTION_IDLE = value.toLong()
            "gatt_scan_retry" -> SentientProperties.GATT_SCAN_RETRY = value.toInt()
            "gatt_scan_duration" -> SentientProperties.GATT_SCAN_DURATION = value.toLong()

            "mqtt_server_host" -> SentientProperties.MQTT_SERVER_HOST = value
            "mqtt_server_port" -> SentientProperties.MQTT_SERVER_PORT = value

            "value_history" -> SentientProperties.VALUE_HISTORY = value.toInt()

            "sensors_config" -> ConfigurationController.loadSensorsConfig(value)
            "actors_config" -> ConfigurationController.loadActorsConfig(value)
            "mappings_config" -> ConfigurationController.loadMappingsConfig(value)
            else -> {
                log.severe("Unknown property $propertyName")
                return
            }
        }

        log.info("${SentientProperties.ANSI_CYAN}Property change $propertyName : $value ${SentientProperties.ANSI_RESET}")
    }

    override fun run() {
        log.info("${SentientProperties.ANSI_GREEN}-- SENTIENT PROPERTIES TASK${SentientProperties.ANSI_RESET}")
    }
}
