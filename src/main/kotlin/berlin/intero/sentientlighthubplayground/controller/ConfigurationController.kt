package berlin.intero.sentientlighthubplayground.controller

import berlin.intero.sentientlighthubplayground.model.actor.ActorConfig
import berlin.intero.sentientlighthubplayground.model.actor.ActorDevice
import berlin.intero.sentientlighthubplayground.model.mapping.MappingConfig
import berlin.intero.sentientlighthubplayground.model.mapping.conditions.Fulfillable
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

    private const val sensorConfigFileName = "sensors.json"
    private const val actorConfigFileName = "actors.json"
    private const val mappingConfigFileName = "mappings.json"

    var sensorConfig: SensorConfig? = null
    var actorConfig: ActorConfig? = null
    var mappingConfig: MappingConfig? = null

    init {
        loadSensorsConfig(sensorConfigFileName)
        loadActorsConfig(actorConfigFileName)
        loadMappingConfig(mappingConfigFileName)
    }

    fun loadSensorsConfig(configFileName: String) = try {
        val result = IOUtils.toString(javaClass.getClassLoader().getResourceAsStream(configFileName), Charset.defaultCharset())
        this.sensorConfig = GsonBuilder().create().fromJson(result, SensorConfig::class.java) as SensorConfig
    } catch (ex: Exception) {
        when (ex) {
            is IOException -> log.severe("$ex")
            is JsonSyntaxException -> log.severe("$ex")
            else -> throw ex
        }
    }

    fun loadActorsConfig(configFileName: String) = try {
        val result = IOUtils.toString(javaClass.getClassLoader().getResourceAsStream(configFileName), Charset.defaultCharset())
        this.actorConfig = GsonBuilder().create().fromJson(result, ActorConfig::class.java) as ActorConfig
    } catch (ex: Exception) {
        when (ex) {
            is IOException -> log.severe("$ex")
            is JsonSyntaxException -> log.severe("$ex")
            else -> throw ex
        }
    }

    fun loadMappingConfig(configFileName: String) = try {
        val result = IOUtils.toString(javaClass.getClassLoader().getResourceAsStream(configFileName), Charset.defaultCharset())

        val gsonBuilder = GsonBuilder()

        val gson = GsonBuilder()
        gson.registerTypeAdapter(Fulfillable::class.java, FulfillableDeserializer())
        this.mappingConfig = gson.create().fromJson(result, MappingConfig::class.java) as MappingConfig
    } catch (e: IOException) {
        log.severe("$e")
    }

    fun getActor(stripID: Int?, ledID: Int?): ActorDevice? {
        return actorConfig?.actorDevices?.filter { d ->
            d.strips.any { s ->
                s.index == stripID && s.leds.any { l ->
                    l.index == ledID }
            }
        }?.firstOrNull()
    }
}
