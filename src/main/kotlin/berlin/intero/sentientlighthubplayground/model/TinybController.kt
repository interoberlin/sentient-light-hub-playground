package berlin.intero.sentientlighthubplayground.model

import tinyb.*
import java.util.logging.Logger

class TinybController
private constructor() : BluetoothNotification<ByteArray> {
    companion object {
        val log = Logger.getLogger(TinybController::class.simpleName)
        private const val SCAN_DURATION = 2

        private var inst: TinybController? = null

        fun getInstance(): TinybController {
            if (inst == null) {
                inst = TinybController()
            }

            return inst as TinybController
        }
    }

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

        for (i in 0 until SCAN_DURATION) {
            log.fine(".")
            Thread.sleep(1000)
        }

        try {
            manager.stopDiscovery()
        } catch (e: BluetoothException) {
            log.warning("Discovery could not be stopped.")
        }

        showDevices(manager.devices)
        return manager.devices
    }

    private fun showDevices(devices: List<BluetoothDevice>) {
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
            log.info("$e")
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

    private fun ensureConnection(device: BluetoothDevice) {
        try {
            while (!device.connected) {
                device.connect()
                if (!device.connected) {
                    log.finer(".")
                    Thread.sleep(1000)
                } else {
                    log.info("Connected")
                    break
                }
            }
        } catch (e: BluetoothException) {
            log.severe("$e")
        }
    }

    fun readCharacteristic(device: BluetoothDevice, characteristicID: String): ByteArray {
        log.info("Read characteristic $characteristicID")
        for (service in device.services) {
            for (characteristic in service.characteristics) {
                if (characteristicID == characteristic.uuid) {
                    ensureConnection(device)
                    val value = characteristic.readValue()
                    log.info("Value ${String(value)}")
                    return value
                }
            }
        }

        return ByteArray(0)
    }

    override fun run(value: ByteArray?) {
        log.fine("PING")
    }
}