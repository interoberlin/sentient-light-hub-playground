package berlin.intero.sentientlighthubplayground

import org.springframework.context.annotation.Configuration

@Configuration
class SentientProperties {
    companion object {
        const val CHARACTERISTIC_SENSOR = "00002014-0000-1000-8000-00805f9b34fb"
        const val CHARACTERISTIC_LED = "00004001-0000-1000-8000-00805f9b34fb"

        const val TOPIC_BASE = "/sentientlight"
        const val TOPIC_SENSOR = "$TOPIC_BASE/floorsensor"
        const val TOPIC_LED = "$TOPIC_BASE/led"

        const val GATT_CONNECTION_RETRY = 10
        const val GATT_CONNECTION_IDLE = 5000L
        const val GATT_SCAN_RETRY = 2
        const val GATT_SCAN_DURATION = 1000L

        const val MQTT_SERVER_HOST = "localhost"
        const val MQTT_SERVER_PORT = "8883"
        // const val MQTT_SERVER_HOST = "broker.hivemq.com"
        // const val MQTT_SERVER_PORT = "1883"
        const val MQTT_SERVER_URI = "tcp://${MQTT_SERVER_HOST}:${MQTT_SERVER_PORT}"

        const val SENSORS_SCAN_RATE = 300_000L
        const val SENSOR_READ_RATE = 100L
        const val SENSOR_READ_DELAY = 100L
        // const val SENSOR_READ_DELAY = 10_000L
        const val SENTIENT_MAPPING_RATE = 100L
        const val SENTIENT_MAPPING_DELAY = 100L
        // const val SENTIENT_MAPPING_DELAY = 10_000L

        const val VALUE_HISTORY = 50

        // Colors

        const val ANSI_RESET = "\u001B[0m"
        const val ANSI_BLACK = "\u001B[30m"
        const val ANSI_RED = "\u001B[31m"
        const val ANSI_GREEN = "\u001B[32m"
        const val ANSI_YELLOW = "\u001B[33m"
        const val ANSI_BLUE = "\u001B[34m"
        const val ANSI_PURPLE = "\u001B[35m"
        const val ANSI_CYAN = "\u001B[36m"
        const val ANSI_WHITE = "\u001B[37m"

        val ANSI_BLACK_BACKGROUND = "\u001B[40m"
        val ANSI_RED_BACKGROUND = "\u001B[41m"
        val ANSI_GREEN_BACKGROUND = "\u001B[42m"
        val ANSI_YELLOW_BACKGROUND = "\u001B[43m"
        val ANSI_BLUE_BACKGROUND = "\u001B[44m"
        val ANSI_PURPLE_BACKGROUND = "\u001B[45m"
        val ANSI_CYAN_BACKGROUND = "\u001B[46m"
        val ANSI_WHITE_BACKGROUND = "\u001B[47m"
    }
}