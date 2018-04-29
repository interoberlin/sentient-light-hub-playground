package berlin.intero.sentientlighthubplayground

import berlin.intero.sentientlighthubplayground.model.Config
import com.google.gson.GsonBuilder
import org.apache.commons.io.IOUtils
import tinyb.*
import java.io.IOException

class TinybController
private constructor() : BluetoothNotification<ByteArray> {
    companion object {
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
        val manager = BluetoothManager.getBluetoothManager()
        try {
            manager.startDiscovery()
        } catch (e: BluetoothException) {
            println("$e")
            return emptyList()
        }

        println("Start scan")

        for (i in 0 until SCAN_DURATION) {
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

    fun connectDevice(device: BluetoothDevice): Boolean {
        println("Connect device ${device.address} ${device.name}")
        try {
            return if (device.connect()) {
                println("Paired : ${device.paired}")
                println("Trusted: ${device.trusted}")
                true
            } else {
                println("Could not connect device")
                System.exit(-1)
                false
            }
        } catch (e: BluetoothException) {
            println("$e")
        }

        return false
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

    fun readCharacteristic(device: BluetoothDevice, characteristicID: String): ByteArray {
        for (service in device.services) {
            for (characteristic in service.characteristics) {
                if (characteristicID == characteristic.uuid) {
                    ensureConnection(device)
                    return characteristic.readValue()
                }
            }
        }

        return ByteArray(0)
    }

    fun loadConfig() {
        try {
            val result = IOUtils.toString(javaClass.getClassLoader().getResourceAsStream("config.json"));
            val config = GsonBuilder().create().fromJson(result, Config::class.java) as Config
            println(config)

            // val sensor = Sensor(0, "topic", "checker")
            // val beacon = Beacon("address", mutableListOf(sensor))
            // val config = Config(mutableListOf(beacon))
            // println(Gson().toJson(config))
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    override fun run(value: ByteArray?) {
        println("PING")
    }
}