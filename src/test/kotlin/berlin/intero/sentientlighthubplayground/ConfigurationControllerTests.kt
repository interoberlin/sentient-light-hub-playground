package berlin.intero.sentientlighthubplayground

import berlin.intero.sentientlighthubplayground.controller.ConfigurationController
import berlin.intero.sentientlighthubplayground.model.mapping.conditions.AbsoluteThresholdCondition
import berlin.intero.sentientlighthubplayground.model.mapping.conditions.DynamicThresholdCondition
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class ConfigurationControllerTests {

    @Before
    fun setup() {
    }

    // @Test
    fun testSensorsConfig() {
        ConfigurationController.loadSensorsConfigFile("test-sensors.json")
        assert(ConfigurationController.sensorsConfig?.sensorDevices?.size == 1)
    }

    @Test
    fun testGetActorMakerFaire() {
        ConfigurationController.loadActorsConfigFile("test-actors-makerfaire2018.json")
        assert(ConfigurationController.getActor("1", "1") != null)
        assert(ConfigurationController.getActor("1", "1")?.address == "87:10:DC:E9:6D:50")
    }

    @Test
    fun testGetActorExisting() {
        ConfigurationController.loadActorsConfigFile("test-actors.json")
        assert(ConfigurationController.getActor("5", "5") != null)
    }

    @Test
    fun testGetActorNonExisting() {
        ConfigurationController.loadActorsConfigFile("test-actors.json")
        assert(ConfigurationController.getActor("2", "2") == null)
    }

    // @Test
    fun testMappingAbsoluteCondition() {
        ConfigurationController.loadMappingsConfigFile("test-mapping-absolute-condition.json")
        assert(ConfigurationController.mappingsConfig?.mappings?.get(0)?.condition is AbsoluteThresholdCondition)
    }

    @Test
    fun testMappingDynamicCondition() {
        ConfigurationController.loadMappingsConfigFile("test-mapping-dynamic-condition.json")
        assert(ConfigurationController.mappingsConfig?.mappings?.get(0)?.condition is DynamicThresholdCondition)
    }
}
