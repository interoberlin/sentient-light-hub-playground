package berlin.intero.sentientlighthubplayground.controller

import berlin.intero.sentientlighthubplayground.model.Config
import com.google.gson.GsonBuilder
import org.apache.commons.io.IOUtils
import java.io.IOException
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
        val result = IOUtils.toString(javaClass.getClassLoader().getResourceAsStream("config.json"));
        this.config = GsonBuilder().create().fromJson(result, Config::class.java) as Config
    } catch (e: IOException) {
        log.severe("$e")
    }

    /**
     * Parses a byte array into a string
     */
    fun parse(byteArray: ByteArray): String {
        val parsedValue = String(byteArray)

        log.fine("Parsed value $parsedValue")

        // TODO Remove millis value when parsing works correctly
        return "$parsedValue ${System.currentTimeMillis() % 100}"
    }
}
