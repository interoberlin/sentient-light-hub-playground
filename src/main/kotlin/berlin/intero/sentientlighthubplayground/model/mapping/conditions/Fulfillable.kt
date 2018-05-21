package berlin.intero.sentientlighthubplayground.model.mapping.conditions

interface Fulfillable {

    val conditionType: EConditionType?

    fun isFulfilled(checkerboardID: String?, vararg values: Int?): Boolean
}