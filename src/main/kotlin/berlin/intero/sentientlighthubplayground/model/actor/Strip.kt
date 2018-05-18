package berlin.intero.sentientlighthubplayground.model.actor

data class Strip(
        val index: Int,
        val direction: EDirection,
        val leds: List<LED>
)