package berlin.intero.sentientlighthubplayground.tasks

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.MqttController
import berlin.intero.sentientlighthubplayground.controller.SentientController
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
        val log: Logger = Logger.getLogger(SentientMappingScheduledTask::class.simpleName)
        val sentientController = SentientController.getInstance()
    }

    init {
        sentientController.loadSensorsConfig()
        sentientController.loadMappingConfig()

        val m = sentientController.mappingConfig
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
                    MqttController.log.info("Connection lost")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    MqttController.log.info("Delivery complete")

                }
            }

            // Run subscription task once
            SimpleAsyncTaskExecutor().execute(mqttSubscribeAsyncTask)
        }
    }

    @Scheduled(fixedRate = SentientProperties.SENTIENT_MAPPING_RATE)
    fun map() {
        log.info("-- SENTIENT MAPPING TASK")

        recentValues.forEach { k, v ->
            log.info("Recent value $k > $v")
        }
    }
}

