# Sentry Tracing Performance Test

## Configure tracing

Configure Sentry tracing properties in `src/main/application.properties`.

## Build the application

`./mvnw package`

## Run the application

`nohup java -jar target/sentry-perf-test-demo-0.0.1-SNAPSHOT.jar > console.out`

## Run performance tests

From another terminal window:

`./mvnw gatling:test`