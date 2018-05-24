# Sentient Light Hub Playground

## Purpose

This proof-of-concept app demonstrates all the features of _Sentient Light Hub_. This includes

* Communication with GATT devices (via Bluetooth Smart)
* Communication with MQTT broker
* Mapping engine


### Features

tbd

### Planned features

tbd

## Development

### Compilation

This application can be compiled using the following command. The created jar file can be found in ./build/libs

```
./gradlew bootJar
```

### Configuration



### Launch

During development this application can be launched in two ways

* Compile the application running the command above and start jar file

```
java -jar ./build/libs/sentient-light-hub-playground-0.0.1-SNAPSHOT.jar
```

* Run the application using gradle

```
./gradlew bootRun
```

## Operations

### Service

This application can be run as a systemd service using the following command

```
systemctl start sentient-light-hub-playground.service
```

This will run the jar file ```/opt/sentient-light-hub-playground/sentient-light-hub-playground``` (which is a symlink to a specific version of the jar file in same directory) The service is can stopped and restarted using regular systemd commands.

### Monitoring

Once it is started as a service the apllication can be monitored using the following journalctl command

```
journalctl -u sentient-light-hub-playground.service -f
```