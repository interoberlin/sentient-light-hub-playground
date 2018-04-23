package berlin.intero.sentientlighthubplayground

import tinyb.BluetoothDevice
import tinyb.BluetoothException
import tinyb.BluetoothManager
import tinyb.BluetoothNotification

class TinybController
private constructor() : BluetoothNotification<ByteArray> {
    companion object {
        private val SCAN_DURATION = 2

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
        val manager = BluetoothManager.getBluetoothManager()
        try {
            manager.startDiscovery()
        } catch (e: BluetoothException) {
            println("$e")
            return emptyList()
        }

        println("Starting scan")

        for (i in 0..SCAN_DURATION - 1) {
            print(".")
            Thread.sleep(1000)
        }

        try {
            manager.stopDiscovery()
        } catch (e: BluetoothException) {
            println("Discovery could not be stopped.")
        }

        showDevices(manager.devices)
        return manager.devices
    }

    fun showDevices(devices: List<BluetoothDevice>) {
        println("Showing devices")
        var i = 0
        for (device in devices) {
            println("# ${++i} ${device.address} ${device.name} ${device.connected}")
        }
    }

    override fun run(value: ByteArray?) {
        println("PING")
    }
}