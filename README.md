# Sentient Light Hub Playground

## Purpose

This proof-of-concept app demonstrates all the features of _Sentient Light Hub_. This includes

* Communication with GATT devices (via Bluetooth Smart)
* Communication with MQTT broker
* Mapping engine

## Development

### Compilation

This app can be compiled using the following command. The created jar file can be found in ./build/libs

```
./gradlew bootJar
```

### Configuration

The app can be configured in two different places

* ```src/main/resources/application.properties``` contains boolean values that can be used to enable/disable different parts of the app
* ```src/main/kotlin/berlin/intero/sentientlighthubplayground/SentientProperties``` contains default values for
  * GATT ID for services and characteristics
  * MQTT connection parameters and topic names
  * Scheduled tasks rates/delay in milliseconds
    * *SENSORS_SCAN_RATE* how often there is a GATT scan for new devices
    * *SENSOR_READ_DELAY* how often values are read from a GATT device
    * *SENTIENT_MAPPING_DELAY* how often the mapping engine evaluates measured values

### Launch

During development this app can be launched in two ways

* Compile the app running the command above and start jar file

```
java -jar ./build/libs/sentient-light-hub-playground-0.0.1-SNAPSHOT.jar
```

* Run the app using gradle

```
./gradlew bootRun
```

## Operations

### Service

This app can be run as a systemd service using the following command

```
systemctl start sentient-light-hub-playground.service
```

This will run the jar file ```/opt/sentient-light-hub-playground/sentient-light-hub-playground``` (which is a symlink to a specific version of the jar file in same directory) The service is can stopped and restarted using regular systemd commands.

### Monitoring

Once it is started as a service the apllication can be monitored using the following journalctl command

```
journalctl -u sentient-light-hub-playground.service -f
```