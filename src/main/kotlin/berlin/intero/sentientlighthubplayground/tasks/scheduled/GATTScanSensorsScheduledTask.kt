package berlin.intero.sentientlighthubplayground.tasks.scheduled

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.TinybController
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class GATTScanSensorsScheduledTask {
    companion object {
        private val log: Logger = Logger.getLogger(GATTScanSensorsScheduledTask::class.simpleName)
    }

    @Scheduled(fixedRate = SentientProperties.SENSORS_SCAN_RATE)
    fun scanDevices() {
        log.info("-- GATT SCAN SENSORS TASK")

        TinybController.scanDevices()
        TinybController.showDevices(TinybController.scannedDevices)
    }
}