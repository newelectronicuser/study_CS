# Security and Authentication

Laravel's security features are robust and integrated directly into the core framework.

## 1. Authentication
Laravel makes implementing authentication very simple. In fact, most of the work is already done for you.
- **Starter Kits**: Breeze (simple) and Jetstream (advanced) provide a complete starting point for auth.
- **Sanctum**: Provides a featherweight authentication system for SPAs (Single Page Applications) and mobile applications. It uses cookies for SPAs and tokens for mobile.
- **Passport**: A full OAuth2 server implementation for your application. Use this if you need to support third-party clients.

## 2. Authorization
Authorization is separate from authentication. It answers the question: "Is this user allowed to perform this action?".
- **Gates**: Simple, closure-based authorization. Best for small actions.
- **Policies**: Classes that group authorization logic around a particular model (e.g., `PostPolicy` handles who can view, update, or delete a `Post`).

## 3. Protecting Against Vulnerabilities
- **SQL Injection**: Eloquent uses PDO prepared statements, making it immune to injection.
- **Cross-Site Scripting (XSS)**: Blade's `{{ $var }}` syntax automatically escapes output to prevent script injection.
- **CSRF**: Laravel includes middleware to automatically verify CSRF tokens for all state-changing requests.

## 4. Encryption and Hashing
- **Hashing**: Laravel uses `Argon2` or `bcrypt` for password hashing via the `Hash` facade.
- **Encryption**: Laravel's encrypter uses **OpenSSL** to provide AES-256 and AES-128 encryption.

## 5. Middleware Auth
The `auth` middleware can be used to only allow authenticated users to access a given route.

> [!IMPORTANT]
> **Authentication vs. Authorization**: 
> - **Authentication** is about identity (Who are you?).
> - **Authorization** is about permissions (What are you allowed to do?).
