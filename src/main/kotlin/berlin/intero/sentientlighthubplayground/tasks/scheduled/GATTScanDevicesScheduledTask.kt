package berlin.intero.sentientlighthubplayground.tasks.scheduled

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.controller.TinybController
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.logging.Logger

/**
 * This scheduled task scans for GATT devices and displays them
 */
@Component
@ConditionalOnProperty("sentient.scandevices.enabled", havingValue = "true", matchIfMissing = false)
class GATTScanDevicesScheduledTask {

    companion object {
        private val log: Logger = Logger.getLogger(GATTScanDevicesScheduledTask::class.simpleName)
    }

    @Scheduled(fixedRate = SentientProperties.SENSORS_SCAN_RATE)
    @SuppressWarnings("unused")
    fun scanDevices() {
        log.info("${SentientProperties.ANSI_GREEN}-- GATT SCAN SENSORS TASK${SentientProperties.ANSI_RESET}")

        TinybController.scanDevices()
        TinybController.showDevices(TinybController.scannedDevices)
    }
}
