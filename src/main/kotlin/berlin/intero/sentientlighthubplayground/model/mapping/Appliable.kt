package berlin.intero.sentientlighthubplayground.model.mapping

interface Appliable {

    fun apply(action: () -> Unit)
}