package berlin.intero.sentientlighthubplayground.model

import java.util.*

data class SensorEvent(val topic: String, val value: String, val timestamp: Date)
