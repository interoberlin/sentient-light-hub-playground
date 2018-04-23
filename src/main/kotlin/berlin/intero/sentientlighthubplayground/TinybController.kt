package berlin.intero.sentientlighthubplayground

import tinyb.*

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

        println("Start scan")

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
        println("Show devices")
        var i = 0
        for (device in devices) {
            println("# ${++i} ${device.address} ${device.name} ${device.connected}")
        }
    }

    fun connectDevice(device: BluetoothDevice) {
        println("Connect device ${device.address} ${device.name}")
        try {
            if (device.connect()) {
                println("Paired : ${device.paired}")
                println("Trusted: ${device.trusted}")
            } else {
                println("Could not connect device")
                System.exit(-1)
            }
        } catch (e: BluetoothException) {
            println("$e")
        }
    }

    fun showServices(device: BluetoothDevice) {
        println("Show service")

        var bluetoothServices: List<BluetoothGattService>

        for (i in 1..10) {
            ensureConnection(device)

            bluetoothServices = device.services

            for (service in bluetoothServices) {
                println("     Service " + service.uuid)
                for (characteristic in service.characteristics) {
                    println("      Characteristic ${characteristic.uuid}")
                }
            }
        }
    }

    fun ensureConnection(device: BluetoothDevice) {
        try {
            while (!device.connected) {
                device.connect()
                if (!device.connected) {
                    print(".")
                    Thread.sleep(1000)
                } else {
                    println("Connected")
                    break
                }
            }
        } catch (e: BluetoothException) {
            println("$e")
        }
    }

    override fun run(value: ByteArray?) {
        println("PING")
    }
}