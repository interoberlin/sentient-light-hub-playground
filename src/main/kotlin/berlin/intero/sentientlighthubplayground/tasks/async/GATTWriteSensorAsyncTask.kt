package berlin.intero.sentientlighthubplayground.tasks.async

import berlin.intero.sentientlighthubplayground.controller.TinybController
import berlin.intero.sentientlighthubplayground.exceptions.BluetoothConnectionException
import org.springframework.stereotype.Component
import tinyb.BluetoothException
import java.util.logging.Logger

@Component
class GATTWriteSensorAsyncTask : Runnable {
    var address = ""
    var characteristicID = ""
    var value = ByteArray(1)

    companion object {
        val log: Logger = Logger.getLogger(GATTWriteSensorAsyncTask::class.simpleName)
        val tinybController = TinybController.getInstance()
    }

    override fun run() {
        log.info("-- GATT WRITE SENSOR TASK")

        val scannedDevices = tinybController.scannedDevices

        try {
            val device = scannedDevices.first { d -> d.address == this.address }

            // Ensure connection
            tinybController.ensureConnection(device)

            // Write raw value
            tinybController.writeCharacteristic(device, characteristicID, value)
        } catch (ex: Exception) {
            when (ex) {
                is BluetoothException -> {
                    log.warning("Generic bluetooth exception")
                }
                is BluetoothConnectionException -> {
                    log.warning("Cannot connect to device ${this.address}")
                }
                is NoSuchElementException -> {
                    log.warning("Cannot find device ${this.address}")
                }
                else -> throw ex
            }
        }
    }
}
