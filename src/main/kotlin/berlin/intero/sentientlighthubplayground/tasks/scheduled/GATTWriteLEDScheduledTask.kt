package berlin.intero.sentientlighthubplayground.tasks.scheduled

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.ConfigurationController
import berlin.intero.sentientlighthubplayground.tasks.async.GATTWriteSensorAsyncTask
import berlin.intero.sentientlighthubplayground.tasks.async.MQTTSubscribeAsyncTask
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.logging.Logger

/**
 * This scheduled task
 * <li> calls {@link MQTTSubscribeAsyncTask} to subscribe mapping values from MQTT broker
 * <li> calls {@link GATTWriteSensorAsyncTask} to write values to a GATT device
 */
@Component
@ConditionalOnProperty(value = "sentient.writeled.enabled", havingValue = "true", matchIfMissing = false)
class GATTWriteLEDScheduledTask {
    val values: MutableMap<String, String> = HashMap()

    companion object {
        private val log: Logger = Logger.getLogger(GATTWriteLEDScheduledTask::class.simpleName)
    }

    init {
        val topic = "${SentientProperties.TOPIC_LED}/#"
        val callback = object : MqttCallback {
            override fun messageArrived(topic: String, message: MqttMessage) {
                values[topic] = String(message.payload)
            }

            override fun connectionLost(cause: Throwable?) {
                log.info("Connection lost")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                log.info("Delivery complete")
            }
        }

        // Call MQTTSubscribeAsyncTask
        SimpleAsyncTaskExecutor().execute(MQTTSubscribeAsyncTask(topic, callback))
    }

    @Scheduled(fixedDelay = SentientProperties.SENTIENT_MAPPING_DELAY)
    fun map() {
        log.info("-- GATT WRITE LED TASK")

        values.forEach { topic, value ->
            log.info("Recent value $topic > $value")

            val stripID = topic.split('/')[1]
            val ledID = topic.split('/')[2]

            val actor = ConfigurationController.getActor(stripID.toIntOrNull(), ledID.toIntOrNull())

            if (actor != null) {
                val address = actor.address
                val characteristicID = SentientProperties.CHARACTERISTIC_LED

                var byteValue = byteArrayOf()

                when (value) {
                    "0" -> byteValue = byteArrayOf(0x00)
                    "1" -> byteValue = byteArrayOf(0x01)
                }

                // Call GATTWriteSensorAsyncTask
                SimpleAsyncTaskExecutor().execute(GATTWriteSensorAsyncTask(address, characteristicID, byteValue))
            }
        }
    }
}
