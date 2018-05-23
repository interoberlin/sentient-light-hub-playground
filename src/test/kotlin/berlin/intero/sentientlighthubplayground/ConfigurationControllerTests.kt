package berlin.intero.sentientlighthubplayground

import berlin.intero.sentientlighthubplayground.controller.ConfigurationController
import berlin.intero.sentientlighthubplayground.model.mapping.conditions.AbsoluteThresholdCondition
import berlin.intero.sentientlighthubplayground.model.mapping.conditions.DynamicThresholdCondition
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.logging.Logger

@RunWith(SpringRunner::class)
@SpringBootTest
class ConfigurationControllerTests {

    @Before
    fun setup() {
        ConfigurationController.loadActorsConfig("test-actors.json")
    }

    @Test
    fun testSensorsConfig() {
        ConfigurationController.loadSensorsConfig("test-sensors.json")
        assert(ConfigurationController.sensorConfig?.sensorDevices?.size == 1)
    }

    // @Test
    fun testGetActorExisting() {
        assert(ConfigurationController.getActor(5, 5) != null)
    }

    // @Test
    fun testGetActorNonExisting() {
        assert(ConfigurationController.getActor(2, 2) == null)
    }

    // @Test
    fun testMappingAbsoluteCondition() {
        ConfigurationController.loadMappingConfig("test-mapping-absolute-condition.json")
        assert(ConfigurationController.mappingConfig?.mappings?.get(0)?.condition is AbsoluteThresholdCondition)
    }

    // @Test
    fun testMappingDynamicCondition() {
        ConfigurationController.loadMappingConfig("test-mapping-dynamic-condition.json")
        assert(ConfigurationController.mappingConfig?.mappings?.get(0)?.condition is DynamicThresholdCondition)
    }
}
