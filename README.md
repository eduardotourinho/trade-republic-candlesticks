# Candlestick challenge

This is the implementation of the candlestick challenge, described in this [README.md](docs%2FREADME.md)

## Content

- [Tech stack](#tech-stack)
- [Assumptions](#assumptions)
- [Future improvement](#future-improvements)
- [Tests](#tests)
- [Code Architecture](#code-architecture)
- [Running the app](#running-the-app)

## Tech stack

The tech stack used to implement this challenge is:

- Java 17
- Springboot 3.0.5
- H2 embedded DB
- Lombok

## Assumptions

- The number of requests to get the candlesticks are small, so we can aggregate the data on real-time;
- The deleted `Instruments` and its `Quotes` history are no longer needed so we can physically delete them from the database

## Future improvements

- Proper error handling/resilience
- Use a NoSQL instead of SQL database
- Pre-aggregate the candlesticks instead of calculating them on real-time 
- Caching
- Stream (i.e. using Kafka, PubSub, SQS) the events received through the websocket
- Use gRPC instead of WebSocket to communicate with the `Partner` service (if possible)

## Tests

All the test were implemented considering only the "happy-path" of the application. There are some tests regarding exceptional errors, however they need to be improved.

Before deploying to production, we MUST implement all the missing tests for this.

### Missing tests

- General error handling. 
- `websocket` adapter tests.

## Code Architecture

The code is organized based on the Hexagonal architecture:

- `adapters` are the implementation for the external dependencies of the application (i.e. Database, Rest request, Websocket communication)
- `application` contains the business logic for the application. The communication with external dependencies are done through the defined `ports`.

### Simplified View

![Candlesticks hexagonal design.jpg](docs%2FCandlesticks%20hexagonal%20design.jpg)

## Running the app

### Gradle
To run the app you can use the following gradle commands
```
./gradlew build
./gradlew test
./gradlew run
```
### IntelliJ
If you want to use `IntelliJ` to run the application, run the file [TradeRepublicCandlesticksApplication.java](src%2Fmain%2Fjava%2Fcom%2Ftraderepublic%2FTradeRepublicCandlesticksApplication.java)

Once the server is running you can check the results at
```
http://localhost:8080/candlesticks?isin={ISIN}
```