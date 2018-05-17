package berlin.intero.sentientlighthubplayground.controller

import berlin.intero.sentientlighthubplayground.model.mapping.MappingConfig
import berlin.intero.sentientlighthubplayground.model.sensor.SensorConfig
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Controller
import java.io.IOException
import java.nio.charset.Charset
import java.util.logging.Logger

@Controller
object ConfigurationController {

    private val log: Logger = Logger.getLogger(ConfigurationController::class.simpleName)

    var sensorConfig: SensorConfig? = null
    var mappingConfig: MappingConfig? = null

    init {
        loadSensorsConfig()
        loadMappingConfig()
    }

    fun loadSensorsConfig() = try {
        val result = IOUtils.toString(javaClass.getClassLoader().getResourceAsStream("sensors.json"), Charset.defaultCharset())
        this.sensorConfig = GsonBuilder().create().fromJson(result, SensorConfig::class.java) as SensorConfig
    } catch (ex: Exception) {
        when (ex) {
            is IOException -> log.severe("$ex")
            is JsonSyntaxException -> log.severe("$ex")
            else -> throw ex
        }
    }

    fun loadMappingConfig() = try {
        val result = IOUtils.toString(javaClass.getClassLoader().getResourceAsStream("mapping.json"), Charset.defaultCharset())
        this.mappingConfig = GsonBuilder().create().fromJson(result, MappingConfig::class.java) as MappingConfig
    } catch (e: IOException) {
        log.severe("$e")
    }
}
