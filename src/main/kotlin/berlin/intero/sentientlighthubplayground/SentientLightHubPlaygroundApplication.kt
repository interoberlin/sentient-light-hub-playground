package berlin.intero.sentientlighthubplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import tinyb.BluetoothDevice

@SpringBootApplication
class SentientLightHubPlaygroundApplication

fun main(args: Array<String>) {
    runApplication<SentientLightHubPlaygroundApplication>(*args)

    println("Sentient Light Hub");

    val tinybController = TinybController.getInstance()

    val devices: List<BluetoothDevice>
    val device: BluetoothDevice

    devices = tinybController.scanDevices()
}
