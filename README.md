# Cruisebound

Cruisebound - Jorge Backend Engineer Assessment

## Preparation

You must have installed the following tools:

- [<u>Java 17 or later</u>](https://www.oracle.com/java/technologies/downloads/) needed to build the project
- [<u>maven 3.5+</u>](https://maven.apache.org/) needed to build the project

### Local environment

- [<u>PostgreSQL 15</u>](https://www.postgresql.org/download/)

### Docker

- [<u>Docker</u>](https://docs.docker.com/desktop/install/mac-install/)

## Configuration

### Local environment

Before running the project you should create a new PostgreSQL database.
You should modify the applications.properties file with the parameters you used to create the database.
As you can see I used the sailings database with the "postgres" user and the "toor" password

- spring.datasource.url=jdbc:postgresql://localhost:5432/sailings
- spring.datasource.username=postgres
- spring.datasource.password=toor

### Docker

The Dockerfile will be used to run the Java environment
The docker-compose.yml file will be used to specify the configurations
The db/init.sh script will create the sailings database automatically

## Run

### Remember to close all connections to ports 8080 and 5432

To run the project you should execute the following commands:

- mvn clean install (This command will execute the tests too)

### Local

- mvn spring-boot:run

### Docker

- docker-compose up

## Run tests

To run only the tests you should execute the following command:

- mvn test

## Usage

You can import the "Assessment - Jorge Diaz.postman_collection.json" file into postman to call the APIs