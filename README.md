# DevAPIX - API Catalog Service

## Overview
The API Catalog Service is a core microservice within the DevAPIX platform responsible for the complete lifecycle management of APIs. It serves as the central repository for all API metadata, documentation, and pricing configurations.

## Key Features
- **API Publishing & Management**: Allows API owners to publish, update, and deprecate their APIs.
- **Swagger/OpenAPI Integration**: Seamlessly imports API specifications from Swagger/OpenAPI formats to automatically populate endpoints.
- **Endpoint Configuration**: Granular control over individual API endpoints, methods, and configurations.
- **Pricing & Plans**: Administration of tiered pricing plans, monetization strategies, and access controls for APIs.
- **Ownership Control**: Strict role-based access control ensuring only authorized API owners can modify their configurations.

## Technology Stack
- **Framework**: Spring Boot 3
- **Database**: PostgreSQL
- **Service Discovery**: Netflix Eureka Client
- **Communication**: Feign Client (for inter-service communication)

## Running Locally
Ensure that PostgreSQL, Eureka Server, and Config Server are running before starting this service.
