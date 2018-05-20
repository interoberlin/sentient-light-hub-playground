package berlin.intero.sentientlighthubplayground.tasks.async

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.model.mapping.Mapping
import org.springframework.core.task.SimpleAsyncTaskExecutor
import java.util.logging.Logger

/**
 * This async task evaluates a mapping and triggers the mapping's action
 * if the mapping's condition is fulfilled
 *
 * @param mapping to be evaluated
 * @param values current values
 */
class SentientMappingEvaluationAsyncTask(
        val mapping: Mapping,
        val values: Map<String, String>
) : Runnable {

    companion object {
        private val log: Logger = Logger.getLogger(SentientMappingEvaluationAsyncTask::class.simpleName)
    }

    override fun run() {
        log.info("-- SENTIENT MAPPING EVALUATION TASK")

        val condition = mapping.condition
        val action = mapping.action

        val checkerboardID = condition.checkerboardID
        val value = values[checkerboardID]

        if (condition.isFulfilled(checkerboardID, value?.toIntOrNull())) {

            action.apply {
                val topic = "${SentientProperties.TOPIC_LED}/${action.stripID}/${action.ledID}"

                // Call MQTTPublishAsyncTask
                SimpleAsyncTaskExecutor().execute(MQTTPublishAsyncTask(topic, value.orEmpty()))
            }
        }
    }
}
