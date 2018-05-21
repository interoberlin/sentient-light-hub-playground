package berlin.intero.sentientlighthubplayground.model.mapping.conditions

enum class EConditionType
constructor(val value: String, val c: Class<*>) {

    ABSOLUTE_THRESHOLD("absolute_threshold", AbsoluteThresholdCondition::class.java),
    DYNAMIC_THRESHOLD("dynamic_threshold", DynamicThresholdCondition::class.java);

    companion object {

        fun fromString(name: String): EConditionType? {
            for (e in EConditionType.values()) {
                if (e.name == name)
                    return e
            }

            return null
        }

        val valuesList: ArrayList<String>
            get() {
                val values = ArrayList<String>()

                for (e in EConditionType.values()) {
                    values.add(e.name)
                }

                return values
            }
    }
}