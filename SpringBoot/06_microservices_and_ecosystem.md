# Microservices and Cloud Ecosystem

Spring Boot is the foundation of the modern microservices architecture in the Java ecosystem, primarily through the **Spring Cloud** project.

## 1. Service Discovery (Netflix Eureka)
Allows microservices to find and talk to each other without hard-coding host/ip information.
- **Eureka Server**: The central registry.
- **Eureka Client**: Every service that registers itself and discovers others.

## 2. API Gateway (Spring Cloud Gateway)
The single entry point for all clients.
- Handles routing, filtering, load balancing, and cross-cutting concerns like authentication and rate limiting.

## 3. Distributed Configuration (Spring Cloud Config)
Centralized management of application properties for different environments (Dev, Test, Prod).
- Configuration can be stored in a Git repository and updated without restarting the microservices.

## 4. Resilience (Resilience4j)
In a microservice architecture, failures are inevitable. Resilience4j (successor to Hystrix) provides:
- **Circuit Breaker**: Stops making requests to a failing service to prevent a cascade of failures.
- **Rate Limiter**: Limits the frequency of requests.
- **Retry**: Automatically retries failed requests.

## 5. Direct Service-to-Service Communication
- **Feign Client**: A declarative HTTP client. You only define an interface, and Spring Cloud generates the implementation.
- **RestTemplate / WebClient**: Tools for making manual HTTP calls.

## 6. Distributed Tracing (Spring Cloud Sleuth / Micrometer)
Assigns a unique ID to every request as it passes through different microservices, allowing you to trace the entire path and identify bottlenecks using tools like **Zipkin**.

## 7. Messaging (Spring Cloud Stream)
Allows microservices to communicate asynchronously using message brokers like **RabbitMQ** or **Apache Kafka**.

> [!TIP]
> **Load Balancing**: 
> Spring Cloud uses **LoadBalancer** (formerly Ribbon) to distribute incoming requests across multiple instances of a service registered with Eureka.
