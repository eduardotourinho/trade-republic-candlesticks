# Candlestick challenge

This is the implementation of the candlestick challenge, described in this [README.md](docs%2FREADME.md)

## Content

- [Tech stack](#tech-stack)
- [Assumptions](#assumptions)
- [Future improvement](#future-improvements)
- [Tests](#tests)
- [Code Architecture](#code-architecture)
- [Running the Partner Service](#running-the-partner-service)
- [Running the app](#running-the-app)

## Tech stack

The tech stack used to implement this challenge is:

- Java 17
- Springboot 3.0.5
- H2 embedded DB
- Lombok

## Assumptions

- The system will return at most 30 candlesticks. If we only have quotes for a given ISIN for the past 10 minutes, it will return only 10 candlesticks. However, the gaps between the first candlestick timestamp and the last timestamp are filled, according the requirements;
- The number of requests to get the candlesticks initially will be small enough, so we can aggregate the data in real-time;
- The deleted `Instruments` and its `Quotes` history are no longer needed so we can physically delete them from the database;
- All values are displayed with 2 decimal places;
- All timestamps are saved and returned with UTC offset; 
- Candlesticks for the current minute are not returned (i.e if someone makes request at "12:02", the last returned candlesticks will be from the `openingTimestamp` "12:01");


## Future improvements

- Proper error handling
- Use a NoSQL instead of SQL database
- Pre-calculate the candlesticks instead of calculating them on real-time 
- Add a caching layer
- Stream the events (i.e. using Kafka, SQS) received through the websocket
- Use gRPC instead of WebSocket to communicate with the `Partner` service (if possible)

## Tests

All the test were implemented considering only the "happy-path" of the application. There are some tests for some exceptions, however it needs to be improved.

Before deploying to production, we MUST implement all the missing tests for this (i.e. handling exceptions, integration tests for websocket, and others.

### Missing tests

- General error handling. 
- `websocket` adapter tests.

## Code Architecture

The code is organized based on the Hexagonal architecture:

- `adapters` are the implementation for the external dependencies of the application (i.e. Database, Rest request, Websocket communication)
- `application` contains the business logic for the application. The communication with external dependencies are done through the defined `ports`.

### Simplified View

![Candlesticks hexagonal design.jpg](docs%2FCandlesticks%20hexagonal%20design.jpg)

## Running the Partner Service

To run a partner service you can either use docker-compose. Docker v3 or above will require slight changes to the docker-compose files.
``` 
cd partner-server
docker-compose up -d
```
or Java
```
java -jar partner-service-1.0.1-all.jar --port=8032
```

## Running the app

You need to start the `partner-server` first, before running the application 

### Gradle
To run the app you can use the following gradle commands
```
./gradlew build
./gradlew test
./gradlew bootRun
```

### IntelliJ
If you want to use `IntelliJ` to run the application, run the file [TradeRepublicCandlesticksApplication.java](src%2Fmain%2Fjava%2Fcom%2Ftraderepublic%2FTradeRepublicCandlesticksApplication.java)

Once the server is running you can check the results at
```
http://localhost:8080/candlesticks?isin={ISIN}
```

