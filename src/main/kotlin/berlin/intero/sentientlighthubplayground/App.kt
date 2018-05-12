package berlin.intero.sentientlighthubplayground

import berlin.intero.sentientlighthubplayground.model.ConfigController
import berlin.intero.sentientlighthubplayground.model.MqttController
import berlin.intero.sentientlighthubplayground.model.TinybController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.logging.Logger

@SpringBootApplication
class SentientLightHubPlaygroundApplication

fun main(args: Array<String>) {
    runApplication<SentientLightHubPlaygroundApplication>(*args)
    val log = Logger.getLogger(SentientLightHubPlaygroundApplication::class.simpleName)

    log.info("Sentient Light Hub Playground")

    val configController = ConfigController.getInstance()
    val tinybController = TinybController.getInstance()
    val mqttController = MqttController.getInstance()

    configController.loadConfig()

    val scannedDevices = tinybController.scanDevices()
    val intendedDevices = configController.config?.devices

    intendedDevices?.forEach { intendedDevice ->
        val devices = scannedDevices.filter { d -> d.address == intendedDevice.address }

        if (!devices.isEmpty()) {
            val device = devices.first()
            while (!tinybController.connectDevice(device)) {
                Thread.sleep(1000)
            }
            // tinybController.showServices(device)

            val value = tinybController.readCharacteristic(device, SentientProperties.characteristic_sensor)

            // TODO parse value here

            intendedDevice.sensors.forEach { s ->
                log.info("Subscribe")
                mqttController.subscribe("${SentientProperties.topic_base}/${s.topic}")

                log.info("Publish ${s.id}")
                mqttController.publish("${SentientProperties.topic_base}/${s.topic}", "${System.currentTimeMillis()} $value")
            }

            while (true) {
                Thread.sleep(5000)
                log.info(".")
            }

            log.info("END")
        } else {
            log.warning("Device ${intendedDevice.address} not found")
        }
    }
}
