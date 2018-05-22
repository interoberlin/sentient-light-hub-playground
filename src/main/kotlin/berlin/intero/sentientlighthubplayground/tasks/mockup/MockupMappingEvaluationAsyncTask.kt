package berlin.intero.sentientlighthubplayground.tasks.mockup

import berlin.intero.sentientlighthubplayground.SentientProperties
import berlin.intero.sentientlighthubplayground.model.mapping.Mapping
import berlin.intero.sentientlighthubplayground.model.mapping.conditions.AbsoluteThresholdCondition
import berlin.intero.sentientlighthubplayground.model.mapping.conditions.DynamicThresholdCondition
import java.util.logging.Logger

/**
 * This async task evaluates a mapping and triggers the mapping's action
 * if the mapping's condition is fulfilled
 *
 * @param mapping to be evaluated
 * @param valuesCurrent current values
 * @param valuesAverage average values
 */
class MockupMappingEvaluationAsyncTask(
        val mapping: Mapping,
        val valuesCurrent: Map<String, String>,
        val valuesAverage: Map<String, String>
) : Runnable {

    companion object {
        private val log: Logger = Logger.getLogger(MockupMappingEvaluationAsyncTask::class.simpleName)
    }

    override fun run() {
        log.info("-- MOCKUP MAPPING EVALUATION TASK")

        val condition = mapping.condition
        val action = mapping.action
        var fulfilled = false

        when (condition) {

            is AbsoluteThresholdCondition -> {
                val checkerboardID = condition.checkerboardID
                val value = valuesCurrent[checkerboardID]

                fulfilled = condition.isFulfilled(checkerboardID, value?.toIntOrNull())
            }

            is DynamicThresholdCondition -> {
                val checkerboardID = condition.checkerboardID
                val averageValue = valuesAverage[checkerboardID]
                val value = valuesCurrent[checkerboardID]

                fulfilled = condition.isFulfilled(checkerboardID, averageValue?.toIntOrNull(), value?.toIntOrNull())

                log.info("avg:$averageValue / val:$value ($fulfilled)")
            }
        }

        if (fulfilled) {
            action.apply {
                val topic = "${SentientProperties.TOPIC_LED}/${action.stripID}/${action.ledID}"

                log.warning("TRIGGERED")

                // Call MQTTPublishAsyncTask
                // SimpleAsyncTaskExecutor().execute(MQTTPublishAsyncTask(topic, action.value))
            }
        }
    }
}
