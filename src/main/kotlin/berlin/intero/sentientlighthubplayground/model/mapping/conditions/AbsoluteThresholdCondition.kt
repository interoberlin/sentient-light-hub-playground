package berlin.intero.sentientlighthubplayground.model.mapping.conditions

import java.util.logging.Logger

/**
 * The condition is met if the measured value of
 *
 * @param checkerboardID id of the sensor that to be observed
 * @param threshold value that must be exceeded to to fulfill this condition
 */
data class AbsoluteThresholdCondition(
        val checkerboardID: String,
        val threshold: Int
) : Fulfillable {
    override val conditionType = EConditionType.ABSOLUTE_THRESHOLD


    companion object {
        val log = Logger.getLogger(AbsoluteThresholdCondition::class.simpleName)
    }

    override fun isFulfilled(checkerboardID: String?, vararg values: Int?): Boolean {
        val currentValue = values[0]

        log.fine("checkerboardID ${checkerboardID}")
        log.fine("this.checkerboardID ${this.checkerboardID}")
        log.fine("currentValue ${currentValue}")
        log.fine("threshold ${threshold}")

        return this.checkerboardID == checkerboardID && currentValue != null && currentValue > threshold
    }
}


