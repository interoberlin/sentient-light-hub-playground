package berlin.intero.sentientlighthubplayground

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("sentient")
class SentientProperties {
    companion object {
        const val CHARACTERISTIC_SENSOR = "00002014-0000-1000-8000-00805f9b34fb"
        const val TOPIC_BASE = "/sentientlight"

        const val GATT_CONNECTION_RETRY = 10
        const val GATT_CONNECTION_IDLE = 5000L
        const val GATT_SCAN_RETRY = 2
        const val GATT_SCAN_DURATION = 1000L

        const val MQTT_SERVER_HOST = "localhost"
        const val MQTT_SERVER_PORT = "8883"
        const val MQTT_SERVER_URI = "tcp://${MQTT_SERVER_HOST}:${MQTT_SERVER_PORT}"

        const val SENSOR_READ_RATE = 5000L
        const val SENTIENT_MAPPING_RATE = 5000L
    }
}