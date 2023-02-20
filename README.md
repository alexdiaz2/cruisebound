# Cruisebound

Cruisebound - Jorge Backend Engineer Assessment

## Preparation

You must have installed the following tools:

### Local

- [<u>Java 1.8 or later</u>](https://www.oracle.com/java/technologies/downloads/)
- [<u>maven 3.5+</u>](https://maven.apache.org/) installed to build the project
- [<u>PostgreSQL 15</u>](https://www.postgresql.org/download/)

### Docker

- [<u>Docker</u>](https://docs.docker.com/desktop/install/mac-install/)

## Configuration

### Local

Before running the project you should create a new PostgreSQL database.
You should modify the applications.properties file with the parameters you used to create the database.
As you can see I used the sailings database with the "postgres" user and the "toor" password

spring.datasource.url=jdbc:postgresql://localhost:5432/sailings
spring.datasource.username=postgres
spring.datasource.password=toor

### Docker

The Dockerfile will be used to run the Java environment
The docker-compose.yml file will be used to specify the configurations
The db/init.sh script will create the sailings database automatically

## Run

To run the project you should execute the following commands:

- mvn clean install

### Local

- mvn spring-boot:run

### Docker

- docker-compose up

## Usage

You can import the "Cruisebound.postman_collection.json" file into postman to call the "/api/v1/sailings/search" API