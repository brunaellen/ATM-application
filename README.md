# ATM-Application

The Atm application allows users to check the bank account balance, account details and request a withdrawal. 
The application also allows the bank to check ATM transactions for bank purposes.

### Atm application was made using:

* Spring Boot
* Java 11
* maven
* Docker


### Endpoints

The application has four endpoints:
* /bankAccount/details;
* /bankAccount/balance;
* /bankAccount/withdraw;
* /atm/transactions;

#### POST /bankAccount/details

Endpoints that returns the bank account details:

Usage:

```
curl --location --request POST 'http://localhost:8080/bankAccount/details' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountNumber": 123456789,
    "pin": 1234
}'
```

Response:

```
{
    "accountNumber": 123456789,
    "date": "2022-01-08T11:21:20.955+00:00",
    "balance": 800,
    "availableToSpend": 1000,
    "statements": []
}
```

#### POST /bankAccount/balance

Endpoints that returns the current balance of a bank account:

Usage:

```
curl --location --request POST 'http://localhost:8080/bankAccount/balance' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountNumber": 123456789,
    "pin": 1234
}'
```

Response:

```
{
    "totalFundsForWithdraw": 1000,
    "balance": 800,
    "date": "2022-01-08T11:22:18.624+00:00"
}
```

#### POST /bankAccount/withdraw

Use this endpoint to submit a withdraw request:

Usage:

```
curl --location --request POST 'http://localhost:8080/bankAccount/withdraw/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountNumber": 123456789,
    "pin": 1234,
    "amount": 500
}'
```

Response:

```
{
    "notes": {
        "50": 10,
        "20": 0,
        "5": 0,
        "10": 0
    },
    "date": "2022-01-08T11:24:16.716+00:00",
    "accountNumber": 123456789,
    "balance": 300
}
```

#### GET /atm/transactions

Endpoints that returns the ATM transactions:

Usage:

```
curl --location --request GET 'http://localhost:8080/atm/transactions'
```

Response:

```
{
    "statements": [
        {
            "type": "WITHDRAW",
            "amount": 500,
            "date": "2022-01-08T11:24:16.715+00:00"
        }
    ],
    "notesAvailable": {
        "50": 0,
        "20": 30,
        "10": 30,
        "5": 20
    },
    "date": "2022-01-08T11:28:48.073+00:00",
    "balance": 1000.0
}
```
## How to build

`mvn clean install`

## Running

`mvn spring-boot:run`

### Building docker container

`mvn clean install spring-boot:build-image -Dspring-boot.build-image.imageName=zinkworks/atm-application`

### Running with Docker

`docker run -d -it --rm -p 8080:8080 --name atm docker.io/zinkworks/atm-application:latest`

## Test coverage

`open target/site/jacoco/index.html`
