package berlin.intero.sentientlighthubplayground.tasks.scheduled

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.SentientController
import berlin.intero.sentientlighthubplayground.controller.TinybController
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class GATTScanSensorsScheduledTask {
    companion object {
        val log: Logger = Logger.getLogger(GATTScanSensorsScheduledTask::class.simpleName)
        val sentientController = SentientController.getInstance()
        val tinybController = TinybController.getInstance()
    }

    @Scheduled(fixedRate = SentientProperties.SENSORS_SCAN_RATE)
    fun scanDevices() {
        log.info("-- GATT SCAN SENSORS TASK")

        tinybController.scanDevices()
        tinybController.showDevices(tinybController.scannedDevices)
    }
}
