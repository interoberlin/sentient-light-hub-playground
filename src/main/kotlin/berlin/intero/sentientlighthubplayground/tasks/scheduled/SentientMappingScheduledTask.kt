package berlin.intero.sentientlighthubplayground.tasks.scheduled

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.ConfigurationController
import berlin.intero.sentientlighthubplayground.tasks.async.MQTTSubscribeAsyncTask
import berlin.intero.sentientlighthubplayground.tasks.async.SentientMappingEvaluationAsyncTask
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
 * <li> calls {@link MQTTSubscribeAsyncTask} to subscribe sensor values from MQTT broker
 * <li> calls {@link SentientMappingEvaluationAsyncTask} for each mapping from configuration
 */
@Component
@ConditionalOnProperty(value = "sentient.mapping.enabled", havingValue = "true", matchIfMissing = false)
class SentientMappingScheduledTask {

    val values: MutableMap<String, String> = HashMap()

    companion object {
        private val log: Logger = Logger.getLogger(SentientMappingScheduledTask::class.simpleName)
    }

    init {
        val topic = "${SentientProperties.TOPIC_SENSOR}/#"
        val callback = object : MqttCallback {
            override fun messageArrived(topic: String, message: MqttMessage) {
                val checkerboardID = topic.split('/').last()
                val value = String(message.payload)

                values[checkerboardID] = value
            }

            override fun connectionLost(cause: Throwable?) {
                log.info("Connection lost")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                log.info("Delivery complete")
            }
        }

        // Call MQTTSubscribeAsyncTask
        val mqttSubscribeAsyncTask = MQTTSubscribeAsyncTask(topic, callback)
        SimpleAsyncTaskExecutor().execute(mqttSubscribeAsyncTask)
    }

    @Scheduled(fixedDelay = SentientProperties.SENTIENT_MAPPING_DELAY)
    @SuppressWarnings("unused")
    fun map() {
        log.info("-- SENTIENT MAPPING TASK")

        ConfigurationController.mappingConfig?.mappings?.forEach { mapping ->

            // Call SentientMappingEvaluationAsyncTask
            SimpleAsyncTaskExecutor().execute(SentientMappingEvaluationAsyncTask(mapping, values))
        }
    }
}
