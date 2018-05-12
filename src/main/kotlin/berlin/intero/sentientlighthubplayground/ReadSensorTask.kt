package berlin.intero.sentientlighthubplayground

import berlin.intero.sentientlighthubplayground.controller.ConfigController
import berlin.intero.sentientlighthubplayground.controller.MqttController
import berlin.intero.sentientlighthubplayground.controller.SentientController
import berlin.intero.sentientlighthubplayground.controller.TinybController
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tinyb.BluetoothException
import java.util.logging.Logger

@Component
class ReadSensorTask {
    companion object {
        val log = Logger.getLogger(TinybController::class.simpleName)
        val configController = ConfigController.getInstance()
        val sentientController = SentientController.getInstance()
        val tinybController = TinybController.getInstance()
        val mqttController = MqttController.getInstance()
    }

    init {
        configController.loadConfig()
    }

    @Scheduled(fixedRate = SentientProperties.SENSOR_READ_RATE)
    fun readSensor() {
        log.info("-- READ SENSOR TASK")

        val scannedDevices = tinybController.scanDevices()
        val intendedDevices = configController.config?.devices

        // Iterate over intended devices
        intendedDevices?.forEach { intendedDevice ->
            try {
                val device = scannedDevices.filter { d -> d.address == intendedDevice.address }.first()

                // Ensure connection
                tinybController.ensureConnection(device)

                // Read value
                val value = tinybController.readCharacteristic(device, SentientProperties.CHARACTERISTIC_SENSOR)

                // Parse value
                val parsedValue = sentientController.parse(value)

                // Publish values
                intendedDevice.sensors.forEach { s ->
                    mqttController.publish(SentientProperties.MQTT_SERVER_URI,
                            "${SentientProperties.TOPIC_BASE}/${s.topic}",
                            "${System.currentTimeMillis()} $parsedValue")
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

