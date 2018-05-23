package berlin.intero.sentientlighthubplayground.model.actor

data class Strip(
        val index: String,
        val direction: EDirection,
        val leds: List<LED>
)