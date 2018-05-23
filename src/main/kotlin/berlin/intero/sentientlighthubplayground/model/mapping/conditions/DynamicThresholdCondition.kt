package berlin.intero.sentientlighthubplayground.model.mapping.conditions

import berlin.intero.sentientlighthubplayground.SentientProperties
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

        log.info("${SentientProperties.ANSI_CYAN}checkerboardID $checkerboardID${SentientProperties.ANSI_RESET}")
        log.info("${SentientProperties.ANSI_CYAN}this.checkerboardID ${this.checkerboardID}${SentientProperties.ANSI_RESET}")
        log.info("${SentientProperties.ANSI_CYAN}averageValue $averageValue${SentientProperties.ANSI_RESET}")
        log.info("${SentientProperties.ANSI_CYAN}currentValue $currentValue${SentientProperties.ANSI_RESET}")
        log.info("${SentientProperties.ANSI_CYAN}dynamicThreshold $dynamicThreshold${SentientProperties.ANSI_RESET}")

        if (this.checkerboardID == checkerboardID && currentValue != null && averageValue != null) {
            when (thresholdType) {
                EThresholdType.ABOVE_AVERAGE -> {
                    log.info("${SentientProperties.ANSI_YELLOW}fulfilled ABOVE_AVERAGE${SentientProperties.ANSI_RESET}")
                    return currentValue > averageValue + dynamicThreshold
                }
                EThresholdType.BELOW_AVERAGE -> {
                    log.info("${SentientProperties.ANSI_YELLOW}fulfilled BELOW_AVERAGE${SentientProperties.ANSI_RESET}")
                    return currentValue < averageValue - dynamicThreshold
                }
            }
        }

        return false
    }
}


