# Calculator RESTful Api

## Description

This is a simple calculator RESTful API built with Spring Boot. It provides endpoints for basic arithmetic operations:
addition, subtraction, multiplication, and division.

It contains three modules:

- [**rest**](rest): The main module that contains the RESTful API for the calculator.
- [**calculator**](calculator): The module that contains the calculator logic.
- [**common**](common): The module that contains common classes and utilities used by the other modules.

## How to build & run the project

The project uses Docker and Docker Compose for containerization and orchestration. It also uses Gradle as the build
tool.

1. Clone the project:
   ```bash
   git clone https://github.com/rnribeiro/wit-challenge.git
   ```

2. To build the project, run the following command in the root directory of the project:
   ```bash
   .\gradlew clean build
   ```

   To build without tests, use:

   ```bash
    .\gradlew clean build -x test
    ```
3. To run the project, use the following command:
   ```bash
    docker-compose up --build
    ```

This will build the Docker images and start the containers for the API and the database. The API will be accessible at
`http://localhost:8080`.

## API Endpoints

The API provides the following endpoints for basic arithmetic operations:

- **Addition**: `GET /sum?a={num1}&b={num2}`
- **Subtraction**: `GET /subtraction?a={num1}&b={num2}`
- **Multiplication**: `GET /multiplication?a={num1}&b={num2}`
- **Division**: `GET /division?a={num1}&b={num2}`

`num1` and `num2` are the numbers to be used in the operation. The API will return the result of the operation in JSON
format.
In case of an error (e.g., division by zero, invalid operand input), the API will return an error message.



