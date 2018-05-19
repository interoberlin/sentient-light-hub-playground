package berlin.intero.sentientlighthubplayground.model.mapping

import java.util.logging.Logger

data class Action(
        val stripID: String,
        val ledID: String,
        var value: String
): Appliable {

    companion object {
        val log = Logger.getLogger(Action::class.simpleName)
    }

    override fun apply(action: () -> Unit) {
        action.invoke()
    }
}
