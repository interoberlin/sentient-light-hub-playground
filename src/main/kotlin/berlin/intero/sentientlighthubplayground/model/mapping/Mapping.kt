package berlin.intero.sentientlighthubplayground.model.mapping

import berlin.intero.sentientlighthubplayground.model.mapping.actions.Action
import berlin.intero.sentientlighthubplayground.model.mapping.conditions.Fulfillable

data class Mapping(
        val condition: Fulfillable,
        val action: Action
)
