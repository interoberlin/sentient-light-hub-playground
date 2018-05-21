package berlin.intero.sentientlighthubplayground.controller

import berlin.intero.sentientlighthubplayground.model.mapping.conditions.EConditionType
import berlin.intero.sentientlighthubplayground.model.mapping.conditions.Fulfillable
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.*

class FulfillableDeserializer : JsonDeserializer<Fulfillable> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): Fulfillable {

        val type = json.asJsonObject.get("conditionType").asString
        val c = EConditionType.valueOf(type.toUpperCase(Locale.GERMAN)).c

        return context.deserialize<Fulfillable>(json, c)
    }
}
