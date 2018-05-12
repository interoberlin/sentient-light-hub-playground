package berlin.intero.sentientlighthubplayground

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("sentient")
class SentientProperties {
    companion object {
        const val characteristic_sensor = "00002014-0000-1000-8000-00805f9b34fb"
        const val topic_base = "/sentientlight"
    }
}