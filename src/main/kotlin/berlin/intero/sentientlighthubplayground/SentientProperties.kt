package berlin.intero.sentientlighthubplayground

import org.springframework.context.annotation.Configuration

@Configuration
class SentientProperties {
    companion object {
        var CHARACTERISTIC_SENSOR = "00002014-0000-1000-8000-00805f9b34fb"
        var CHARACTERISTIC_LED = "00004001-0000-1000-8000-00805f9b34fb"

        var TOPIC_BASE = "/sentientlight"
        var TOPIC_SENSOR = "$TOPIC_BASE/floorsensor"
        var TOPIC_LED = "$TOPIC_BASE/led"
        var TOPIC_CONFIGURATION = "$TOPIC_BASE/config"

        var GATT_CONNECTION_RETRY = 10
        var GATT_CONNECTION_IDLE = 5000L
        var GATT_SCAN_RETRY = 2
        var GATT_SCAN_DURATION = 1000L

        var MQTT_SERVER_HOST = "localhost"
        var MQTT_SERVER_PORT = "8883"
        //  var MQTT_SERVER_HOST = "broker.hivemq.com"
        //  var MQTT_SERVER_PORT = "1883"
        var MQTT_SERVER_URI = "tcp://${MQTT_SERVER_HOST}:${MQTT_SERVER_PORT}"

        const val SENSORS_SCAN_RATE = 300_000L
        const val SENSOR_READ_RATE = 100L
        const val SENSOR_READ_DELAY = 1_000L
        // const val SENSOR_READ_DELAY = 10_000L
        const val SENTIENT_MAPPING_RATE = 100L
        const val SENTIENT_MAPPING_DELAY = 1_000L
        // const val SENTIENT_MAPPING_DELAY = 10_000L

        var VALUE_HISTORY = 50

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

        const val ANSI_BLACK_BACKGROUND = "\u001B[40m"
        const val ANSI_RED_BACKGROUND = "\u001B[41m"
        const val ANSI_GREEN_BACKGROUND = "\u001B[42m"
        const val ANSI_YELLOW_BACKGROUND = "\u001B[43m"
        const val ANSI_BLUE_BACKGROUND = "\u001B[44m"
        const val ANSI_PURPLE_BACKGROUND = "\u001B[45m"
        const val ANSI_CYAN_BACKGROUND = "\u001B[46m"
        const val ANSI_WHITE_BACKGROUND = "\u001B[47m"
    }
}