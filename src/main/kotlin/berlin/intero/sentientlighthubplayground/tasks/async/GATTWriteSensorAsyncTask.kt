package berlin.intero.sentientlighthubplayground.tasks.async

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.TinybController
import berlin.intero.sentientlighthubplayground.exceptions.BluetoothConnectionException
import tinyb.BluetoothException
import java.util.logging.Logger

/**
 * This async task writes a value to a device's characteristic
 *
 * @param address MAC address of the device
 * @param characteristicID ID of the characteristic that the value should be written to
 * @param value value to write
 */
class GATTWriteSensorAsyncTask(
        val address: String,
        val characteristicID: String,
        val value: ByteArray
) : Runnable {

    companion object {
        private val log: Logger = Logger.getLogger(GATTWriteSensorAsyncTask::class.simpleName)
    }

    override fun run() {
        log.info("${SentientProperties.ANSI_GREEN}-- GATT WRITE SENSOR TASK${SentientProperties.ANSI_RESET}")

        val scannedDevices = TinybController.scannedDevices

        try {
            val device = scannedDevices.first { d -> d.address == this.address }

            // Ensure connection
            TinybController.ensureConnection(device)

            // Write raw value
            TinybController.writeCharacteristic(device, characteristicID, value)
        } catch (ex: Exception) {
            when (ex) {
                is BluetoothException -> {
                    log.severe("Generic bluetooth exception")
                }
                is BluetoothConnectionException -> {
                    log.severe("Cannot connect to device ${this.address}")
                }
                is NoSuchElementException -> {
                    log.severe("Cannot find device ${this.address}")
                }
                else -> throw ex
            }
        }
    }
}
