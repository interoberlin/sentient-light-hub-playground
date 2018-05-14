package berlin.intero.sentientlighthubplayground.controller

import berlin.intero.sentientlighthubplayground.model.Config
import com.google.gson.GsonBuilder
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.util.logging.Logger


class SentientController {

    var config: Config? = null

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

    fun loadConfig() = try {
        val result = IOUtils.toString(javaClass.getClassLoader().getResourceAsStream("config.json"), Charset.defaultCharset())
        this.config = GsonBuilder().create().fromJson(result, Config::class.java) as Config
    } catch (e: IOException) {
        log.severe("$e")
    }

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
            bytesArray[1] = bytes[i+1]

            val int16 = ByteBuffer.wrap(bytesArray).order(ByteOrder.LITTLE_ENDIAN).short
            log.fine("Parsed value $int16")

            parsedValues.add(int16.toString())
        }

        log.fine("Parsed value ${parsedValues.size}")

        return parsedValues
    }
}
