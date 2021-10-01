# Monese Tech test
## System requirements:
* Java 1.8
* Maven 3.6.2 or greater

## How to run the application
Run the following command: 
`mvn clean install`

`java -jar target/monese-app-0.0.1-SNAPSHOT.jar`

## API
The application as per request is exposing 2 endpoint:
* Transferring funds between two accounts
    `POST http://localhost:8080/api/v1/account/transfer`
    
* Requesting account statement with account balance and list of transactions
    `GET http://localhost:8080/api/v1/account/{accountId}/statement`
    
## Initial data
On every start of the application data inside the `/monese-app/src/main/resources/data-h2.sql` file 
will be loaded and they will be available until the application is terminated.

## Potential improvements
* More test coverage
* A defined model returned as response of the transferring funds between two accounts endpoint, since it was not provided
the endpoint is just returning a response with the correct HTTP status
* Using a not-in-memory database to store the data after the application is terminated
* Assuming the previous point it might be beneficial to use Docker to run the application locally
