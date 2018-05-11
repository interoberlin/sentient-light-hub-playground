package berlin.intero.sentientlighthubplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.logging.Logger

@SpringBootApplication
class SentientLightHubPlaygroundApplication

fun main(args: Array<String>) {
    runApplication<SentientLightHubPlaygroundApplication>(*args)
    val log = Logger.getLogger(SentientLightHubPlaygroundApplication::class.simpleName)

    log.info("Sentient Light Hub")

    val tinybController = TinybController.getInstance()

    tinybController.loadConfig()

    val scannedDevices = tinybController.scanDevices()
    val intendedDevices = tinybController.config?.devices

    intendedDevices?.forEach { intendedDevice ->
        val devices = scannedDevices.filter { d -> d.address == intendedDevice.address }

        if (!devices.isEmpty()) {
            val device = devices.first()
            while (!tinybController.connectDevice(device)) {
                Thread.sleep(1000)
            }
            tinybController.showServices(device)
        } else {
            log.warning("Device ${intendedDevice.address} not found")
        }
        // tinybController.readCharacteristic()
    }
}
