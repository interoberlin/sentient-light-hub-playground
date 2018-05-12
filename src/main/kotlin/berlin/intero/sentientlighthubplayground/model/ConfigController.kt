package berlin.intero.sentientlighthubplayground.model

import com.google.gson.GsonBuilder
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.util.logging.Logger

class ConfigController {

    var config: Config? = null

    companion object {
        val log = Logger.getLogger(ConfigController::class.simpleName)
        private const val SCAN_DURATION = 2

        private var inst: ConfigController? = null

        fun getInstance(): ConfigController {
            if (inst == null) {
                inst = ConfigController()
            }

            return inst as ConfigController
        }
    }

    fun loadConfig() = try {
        val result = IOUtils.toString(javaClass.getClassLoader().getResourceAsStream("config.json"));
        this.config = GsonBuilder().create().fromJson(result, Config::class.java) as Config
    } catch (e: IOException) {
        log.severe("$e")
    }
}