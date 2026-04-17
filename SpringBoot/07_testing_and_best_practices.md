# Testing and Best Practices

Testing is an integral part of the Spring Boot philosophy, providing many tools for both unit and integration testing.

## 1. Unit Testing with JUnit 5 and Mockito
The standard for Java testing.
- **JUnit 5**: The testing framework itself (`@Test`, `@BeforeEach`, `@AfterEach`).
- **Mockito**: Use to "mock" dependencies.
    - `@Mock`: Create a mock object.
    - `@InjectMocks`: Inject mocked dependencies into the object being tested.

## 2. Spring Boot Test Annotations
- **`@SpringBootTest`**: Loads the entire application context for integration testing. Use sparingly as it is slow.
- **`@WebMvcTest`**: Focuses only on the web layer (Controllers).
- **`@DataJpaTest`**: Focuses only on the persistence layer (Repositories).
- **`@MockBean`**: Similar to Mockito's `@Mock`, but it replaces a bean in the Spring ApplicationContext with a mock.

## 3. Profiles
Allows you to separate your application configuration for different environments.
- **`application-dev.properties`**, **`application-prod.yml`**.
- Activate a profile using `spring.profiles.active=dev` or as a VM argument `-Dspring.profiles.active=prod`.

## 4. Performance Optimization
- **Caching**: Use `@Cacheable`, `@CachePut`, and `@CacheEvict`. Requires `@EnableCaching`.
- **Async Execution**: Use `@Async` to run methods in a separate thread. Requires `@EnableAsync`.
- **Lombok**: Use `@Data`, `@AllArgsConstructor`, and `@NoArgsConstructor` to reduce Java boilerplate (getters, setters, constructors).

## 5. Dependency Management
- **Maven/Gradle**: Use the **Spring Boot Parent** or the **BOM (Bill of Materials)** to ensure all Spring Boot related libraries are compatible with each other.

## 6. Best Practices Checklist
- [ ] Use constructor injection.
- [ ] Implement global exception handling.
- [ ] Externalize configuration using `@Value` or `@ConfigurationProperties`.
- [ ] Keep controllers lean; move business logic to the service layer.
- [ ] Always implementation versioning for your REST APIs.
- [ ] Use DTOs (Data Transfer Objects) instead of exposing database entities directly to the API.

> [!TIP]
> **Spring Boot DevTools**: 
> Add this dependency during development to enable **LiveReload** and automatic application restart whenever you change your code.
