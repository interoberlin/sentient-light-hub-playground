package berlin.intero.sentientlighthubplayground.tasks

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.SentientController
import berlin.intero.sentientlighthubplayground.controller.TinybController
import berlin.intero.sentientlighthubplayground.exceptions.BluetoothConnectionException
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tinyb.BluetoothException
import java.util.logging.Logger

@Component
@SuppressWarnings("unused")
class GATTReadSensorScheduledTask {
    companion object {
        val log: Logger = Logger.getLogger(GATTReadSensorScheduledTask::class.simpleName)
        val sentientController = SentientController.getInstance()
        val tinybController = TinybController.getInstance()
    }

    init {
        sentientController.loadConfig()
    }

    @Scheduled(fixedRate = SentientProperties.SENSOR_READ_RATE)
    fun readSensor() {
        log.info("-- GATT READ SENSOR TASK")

        val scannedDevices = tinybController.scanDevices()
        val intendedDevices = sentientController.config?.devices

        // Iterate over intended devices
        intendedDevices?.forEach { intendedDevice ->
            try {
                val device = scannedDevices.first { d -> d.address == intendedDevice.address }

                // Ensure connection
                tinybController.ensureConnection(device)

                // Read raw value
                val rawValue = tinybController.readCharacteristic(device, SentientProperties.CHARACTERISTIC_SENSOR)

                // Parse values
                val parsedValues = sentientController.parse(rawValue)

                // Publish values
                intendedDevice.sensors.forEach { s ->
                    log.info("topic $s.topic")
                    log.info("index $s.index")
                    log.info("parsedValue ${parsedValues[s.index]}")

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