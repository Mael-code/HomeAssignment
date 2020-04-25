# **Home assignement** 

## Purpose

The purpose is to have an API in order to park car into parking

## Building and running

You can start a the project as a standalone application:
```
$ gradlew clean build bootRun
```

You can build a WAR file as follows:

```
$ gradlew clean build war
```

Then, you can directly deploy the service with embedded Tomcat:

```
$ java -jar build/libs/home-assignment-1.0.0-SNAPSHOT.war
```

The WAR file produced by Gradle can also be deployed in the embedded Jetty container.

Sometimes the gradle processes are not killing properly when you stop the running application. If you receive the message "the port is already in use" on starting microservice, then kill all suspending gradle processes for previous task. You can do it manually or use in IntelliJ IDEA Gradle killer plugin.

## Swagger

Available resources can be listed and tested with Swagger. The associated code is in the **Application.java** file:
To access Swagger API:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

In order to follow the best testing practises it is included testing part of Spring components with Mockito.<br>
For integration test it is provided code in SpringUserRestTest class. For testing REST methods is used RestTemplate from spring framework.<br>

## Code coverage

Code coverage can be generating by running jacoco
```./gradlew jacocoTestReport```
The html report will be available on you favorite browser in the directory build/jacocoHtml/index.html

## Import project in Intelij

File -> Import from existing sources -> build.gradle

Then the lombok plugin must be configured
File -> Settings -> Plugins -> Search for `Lombok` and install it

In order to activate the annotation processing:
Files -> Settings -> Build, Execution, Deployment, -> Compiler -> Annotation processorts -> Select `Enable annotation processing`

