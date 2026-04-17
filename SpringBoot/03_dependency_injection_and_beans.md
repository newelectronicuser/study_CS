# Dependency Injection and Beans

Dependency Injection (DI) and Inversion of Control (IoC) are the core pillars of the Spring Framework.

## 1. IoC (Inversion of Control) Container
The IoC Container is responsible for instantiating, configuring, and assembling the objects (Beans). 
- In Spring, the `ApplicationContext` interface represents the IoC container.

## 2. Dependency Injection (DI)
The process where objects define their dependencies only through constructor arguments or setter methods. The container then "injects" those dependencies when it creates the bean.
- **Constructor Injection**: Recommended. Ensures mandatory dependencies are not null and allows for immutability (final fields).
- **Field Injection (`@Autowired` on field)**: Easy to write but hard to test (requires reflection) and doesn't allow for final fields.
- **Setter Injection**: Used for optional dependencies.

## 3. Spring Beans
A bean is an object that is managed by the Spring IoC container.
- **@Component**: Generic stereotype for any Spring-managed component.
- **@Service**: Specialization for service-layer classes.
- **@Repository**: Specialization for data-access classes (includes automatic exception translation).
- **@Controller / @RestController**: Specialization for web-layer classes.

## 4. Bean Scopes
Defines the lifecycle and visibility of a bean.
1.  **Singleton (Default)**: Only one instance is created per IoC container.
2.  **Prototype**: A new instance is created every time the bean is requested.
3.  **Request**: One instance per HTTP request (Web applications only).
4.  **Session**: One instance per HTTP session (Web applications only).
5.  **Application**: One instance per ServletContext (Web applications only).

## 5. Bean Lifecycle
1.  **Instantiation**: The container creates the bean.
2.  **Populate Properties**: Dependencies are injected.
3.  **Initialization**: Custom logic runs in `@PostConstruct` or `afterPropertiesSet()`.
4.  **Ready**: Bean is ready for use.
5.  **Destruction**: Custom logic runs in `@PreDestroy` or `destroy()` before the application shuts down.

## 6. Resolving Ambiguity
If multiple beans of the same type exist:
- **`@Primary`**: Tells Spring to prioritize this bean.
- **`@Qualifier("name")`**: Used at the injection point to specify the exact bean name to use.

> [!IMPORTANT]
> **Constructor Injection is Best**: 
> "Field injection is evil." Constructor injection makes it impossible to create an object in an invalid state and simplifies unit testing because you can just pass mocks through the constructor.
