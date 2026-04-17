# REST API and Conventions

Spring Boot simplifies the creation of RESTful web services using the Spring MVC module.

## 1. Important Annotations
- **`@RestController`**: A specialized version of `@Controller` that adds `@ResponseBody` to every method. It tells Spring that the return value should be written directly to the HTTP response body (usually as JSON).
- **`@RequestMapping`**: Used to map web requests to specific handler classes and methods.
- **`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`**: Specialized shortcuts for specific HTTP methods.
- **`@PathVariable`**: Used to extract values from the URI path (e.g., `/users/{id}`).
- **`@RequestParam`**: Used to extract values from query parameters (e.g., `/users?name=John`).
- **`@RequestBody`**: Used to map the body of the HTTP request to a Java object.

## 2. Exception Handling
Handling errors gracefully is critical for a good API.
- **`@ExceptionHandler`**: Used inside a controller to handle specific exceptions thrown by its methods.
- **`@ControllerAdvice`**: A global exception handler. It allows you to consolidate your error handling logic in a single class that applies to all controllers.
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
```

## 3. Bean Validation (JSR 303/380)
Spring Boot integrates with Hibernate Validator to provide automatic data validation.
- **`@Valid`**: Tells Spring to validate the incoming `@RequestBody` or `@ModelAttribute`.
- **Constraint Annotations**: `@NotNull`, `@Min`, `@Max`, `@Email`, `@NotBlank`, `@Size`.

## 4. ResponseEntity
A wrapper for the HTTP response that allows you to specify the Status Code, Headers, and Body.
```java
return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
```

## 5. Jackson Library
The default JSON processor in Spring Boot. It automatically converts Java objects to JSON and vice-versa.
- Use `@JsonProperty` to change a field's name in JSON.
- Use `@JsonIgnore` to exclude a field from the JSON output.

> [!TIP]
> **HATEOAS**: 
> Consider using **Spring HATEOAS** to add hypermedia links to your REST responses, making your API discoverable (Level 3 of the Richardson Maturity Model).
