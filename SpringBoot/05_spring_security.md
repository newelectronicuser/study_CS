# Spring Security

Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications.

## 1. Core Architecture
Spring Security uses a chain of **Servlet Filters** to secure your application.
- **Security Filter Chain**: A series of filters that process every incoming request.
- **AuthenticationManager**: The main entry point for authentication. It delegates to providers to verify credentials.
- **UserDetailsService**: An interface used to retrieve a user's authentication and authorization information from a database or other storage.

## 2. Authentication vs. Authorization
- **Authentication**: Determining who the user is (e.g., Login).
- **Authorization**: Determining if a user has permission to access a specific resource (e.g., Roles and Permissions).

## 3. Security context
- **SecurityContextHolder**: Stores the `SecurityContext`, which contains the `Authentication` object of the currently logged-in user.
```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String username = auth.getName();
```

## 4. JWT (JSON Web Tokens) Integration
Since Spring Boot is often used for stateless REST APIs, JWT is the common choice for security.
1. The user logs in and the server sends a JWT.
2. For all subsequent requests, the client sends this JWT in the `Authorization: Bearer <TOKEN>` header.
3. A custom filter in the Spring Security chain intercepts the request, validates the JWT, and sets the user in the `SecurityContext`.

## 5. Method-level security
Enable it with `@EnableMethodSecurity` and use annotations like `@PreAuthorize` or `@PostAuthorize`.
```java
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long id) { /* ... */ }
```

## 6. CSRF (Cross-Site Request Forgery)
Spring Security provides built-in CSRF protection. However, for stateless APIs (using JWT), it is usually **disabled** because there are no cookies involved that can be hijacked.

> [!IMPORTANT]
> **Password Storage**: 
> Never store passwords in plain text. Spring Security provides the `PasswordEncoder` interface. The current industry standard is **BCrypt** or **Argon2**.
