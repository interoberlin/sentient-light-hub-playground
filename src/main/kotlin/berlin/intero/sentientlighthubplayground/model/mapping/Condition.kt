package berlin.intero.sentientlighthubplayground.model.mapping

import java.util.logging.Logger

data class Condition(
        val checkerboardID: String,
        val threshold: Int
) : Fulfillable {

    companion object {
        val log = Logger.getLogger(Condition::class.simpleName)
    }

    override fun isFulfilled(checkerboardID: String, value: Int?): Boolean {
        log.info("checkerboardID ${checkerboardID}")
        log.info("this.checkerboardID ${this.checkerboardID}")
        log.info("value ${value}")
        log.info("threshold ${threshold}")

        return this.checkerboardID == checkerboardID && value != null && value > threshold
    }
}


