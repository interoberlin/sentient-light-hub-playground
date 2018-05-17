package berlin.intero.sentientlighthubplayground.model.sensor

data class Device(val address: String, val description: String, val sensors: List<Sensor>)