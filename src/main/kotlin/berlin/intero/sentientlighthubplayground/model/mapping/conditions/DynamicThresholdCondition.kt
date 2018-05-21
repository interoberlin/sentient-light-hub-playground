package berlin.intero.sentientlighthubplayground.model.mapping.conditions

import java.util.logging.Logger

/**
 * The condition is met if the measured value of
 *
 * @param checkerboardID id of the sensor that to be observed
 * @param thresholdType threshold thresholdType
 * @param dynamicThreshold value that must be exceeded to to fulfill this condition
 */
data class DynamicThresholdCondition(
        val checkerboardID: String,
        val thresholdType: EThresholdType,
        private val dynamicThreshold: Int
) : Fulfillable {

    override val conditionType = EConditionType.DYNAMIC_THRESHOLD

    companion object {
        val log: Logger = Logger.getLogger(DynamicThresholdCondition::class.simpleName)
    }

    override fun isFulfilled(checkerboardID: String?, vararg values: Int?): Boolean {
        val averageValue = values[0]
        val currentValue = values[1]

        log.fine("checkerboardID $checkerboardID")
        log.fine("this.checkerboardID ${this.checkerboardID}")
        log.fine("averageValue $averageValue")
        log.fine("currentValue $currentValue")
        log.fine("dynamicThreshold $dynamicThreshold")

        if (this.checkerboardID == checkerboardID && currentValue != null && averageValue != null) {
            return when (thresholdType) {
                EThresholdType.ABOVE_AVERAGE -> {
                    currentValue > averageValue + dynamicThreshold
                }
                EThresholdType.BELOW_AVERAGE -> {
                    currentValue < averageValue - dynamicThreshold
                }
            }
        }

        return false
    }
}


