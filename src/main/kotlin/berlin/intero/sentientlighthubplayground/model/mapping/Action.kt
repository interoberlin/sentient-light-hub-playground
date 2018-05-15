package berlin.intero.sentientlighthubplayground.model.mapping

import java.util.logging.Logger

data class Action(val address: String, val characteristic: String, val value: String): Appliable {
    companion object {
        val log = Logger.getLogger(Action::class.simpleName)
    }

    override fun apply() {
        log.info("BAAAAAM $address / $characteristic / $value")
    }
}
