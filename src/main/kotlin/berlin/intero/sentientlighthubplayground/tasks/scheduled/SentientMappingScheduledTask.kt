package berlin.intero.sentientlighthubplayground.tasks.scheduled

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.ConfigurationController
import berlin.intero.sentientlighthubplayground.tasks.async.MQTTSubscribeAsyncTask
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class SentientMappingScheduledTask {
    val recentValues: MutableMap<String, String> = HashMap()

    companion object {
        private val log: Logger = Logger.getLogger(SentientMappingScheduledTask::class.simpleName)
    }

    init {
        val m = ConfigurationController.mappingConfig
        log.info("m $m")

        if (m != null) {
            val mqttSubscribeAsyncTask = MQTTSubscribeAsyncTask()

            mqttSubscribeAsyncTask.topic = "${SentientProperties.TOPIC_SENSOR}/${m.condition.checkerboardID}"
            mqttSubscribeAsyncTask.callback = object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    if (topic != null && message != null)
                        recentValues.put(topic, String(message.payload))
                }

                override fun connectionLost(cause: Throwable?) {
                    log.info("Connection lost")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    log.info("Delivery complete")
                }
            }

            // Run subscription task once
            SimpleAsyncTaskExecutor().execute(mqttSubscribeAsyncTask)
        }
    }

    @Scheduled(fixedDelay = SentientProperties.SENTIENT_MAPPING_DELAY)
    fun map() {
        log.info("-- SENTIENT MAPPING TASK")

        recentValues.forEach { topic, value ->
            log.info("Recent value $topic > $value")
            log.info("Condition fulfilled ${ConfigurationController.mappingConfig?.condition?.isFulfilled(topic, value.toIntOrNull())}")

            val checkerboardID = topic.split('/').last()

            if (ConfigurationController.mappingConfig?.condition?.isFulfilled(checkerboardID, value.toIntOrNull())
                            ?: false) {
                val action = ConfigurationController.mappingConfig?.action

                action?.value = value
                action?.apply()
            }
        }
    }
}

