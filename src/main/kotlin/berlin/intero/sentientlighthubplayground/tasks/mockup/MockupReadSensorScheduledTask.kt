package berlin.intero.sentientlighthubplayground.tasks.mockup

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.tasks.async.MQTTPublishAsyncTask
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.logging.Logger

/**
 * This scheduled task
 * <li> read GATT characteristics
 * <li> calls {@link MQTTPublishAsyncTask} to publish the characteristics' values to a MQTT broker
 */
@Component
@ConditionalOnProperty(value = "mockup.readsensor.enabled", havingValue = "true", matchIfMissing = false)
class MockupReadSensorScheduledTask {

    companion object {
        private val log: Logger = Logger.getLogger(MockupReadSensorScheduledTask::class.simpleName)
    }

    @Scheduled(fixedDelay = SentientProperties.SENSOR_READ_DELAY)
    @SuppressWarnings("unused")
    fun readSensor() {
        log.info("-- MOCKUP READ SENSOR TASK")

        // Publish values
        val topic = "${SentientProperties.TOPIC_SENSOR}/D1"
        val value = Random().nextInt(999).toString()

        // Call MQTTPublishAsyncTask
        SimpleAsyncTaskExecutor().execute(MQTTPublishAsyncTask(topic, value))
    }
}
