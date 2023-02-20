# Cruisebound

Cruisebound - Jorge Backend Engineer Assessment

## Preparation

- [<u>Java 1.8 or later</u>](https://www.oracle.com/java/technologies/downloads/)
- [<u>maven 3.5+</u>](https://maven.apache.org/) installed to build the project
- [<u>PostgreSQL 15</u>](https://www.postgresql.org/download/)


## Configuration

Before running the project you should create a new PostgreSQL database.
You should modify the applications.properties file with the parameters you used to create the database.
As you can see I used the sailings database with the "postgres" user and the "toor" password

spring.datasource.url=jdbc:postgresql://localhost:5432/sailings
spring.datasource.username=postgres
spring.datasource.password=toor

## Run

To run the project you should execute the following commands:
1. mvn clean install
2. mvn spring-boot:run

## Usage

You can import the "Cruisebound.postman_collection.json" file into postman to call the "/api/v1/sailings/search" API