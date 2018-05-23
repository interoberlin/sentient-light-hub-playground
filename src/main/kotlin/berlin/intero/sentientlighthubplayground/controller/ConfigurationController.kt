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

    var sensorsConfig: SensorConfig? = null
    var actorsConfig: ActorConfig? = null
    var mappingsConfig: MappingConfig? = null

    init {
        loadSensorsConfigFile(sensorConfigFileName)
        loadActorsConfigFile(actorConfigFileName)
        loadMappingsConfigFile(mappingConfigFileName)
    }

    fun loadSensorsConfig(value: String) = try {
        this.sensorsConfig = GsonBuilder().create().fromJson(value, SensorConfig::class.java) as SensorConfig
    } catch (ex: Exception) {
        when (ex) {
            is JsonSyntaxException -> log.severe("$ex")
            else -> throw ex
        }
    }

    fun loadSensorsConfigFile(configFileName: String) = try {
        val value = IOUtils.toString(javaClass.classLoader.getResourceAsStream(configFileName), Charset.defaultCharset())
        loadSensorsConfig(value)
    } catch (ex: Exception) {
        when (ex) {
            is IOException -> log.severe("$ex")
            else -> throw ex
        }
    }

    fun loadActorsConfig(value: String) = try {
        this.actorsConfig = GsonBuilder().create().fromJson(value, ActorConfig::class.java) as ActorConfig
    } catch (ex: Exception) {
        when (ex) {
            is JsonSyntaxException -> log.severe("$ex")
            else -> throw ex
        }
    }

    fun loadActorsConfigFile(configFileName: String) = try {
        val value = IOUtils.toString(javaClass.classLoader.getResourceAsStream(configFileName), Charset.defaultCharset())
        loadActorsConfig(value)
    } catch (ex: Exception) {
        when (ex) {
            is IOException -> log.severe("$ex")
            else -> throw ex
        }
    }

    fun loadMappingsConfig(value: String) = try {
        val gson = GsonBuilder()
        gson.registerTypeAdapter(Fulfillable::class.java, FulfillableDeserializer())
        this.mappingsConfig = gson.create().fromJson(value, MappingConfig::class.java) as MappingConfig
    } catch (ex: Exception) {
        when (ex) {
            is JsonSyntaxException -> log.severe("$ex")
            else -> throw ex
        }
    }

    fun loadMappingsConfigFile(configFileName: String) = try {
        val value = IOUtils.toString(javaClass.classLoader.getResourceAsStream(configFileName), Charset.defaultCharset())
        loadMappingsConfig(value)
    } catch (e: IOException) {
        log.severe("$e")
    }

    fun getActor(stripID: String?, ledID: String?): ActorDevice? {
        log.info("getActor stripID $stripID / ledID $ledID")

        return actorsConfig?.actorDevices?.filter { d ->
            d.strips.any { (stripIndex, _, leds) ->
                stripID != null && stripIndex == stripID && leds.any { (ledIndex) ->
                    ledID != null && ledID == ledIndex
                }
            }
        }?.firstOrNull()
    }
}
