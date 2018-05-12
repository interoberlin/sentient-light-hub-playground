package berlin.intero.sentientlighthubplayground.tasks

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.ConfigController
import berlin.intero.sentientlighthubplayground.controller.TinybController
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class SubscribeValueTask : Runnable {

    companion object {
        val log = Logger.getLogger(TinybController::class.simpleName)
        val configController = ConfigController.getInstance()
    }

    init {
        configController.loadConfig()
    }

    override fun run() {
        log.info("-- SUBSCRIBE VALUE TASK")

        val intendedDevices = configController.config?.devices

        // Iterate over intended devices
        intendedDevices?.forEach { intendedDevice ->

            // Subscribe values
            intendedDevice.sensors.forEach { s ->
                ReadSensorTask.mqttController.subscribe(SentientProperties.MQTT_SERVER_URI,
                        "${SentientProperties.TOPIC_BASE}/${s.topic}")
            }
        }
    }
}

