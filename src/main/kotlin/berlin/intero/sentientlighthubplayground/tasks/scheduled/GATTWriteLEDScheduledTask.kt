package berlin.intero.sentientlighthubplayground.tasks.scheduled

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.ConfigurationController
import berlin.intero.sentientlighthubplayground.tasks.async.GATTWriteSensorAsyncTask
import berlin.intero.sentientlighthubplayground.tasks.async.MQTTSubscribeAsyncTask
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class GATTWriteLEDScheduledTask {
    val recentValues: MutableMap<String, String> = HashMap()

    companion object {
        private val log: Logger = Logger.getLogger(GATTWriteLEDScheduledTask::class.simpleName)
    }

    init {
        val m = ConfigurationController.mappingConfig
        log.info("m $m")

        if (m != null) {
            val mqttSubscribeAsyncTask = MQTTSubscribeAsyncTask()

            mqttSubscribeAsyncTask.topic = "${SentientProperties.TOPIC_LED}/#"
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

            val stripID = topic.split('/')[1]
            val ledID = topic.split('/')[2]

            val actor = ConfigurationController.getActor(stripID.toIntOrNull(), ledID.toIntOrNull())

            if (actor != null) {
                // Call GATTWriteSensorAsyncTask
                val gattWriteSensorAsyncTask = GATTWriteSensorAsyncTask()
                gattWriteSensorAsyncTask.address = actor.address
                gattWriteSensorAsyncTask.characteristicID = SentientProperties.CHARACTERISTIC_LED

                when (value) {
                    "0" -> gattWriteSensorAsyncTask.value = byteArrayOf(0x00)
                    "1" -> gattWriteSensorAsyncTask.value = byteArrayOf(0x01)
                }

                SimpleAsyncTaskExecutor().execute(gattWriteSensorAsyncTask)
            }
        }
    }
}

