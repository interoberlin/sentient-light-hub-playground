package berlin.intero.sentientlighthubplayground.controller

import org.springframework.stereotype.Controller
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.logging.Logger

@Controller
object SentientController {

    private val log: Logger = Logger.getLogger(SentientController::class.simpleName)

    /**
     * Parses a byte array into a string
     */
    fun parse(byteArray: ByteArray): ArrayList<String> {
        val parsedValues = ArrayList<String>()

        val bytes: MutableList<Byte> = ArrayList()
        val it = byteArray.iterator()

        while (it.hasNext()) {
            bytes.add(it.nextByte())
        }

        for (i in 0 until bytes.size step 2) {
            val bytesArray = ByteArray(2)
            bytesArray[0] = bytes[i]
            bytesArray[1] = bytes[i + 1]

            val int16 = ByteBuffer.wrap(bytesArray).order(ByteOrder.LITTLE_ENDIAN).short
            log.fine("Parsed value $int16")

            parsedValues.add(int16.toString())
        }

        log.fine("Parsed value ${parsedValues.size}")

        return parsedValues
    }
}
