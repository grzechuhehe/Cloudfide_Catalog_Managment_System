# Product Catalog Management System

## Overview
This is a RESTful API backend for an e-commerce platform that handles products from multiple producers. The system is designed to support products with a highly variable number of attributes (from a few basic ones to hundreds of technical specifications) using the Entity-Attribute-Value (EAV) database pattern.

## Technologies Used
* Java 21
* Spring Boot 3.4.x
* Maven
* H2 Database (In-Memory)
* Liquibase (Database schema management)
* MapStruct & Lombok

## Prerequisites
To run this project, you need to have the following installed on your machine:
* Java Development Kit (JDK) 21 or higher
* Maven 3.8+

## Setup and Running Instructions

1. Clone the repository and navigate to the project directory:
   ```bash
   git clone https://github.com/grzechuhehe/Cloudfide_Catalog_Managment_System
   cd Cloudfide_catalog_managment_system
   ```

2. Build the project and run tests to ensure everything is configured correctly:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`.

## Database Configuration
The application uses an in-memory H2 database. Liquibase automatically creates the database schema and populates it with initial data (a few sample producers) upon application startup.

You can access the H2 database console while the application is running:
* Console URL: `http://localhost:8080/h2-console`
* JDBC URL: `jdbc:h2:mem:catalogdb`
* Username: `sa`
* Password: (leave blank)

## API Endpoints Reference

### Producers
* `GET /api/v1/producers` : List all producers.
* `POST /api/v1/producers` : Create a new producer.

### Products
* `GET /api/v1/products` : List all products. Supports optional query parameters: `producerId` and `search`.
* `GET /api/v1/products/{id}` : Retrieve details of a specific product.
* `POST /api/v1/products` : Create a new product (requires an existing producer ID).
* `PUT /api/v1/products/{id}` : Update an existing product and its attributes.
* `DELETE /api/v1/products/{id}` : Remove a product from the system.


