package berlin.intero.sentientlighthubplayground.model.mapping

import berlin.intero.sentientlighthubplayground.tasks.GATTWriteSensorAsyncTask
import org.springframework.core.task.SimpleAsyncTaskExecutor
import java.util.logging.Logger

data class Action(val address: String, val characteristicID: String, var value: String): Appliable {
    companion object {
        val log = Logger.getLogger(Action::class.simpleName)
    }

    override fun apply() {
        log.info("Apply action $address / $characteristicID / $value")

        // Call MQTTPublishAsyncTask
        val gattWriteSensorAsyncTask = GATTWriteSensorAsyncTask()
        gattWriteSensorAsyncTask.address = this.address
        gattWriteSensorAsyncTask.characteristicID = this.characteristicID

        when (value) {
            "0" ->   gattWriteSensorAsyncTask.value = byteArrayOf(0x00)
            "1" ->   gattWriteSensorAsyncTask.value = byteArrayOf(0x01)
        }

        SimpleAsyncTaskExecutor().execute(gattWriteSensorAsyncTask)
    }
}
