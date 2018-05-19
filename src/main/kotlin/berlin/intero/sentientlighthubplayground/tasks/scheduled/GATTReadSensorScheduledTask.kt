package berlin.intero.sentientlighthubplayground.tasks.scheduled

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.ConfigurationController
import berlin.intero.sentientlighthubplayground.controller.SentientController
import berlin.intero.sentientlighthubplayground.controller.TinybController
import berlin.intero.sentientlighthubplayground.exceptions.BluetoothConnectionException
import berlin.intero.sentientlighthubplayground.tasks.async.MQTTPublishAsyncTask
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tinyb.BluetoothException
import java.util.logging.Logger

@Component
class GATTReadSensorScheduledTask {

    companion object {
        private val log: Logger = Logger.getLogger(GATTReadSensorScheduledTask::class.simpleName)
    }

    @Scheduled(fixedDelay = SentientProperties.SENSOR_READ_DELAY)
    fun readSensor() {
        log.info("-- GATT READ SENSOR TASK")

        val scannedDevices = TinybController.scannedDevices
        val intendedDevices = ConfigurationController.sensorConfig?.devices

        // Iterate over intended devices
        intendedDevices?.forEach { intendedDevice ->
            try {
                val device = scannedDevices.first { d -> d.address == intendedDevice.address }

                // Ensure connection
                TinybController.ensureConnection(device)

                // Read raw value
                val rawValue = TinybController.readCharacteristic(device, SentientProperties.CHARACTERISTIC_SENSOR)

                // Parse values
                val parsedValues = SentientController.parse(rawValue)

                // Publish values
                intendedDevice.sensors.forEach { s ->
                    // Call MQTTPublishAsyncTask
                    val mqttPublishAsyncTask = MQTTPublishAsyncTask()
                    mqttPublishAsyncTask.topic = "${SentientProperties.TOPIC_SENSOR}/${s.checkerboardID}"
                    mqttPublishAsyncTask.value = parsedValues.getOrElse(s.index) { -1 }.toString()
                    SimpleAsyncTaskExecutor().execute(mqttPublishAsyncTask)
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