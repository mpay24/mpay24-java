# mpay24-java
Offical mPAY24 Java Payment SDK

## Requirements

Java 1.6 or later.

## Documentation

A short demo implementation guide is available at https://docs.mpay24.com/docs/get-started</br>
Documentation is available at https://docs.mpay24.com/docs/java-sdk.

## SDK Overview
### mpay24 class
The Mpay24 class is instantiated using your merchant id, your soap password and the environment you are connecting to (`TEST` or `PRODUCTION`):
```java
Mpay24 mpay24 = new Mpay24("merchantID", "password", Environment.TEST);
```

The different payment calls can be found at https://docs.mpay24.com/docs/java-sdk
