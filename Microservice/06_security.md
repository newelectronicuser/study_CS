# Security in Microservices

Securing microservices requires a shift from "Perimeter Security" to "Zero Trust Architecture."

## 1. Authentication and Authorization
Authentication (Whos are you?) and Authorization (What can you do?) must be handled differently in a distributed environment.

- **Distributed Authentication**: Typically handled at the **API Gateway**.
- **JWT (JSON Web Tokens)**: A common way to pass identity between microservices. The gateway authenticates the user and generates a signed JWT that contains user identity and roles.
- **OAuth2 / OpenID Connect**: Standards for delegated authentication and identity.

## 2. API Gateway Security
The gateway acts as the first line of defense.
- **Responsibilities**: SSL termination, IP Whitelisting, Rate limiting (to prevent DDoS), API Key validation, CORS.

## 3. Mutual TLS (mTLS)
In a production environment, you cannot trust the internal network. mTLS ensures that every service-to-service communication is encrypted and that both the client and server verify each other's certificates.

## 4. Secret Management
Never store credentials (DB passwords, API keys) in code or config files. Use a dedicated secret manager like **HashiCorp Vault**, **AWS Secrets Manager**, or **Kubernetes Secrets**.

## 5. Security at the Data Layer
- **Environment Isolation**: Production data should never be accessible from non-prod environments.
- **Encryption at Rest**: Ensuring data is encrypted on disk.

## 6. Access Control Patterns
- **RBAC (Role-Based Access Control)**: Assigning permissions based on user roles (e.g., "Admin", "User").
- **ABAC (Attribute-Based Access Control)**: More granular control based on attributes of the user, resource, and environment.

> [!IMPORTANT]
> **Stateless Auth**: JWTs are stateless, meaning you don't need to query a session database for every request. However,revoking a JWT before it expires is difficult (often requires a blacklist or very short TTLs).
