package berlin.intero.sentientlighthubplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SentientLightHubPlaygroundApplication

fun main(args: Array<String>) {
    runApplication<SentientLightHubPlaygroundApplication>(*args)

    println("Sentient Light Hub")


    val tinybController = TinybController.getInstance()

    tinybController.loadConfig()

    /*
    val devices: List<BluetoothDevice>
    val device: BluetoothDevice

    devices = tinybController.scanDevices()
    device = devices[0]

    tinybController.connectDevice(device)

    while(!tinybController.connectDevice(device)) {
        Thread.sleep(1000)
    }

    tinybController.showServices(device)

    // tinybController.readCharacteristic()
    */
}
