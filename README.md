# Building and Running the Application with Gradle

This guide will show you how to build a Java artifact using Gradle and how to run it.

## Prerequisites

- Java 17 Development Kit (JDK) installed on your system.

## Building the Application

1. Open a terminal and navigate to the root directory of the project (where the `build.gradle` file is located).

2. Run the following command to build the application:

```bash
./gradlew clean build
```
this will compile the Java files, run any tests, and package the compiled files into a JAR file. The JAR file will be located in the build/libs directory.

## Running the Application
After building the application, navigate to the build/libs directory:
```bash
cd build/libs
```
Run the following command to start the application:
```bash
java -jar coffee-shop-coding-task-1.0.jar
```

That's it! The application should now be running.

Please note that this is a simple guide and your actual build and run process might be different depending on the specifics of your device.