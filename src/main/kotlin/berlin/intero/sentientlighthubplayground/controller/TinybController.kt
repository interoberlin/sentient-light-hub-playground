package berlin.intero.sentientlighthubplayground.controller

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.exceptions.BluetoothConnectionException
import org.springframework.stereotype.Controller
import tinyb.*
import java.util.logging.Logger

@Controller
object TinybController: BluetoothNotification<ByteArray> {

    private val log: Logger = Logger.getLogger(TinybController::class.simpleName)

    var scannedDevices: MutableList<BluetoothDevice> = ArrayList()

    @Throws(InterruptedException::class)
    fun scanDevices(): List<BluetoothDevice> {
        log.fine("Start scan")

        val manager = BluetoothManager.getBluetoothManager()
        try {
            manager.startDiscovery()
        } catch (e: BluetoothException) {
            log.severe("$e")
            return emptyList()
        }

        for (i in 0 until SentientProperties.GATT_SCAN_RETRY) {
            log.fine(".")
            Thread.sleep(SentientProperties.GATT_SCAN_DURATION)
        }

        try {
            manager.stopDiscovery()
        } catch (e: BluetoothException) {
            log.warning("Discovery could not be stopped.")
        }

        scannedDevices = manager.devices
        return manager.devices
    }

    fun showDevices(devices: List<BluetoothDevice>) {
        log.fine("Show devices")

        for ((i, device) in devices.withIndex()) {
            log.info("# ${i + 1} ${device.address} ${device.name} ${device.connected}")
        }
    }

    fun connectDevice(device: BluetoothDevice): Boolean {
        log.fine("Connect device ${device.address} ${device.name}")

        try {
            return if (device.connect()) {
                log.info("Paired : ${device.paired}")
                log.info("Trusted: ${device.trusted}")
                true
            } else {
                log.warning("Could not connect device")
                System.exit(-1)
                false
            }
        } catch (e: BluetoothException) {
            log.severe("$e")
        }

        return false
    }

    fun showServices(device: BluetoothDevice) {
        log.fine("Show service")

        var bluetoothServices: List<BluetoothGattService>

        for (i in 1..10) {
            ensureConnection(device)

            bluetoothServices = device.services

            for (service in bluetoothServices) {
                log.info("     Service " + service.uuid)
                for (characteristic in service.characteristics) {
                    log.info("      Characteristic ${characteristic.uuid}")
                }
            }
        }
    }

    @Throws(BluetoothException::class, BluetoothConnectionException::class)
    fun ensureConnection(device: BluetoothDevice) {
        log.info("Ensure connection")

        repeat(SentientProperties.GATT_CONNECTION_RETRY, {
            if (!connectDevice(device)) {
                log.finer(".")
                Thread.sleep(SentientProperties.GATT_CONNECTION_IDLE)
            } else {
                log.info("Connected")
                return
            }
        })

        throw BluetoothConnectionException("Cannot connect to device")
    }

    fun readCharacteristic(device: BluetoothDevice, characteristicID: String): ByteArray {
        log.info("Read characteristicID $characteristicID")
        for (service in device.services) {
            for (characteristic in service.characteristics) {
                if (characteristicID == characteristic.uuid) {
                    ensureConnection(device)
                    val value = characteristic.readValue()
                    return value
                }
            }
        }

        return ByteArray(0)
    }

    fun writeCharacteristic(device: BluetoothDevice, characteristicID: String, bytes: ByteArray) {
        log.info("Write Characteristic" +
                "\ndevice ${device.address}" +
                "\ncharacteristicID ${characteristicID}" +
                "\nbytes $bytes")

        for (service in device.services) {
            for (characteristic in service.characteristics) {
                if (characteristic.uuid == characteristicID || characteristic.uuid.contains(characteristicID)) {
                    writeCharacteristic(device, characteristic, bytes)
                }
            }
        }
    }


    /**
     * Write an array of @param bytes into the specified @param characteristicID of a @param device
     */
    private fun writeCharacteristic(device: BluetoothDevice, characteristic: BluetoothGattCharacteristic, bytes: ByteArray) {
        log.info("Write Characteristic")
        ensureConnection(device)

        try {
            log.info("Write Characteristic (really)")
            characteristic.writeValue(bytes)
        } catch (e: BluetoothException) {
            log.severe("$e")
        }
    }

    override fun run(value: ByteArray?) {
        log.fine("PING")
    }
}