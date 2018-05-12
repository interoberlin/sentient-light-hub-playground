package berlin.intero.sentientlighthubplayground.controller

import java.util.logging.Logger

class SentientController {

    companion object {
        val log: Logger = Logger.getLogger(SentientController::class.simpleName)

        private var inst: SentientController? = null

        fun getInstance(): SentientController {
            if (inst == null) {
                inst = SentientController()
            }

            return inst as SentientController
        }
    }

    /**
     * Parses a byte array into a string
     */
    fun parse(byteArray: ByteArray): String {
        val parsedValue = String(byteArray)

        log.fine("Parsed value $parsedValue")

        return parsedValue
    }
}
