# Spring Boot Microservices Project -- Online Shop Application

## 📌 Overview

This project is a **production-style Spring Boot Microservices
application** that demonstrates how to design, implement, and integrate
multiple independent services using **Spring Cloud** and modern backend
engineering practices.

The system models an **online shopping platform** and focuses heavily
on: - Service-to-service communication - Synchronous vs asynchronous
workflows - Centralized configuration & service discovery - Distributed
tracing & centralized logging - Security and observability -
Containerized integration testing

This project closely resembles how microservices are built and
maintained in real enterprise systems.

------------------------------------------------------------------------

## 🏗️ System Architecture

### Microservices

-   **Product Service**
-   **Order Service**
-   **Inventory Service**
-   **Notification Service**

### Infrastructure Components

-   **API Gateway** -- Single entry point for all external requests
-   **Eureka Server** -- Service discovery and registration
-   **Config Server** -- Centralized configuration management
-   **Kafka / RabbitMQ** -- Asynchronous messaging
-   **Zipkin** -- Distributed tracing
-   **ELK Stack** -- Centralized logging
-   **Keycloak** -- Authentication & authorization
-   **Vault** -- Secrets management

------------------------------------------------------------------------

## 🔁 Inter‑Service Communication

### 1️⃣ Synchronous Communication (REST)

Used when an **immediate response is required**.

**Flow** Client → API Gateway → Order Service → Inventory Service

**Implementation** - REST calls using Spring Web / WebClient - Inventory
Service exposes a REST endpoint: - `GET /api/inventory/{skuCode}` -
Order Service invokes Inventory Service before placing an order - Order
is rejected if inventory is not available

**Why Sync?** - Stock availability must be confirmed instantly - Order
placement depends on inventory validation

------------------------------------------------------------------------

### 2️⃣ Asynchronous Communication (Event‑Driven)

Used when **eventual consistency** is sufficient.

**Flow** Order Service → Message Broker → Notification Service

**Implementation** - Order Service publishes an `OrderPlacedEvent` -
Kafka / RabbitMQ acts as message broker - Notification Service consumes
events asynchronously - Email / notification is sent without blocking
order processing

**Why Async?** - Improves performance and scalability - Prevents tight
coupling between services - Failure in Notification Service does not
affect order placement

------------------------------------------------------------------------

## 🌐 API Gateway Communication

### Role of API Gateway

-   Acts as a **single entry point**
-   Routes requests to appropriate services
-   Hides internal service details from clients
-   Central place for security, logging, and routing rules

**Example** - `/api/product/**` → Product Service - `/api/order/**` →
Order Service - `/api/inventory/**` → Inventory Service

**Benefits** - No hard‑coded service URLs on client side - Enables load
balancing and routing logic - Simplifies authentication and
authorization

------------------------------------------------------------------------

## 🔍 Distributed Tracing with Zipkin

### Why Distributed Tracing?

In microservices, a single request may travel across multiple services.
Zipkin helps: - Track request flow end‑to‑end - Identify latency
bottlenecks - Debug failures across services

### Implementation

-   Spring Cloud Sleuth adds Trace ID & Span ID to requests
-   Each service propagates tracing context automatically
-   Traces are sent to Zipkin server
-   Visual trace graph shows:
    -   API Gateway → Order Service → Inventory Service

------------------------------------------------------------------------

## 🧾 Centralized Logging (ELK Stack)

### Logging Flow

-   Each microservice writes structured logs
-   Logs are collected by Logstash
-   Stored in Elasticsearch
-   Visualized using Kibana dashboards

**Benefits** - Single place to analyze logs - Easy debugging across
services - Production‑grade observability

------------------------------------------------------------------------

## 🔐 Security with Keycloak

### Authentication & Authorization

-   OAuth2 / OpenID Connect based security
-   API Gateway integrates with Keycloak
-   Access tokens validated before routing requests
-   Role‑based access control for APIs

------------------------------------------------------------------------

## 🧩 Service‑Level Implementation Details

### Product Service

-   MongoDB‑based persistence
-   REST APIs for product creation and retrieval
-   DTO pattern used to avoid exposing entities
-   Uses Lombok to reduce boilerplate

### Order Service

-   MySQL database with JPA/Hibernate
-   Transactional order creation
-   Generates unique order numbers
-   Communicates with Inventory Service synchronously
-   Publishes events asynchronously

### Inventory Service

-   MySQL database
-   Read‑only transactional queries
-   SKU‑based inventory lookup
-   Optimized for fast availability checks

### Notification Service

-   Stateless service
-   Message‑driven architecture
-   Consumes order events
-   Handles notifications independently

------------------------------------------------------------------------

## 🧪 Testing Strategy

### Integration Testing

-   Testcontainers for real infrastructure dependencies
-   MongoDB containers for Product Service tests
-   Dynamic configuration during test startup
-   MockMvc for REST endpoint validation

**Why Testcontainers?** - Eliminates environment dependency issues -
Ensures production‑like test behavior

------------------------------------------------------------------------

## 🚀 How to Run the Project

### Prerequisites

-   Java 17
-   Docker & Docker Compose
-   Maven
-   MongoDB
-   MySQL
-   Kafka or RabbitMQ

### Startup Order

1.  Start Eureka Server
2.  Start Config Server
3.  Start Kafka / RabbitMQ
4.  Start Zipkin
5.  Start API Gateway
6.  Start microservices:
    -   Product Service → 8080
    -   Order Service → 8081
    -   Inventory Service → 8082

------------------------------------------------------------------------

## 🎯 Key Engineering Concepts Covered

-   Microservices architecture design
-   API Gateway & Service Discovery
-   Synchronous vs asynchronous communication
-   Event‑driven systems
-   Distributed tracing
-   Centralized logging
-   Secure microservices
-   Containerized integration testing

------------------------------------------------------------------------

## 📚 Reference

Based on a full‑length hands‑on Spring Boot Microservices tutorial
demonstrating enterprise‑grade backend development.

## Links
Youtube - https://www.youtube.com/watch?v=mPPhcU7oWDU