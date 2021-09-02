# simple-as4-client
This repository contains the simple-as4 Client, made for the DMS project to help economical operators communicate with the AS4 gateway.

##Quick start guide
* Add the simple-as4-client dependency to your project.
* Configure a Client using the ClientBuilder (builder pattern)
* Use the Client to send data with the method ExecutePush.

For an example on how to setup a simple client and a more advanced client, meant to simulate being behind a proxy, please refer to the [example project](https://github.com/skat/simple-as4-client/tree/main/example) attached to this repository.


##How to add simple-as4-client to your Java project
Maven dependency:
```xml
<dependency>
    <groupId>io.github.skat</groupId>
    <artifactId>as4-client</artifactId>
    <version>1.0.0</version>
    <scope>runtime</scope>
</dependency>
```

Gradle dependency:
```json
implementation 'io.github.skat:as4-client:1.0.0'
```
