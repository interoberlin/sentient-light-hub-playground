package berlin.intero.sentientlighthubplayground.controller

import berlin.intero.sentientlighthubplayground.model.mapping.MappingConfig
import berlin.intero.sentientlighthubplayground.model.sensor.SensorConfig
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Controller
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.util.logging.Logger

@Controller
class SentientController {

    var sensorConfig: SensorConfig? = null
    var mappingConfig: MappingConfig? = null

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
