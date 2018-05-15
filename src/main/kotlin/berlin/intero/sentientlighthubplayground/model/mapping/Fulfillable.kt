package berlin.intero.sentientlighthubplayground.model.mapping

interface Fulfillable {
    fun isFulfilled(checkerboardID: String, value: Int?): Boolean
}