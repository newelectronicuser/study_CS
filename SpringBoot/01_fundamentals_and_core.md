# Fundamentals and Core Concepts

Spring Boot is an extension of the Spring framework that eliminates the boilerplate configurations required for setting up a Spring application.

## 1. Spring vs. Spring Boot
- **Spring**: A powerful dependency injection framework. Requires heavy XML or Java based configuration.
- **Spring Boot**: An opinionated framework built on top of Spring. It follows the "Convention over Configuration" approach.
    - **Embedded Server**: No need to install Tomcat/Jewel externally.
    - **Starter Dependencies**: Aggregates common dependencies into a single starter (e.g., `spring-boot-starter-web`).
    - **Auto-configuration**: Automatically configures beans based on the libraries present on the classpath.

## 2. Auto-configuration
The heart of Spring Boot. Powered by the `@EnableAutoConfiguration` annotation (included in `@SpringBootApplication`).
- **How it works**: It looks for files named `META-INF/spring.factories` (or `spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` in newer versions) and loads the configuration classes defined there.
- **Conditions**: Uses `@ConditionalOnClass`, `@ConditionalOnMissingBean`, etc., to decide whether a configuration should be applied.

## 3. Spring Boot Starters
Starters are a set of convenient dependency descriptors that you can include in your application.
- **Example**: `spring-boot-starter-data-jpa` includes Hibernate, Spring Data JPA, and the necessary database drivers.

## 4. Spring Boot Actuator
Provides production-ready features to help you monitor and manage your application.
- **Endpoints**: `/health`, `/metrics`, `/info`, `/env`, `/mappings`.
- **Usage**: Used by monitoring tools like Prometheus and Grafana.

## 5. Main Application Class
Annotated with `@SpringBootApplication`, which is a combination of three annotations:
- **`@SpringBootConfiguration`**: Indicates that the class provides Spring Boot application configuration.
- **`@EnableAutoConfiguration`**: Enables Spring Boot's auto-configuration mechanism.
- **`@ComponentScan`**: Enables component scanning on the package where the application is located.

> [!IMPORTANT]
> **Fat JAR**: 
> Spring Boot applications are packaged as a single executable JAR (Fat JAR) that contains all dependencies and the embedded server, making deployment extremely simple.
