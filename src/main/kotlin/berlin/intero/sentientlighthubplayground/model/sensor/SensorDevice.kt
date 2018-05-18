package berlin.intero.sentientlighthubplayground.model.sensor

data class SensorDevice(
        val address: String,
        val description: String,
        val sensors: List<Sensor>)