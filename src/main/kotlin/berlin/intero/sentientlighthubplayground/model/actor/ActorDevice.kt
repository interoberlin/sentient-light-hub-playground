package berlin.intero.sentientlighthubplayground.model.actor

data class ActorDevice(
        val address: String,
        val description: String,
        val strips: List<Strip>
)