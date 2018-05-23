package berlin.intero.sentientlighthubplayground.tasks.scheduled

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.ConfigurationController
import berlin.intero.sentientlighthubplayground.controller.SentientController
import berlin.intero.sentientlighthubplayground.controller.TinybController
import berlin.intero.sentientlighthubplayground.exceptions.BluetoothConnectionException
import berlin.intero.sentientlighthubplayground.tasks.async.MQTTPublishAsyncTask
import com.google.gson.Gson
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tinyb.BluetoothException
import java.util.logging.Logger

/**
 * This scheduled task
 * <li> read GATT characteristics
 * <li> calls {@link MQTTPublishAsyncTask} to publish the characteristics' values to a MQTT broker
 */
@Component
@ConditionalOnProperty(value = "sentient.readsensor.enabled", havingValue = "true", matchIfMissing = false)
class GATTReadSensorScheduledTask {

    companion object {
        private val log: Logger = Logger.getLogger(GATTReadSensorScheduledTask::class.simpleName)
    }

    @Scheduled(fixedDelay = SentientProperties.SENSOR_READ_DELAY)
    @SuppressWarnings("unused")
    fun readSensor() {
        log.info("-- GATT READ SENSOR TASK")

        val scannedDevices = TinybController.scannedDevices
        val intendedDevices = ConfigurationController.sensorConfig?.sensorDevices

        log.info("Show scannedDevices ${Gson().toJson(scannedDevices.map { d -> d.address })}")
        log.info("Show intendedDevices ${Gson().toJson(intendedDevices?.map { d -> d.address })}")

        // Iterate over intended devices
        intendedDevices?.forEach { intendedDevice ->
            try {
                log.info("Intended device ${intendedDevice.address}")

                val device = scannedDevices.first { d -> d.address == intendedDevice.address }

                // Ensure connection
                TinybController.ensureConnection(device)

                // Show services
                // TinybController.showServices(device)

                // Read raw value
                val rawValue = TinybController.readCharacteristic(device, SentientProperties.CHARACTERISTIC_SENSOR)

                // Parse values
                val parsedValues = SentientController.parse(rawValue)

                // parsedValues.forEachIndexed{index, pv ->
                //     log.info("Value $index $pv");
                // }

                // Publish values
                intendedDevice.sensors.forEach { s ->
                    val topic = "${SentientProperties.TOPIC_SENSOR}/${s.checkerboardID}"
                    val value = parsedValues.getOrElse(s.index) { -1 }.toString()

                    // Call MQTTPublishAsyncTask
                    SimpleAsyncTaskExecutor().execute(MQTTPublishAsyncTask(topic, value))
                }
            } catch (ex: Exception) {
                when (ex) {
                    is BluetoothException -> {
                        log.warning("Generic bluetooth exception")
                    }
                    is BluetoothConnectionException -> {
                        log.warning("Cannot connect to device ${intendedDevice.address}")
                    }
                    is NoSuchElementException -> {
                        log.warning("Cannot find device ${intendedDevice.address}")
                    }
                    else -> throw ex
                }
            }
        }
    }
}
