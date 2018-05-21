package berlin.intero.sentientlighthubplayground.model.mapping.actions

interface Appliable {

    fun apply(action: () -> Unit)
}