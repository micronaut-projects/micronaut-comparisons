# Spring Boot vs Quarkus vs Micronaut Comparisons

This repository houses sample applications intended to compare the performance of [Spring Boot](https://spring.io/projects/spring-boot), [Quarkus](https://quarkus.io/) and [Micronaut](https://micronaut.io) applications.

There are two types of sample projects in this repository.
  1. Simple "Hello World" projects.
  2. Projects from the [JHipster](https://www.jhipster.tech/) sample repositories.

In addition to the sample projects, there is a `loadtests` folder that contains a [Gatling](https://gatling.io) load test suite to measure the performance of the sample applications under load.

## Building The Projects

The three "simple" projects are built with [Gradle](https://gradle.org/).

1. Move into the project folder you would like to build
    + `cd simple-mn`
2. Run the build command
    + `./gradlew clean build` (Mac/Linux)
    + `gradlew clean build` (Windows)
  3. You should see the jar file in the `build/libs` folder

The JHipster projects are built with [Maven](https://maven.apache.org/).

1. Move into the project folder you would like to build
2. Run the build command
    + `./mvnw clean package` (Mac/Linux)
    + `mvnw clean package` (Windows)
3. You should see the jar file in the `target` folder

## Running The Projects

For the purposes of this comparison, all of these projects will be run by executing the built jar file.
In order to mimic a modern Java Service runtime environment, we will be setting `-Xmx128m`

`java -Xmx128m -jar path/to/jar`

All of the projects depend on Java 8 or higher.

## Startup Time

Micronaut, Quarkus and Spring Boot each report how long it took them to start.  To measure startup time, we will rely on the measurements provided by each framework.

+ `java -Xmx128m -jar simple-sb/build/libs/sample-0.0.1-SNAPSHOT.jar`
+ `java -Xmx128m -jar simple-mn/build/libs/sample-0.1-all.jar`
+ `java -Xmx128m -jar simple-mn2/build/libs/sample-0.1-all.jar`
+ `java -Xmx128m -jar simple-qu/build/libs/simple-qu-1.0.0-SNAPSHOT-runner.jar`

## Time to First Response

The time to first response is a more useful measurement as it illustrates the time it takes for a service to begin serving requests.
There is a small Node script called `time.js` for this purpose.  The script will start a provided jar then start issuing http requests to `http://localhost:8080/hello-world`.  Once the endpoint responds with a 200, the script will stop the process and output the number of milliseconds that have elapsed.

+ `node time.js simple-sb/build/libs/sample-0.0.1-SNAPSHOT.jar`
+ `node time.js simple-mn/build/libs/sample-0.1-all.jar`
+ `node time.js simple-mn2/build/libs/sample-0.1-all.jar`
+ `node time.js simple-qu/build/libs/simple-qu-1.0.0-SNAPSHOT-runner.jar`

## Performance Under Load

To measure performance under load, we wanted to use a comparable example that is more complex than your standard "Hello World" REST service.

For this reason, we decided to use sample applications from the JHipster project.  These applications include authentication and authorization, input validation, database interactions, and a number of other configurations that place them closer to the realm of a real application.

This repository contains both the [JHipster Micronaut Sample Application](https://github.com/jhipster/jhipster-sample-app-micronaut) and the [JHipster Spring Boot Sample Application](https://github.com/jhipster/jhipster-sample-app).  Additionally, we have included a version of the Micronaut JHipster sample application that has been upgraded to Micronaut 2.0.0 M2 and a Quarkus based version.

In order to better illustrate the performance of the frameworks, we did modify the JHipster projects to replace the bcrypt encoding and verification of credentials to an essentially no-op implementation.  By design, bcrypt is computationally expensive and leaving it in the sample applications result in load tests that are illustrating the performance of bcrypt with different application wrappers.  Those results are less valuable for this purpose.

To run the load tests:

1. Start the project that you would like to run the load tests against
    + `java -Xmx128m -jar jhipster-sample-app/target/jhipster-sample-application-0.0.1-SNAPSHOT.jar` (Spring Boot)
    + `java -Xmx128m -jar jhipster-sample-app-micronaut/target/jhipster-sample-application-0.0.1-SNAPSHOT.jar` (Micronaut 1.3.4)
    + `java -Xmx128m -jar jhipster-sample-app-micronaut-2/target/jhipster-sample-application-0.0.1-SNAPSHOT.jar` (Micronaut 2.0.0 M2)
    + `java -Xmx128m -jar jhipster-sample-app-quarkus/target/jhipster-1.0.0-SNAPSHOT-runner.jar` (Quarkus)
2. In a new terminal, run the Gatling test suite
    + `cd loadtests`
    + `./gradlew gatlingRun --rerun-tasks` (Mac/Linux)
    + `gradlew gatlingRun --rerun-tasks` (Windows)

By default, the load tests will simulate 1000 users over a duration of 60 seconds.  The scenario includes:
  + Performing a health check
  + Retrieving account information
  + Creating a new bank account

When the tests complete a summary of information will be displayed to the console including number of requests made, the mean requests per second, and the distribution of response times.  Detailed reports are available in `loadtests/build/reports/gatling/`

To measure the memory consumption of the application after a load test is run:
  + `ps x -o rss,vsz,command | grep java` (Mac/Linux)
  + `tasklist` (Windows)

## Test Machine Specifications
  + HP ZBook Laptop
  + Intel(R) Core(TM) i7-6700HQ CPU @ 2.60GHz
  + 16GB of RAM
  + OpenJDK Runtime Environment (build 1.8.0_252-b09)
  + Fedora Linux 31

## Results

### ZBook

Used device: i7-6700HQ  / 16 GB / Fedora Linux 30

|                                            | Spring Boot 2.2 | Micronaut 1.3.4 | Micronaut 2.0.0 M2 |
|:-------------------------------------------|----------------:|----------------:|-------------------:|
| Simple: Start Up (Best of 5)               | 1997 ms         | 955 ms          | 813 ms             |
| Simple: Time to First Response (Best of 5) | 2741 ms         | 1496 ms         | 1295 ms            |
| JHipster: Number of Requests (60s)         | 111,137         | 132,391         | 153,557            |
| JHipster: Mean Response Time               | 444 ms          | 375 ms          | 323 ms             |
| JHipster: Mean Requests Per Second         | 2020.673        | 2407.109        | 2791.945           |
| Memory Consumption After Load Test         | 473.328 MB      | 435.092 MB      | 424.560 MB         |

### ZBook Studio G3

Used Device: ZBook Studio G3 / i7-6700HQ / 32GB / Fedora Linux 32

|                                            | Spring Boot 2.2 | Micronaut 1.3.4 | Micronaut 2.0.0 M2 | Quarkus 1.4 |
|:-------------------------------------------|----------------:|----------------:|-------------------:|------------:|
| Simple: Start Up (Best of 5)               | 2825 ms         | 1041 ms         | 817 ms             | 812ms       |
| Simple: Time to First Response (Best of 5) | 3027 ms         | 1797 ms         | 1499 ms            | 1151ms      |
| JHipster: Number of Requests (60s)         | 109,075         | 122,896         | 139,183            | TODO        | 
| JHipster: Mean Response Time               | 454 ms          | 404 ms          | 349 ms             | TODO        | 
| JHipster: Mean Requests Per Second         | 1983.182        | 2234.473        | 2485.411           | TODO        | 
| Memory Consumption After Load Test         | 478.980 MB      | 509.562 MB      | 517.753 MB         | TODO        | 
