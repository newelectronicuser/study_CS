# Composer and PSR Standards

Modern PHP is heavily standardized through the PHP-FIG (Framework Interoperability Group).

## 1. Composer
Composer is the dependency manager for PHP. It allows you to declare the libraries your project depends on and it will manage (install/update) them for you.
- **`composer.json`**: Describes the dependencies of your project.
- **`composer.lock`**: Stores the exact versions of the dependencies installed. Always commit this file to version control.
- **`vendor/`**: The directory where dependencies are installed.

## 2. PSR (PHP Standards Recommendations)
Standardized naming, formatting, and interfaces that allow different libraries and frameworks to work together.

### Key PSRs
- **PSR-1**: Basic Coding Standard (Class naming, method naming).
- **PSR-4**: Autoloading Standard. Maps namespaces to file paths.
- **PSR-7**: HTTP Message Interfaces. Standardizes how HTTP requests and responses should look.
- **PSR-12**: Extended Coding Style Guide (Replaced PSR-2). Defines how code should be formatted (braces, indentation, etc.).
- **PSR-15**: HTTP Middleware. Standardizes how middleware should be implemented.

## 3. Autoloading (PSR-4)
Instead of manual `require` statements, we use PSR-4 to map namespaces to directories.
```json
{
    "autoload": {
        "psr-4": {
            "App\\": "src/"
        }
    }
}
```
Then, a class `App\Models\User` would be expected at `src/Models/User.php`.

## 4. Modern Ecosystem
Most modern PHP applications use a framework to handle routing, security, and database interaction.
- **Laravel**: Elegant syntax, focus on developer happiness, massive ecosystem.
- **Symfony**: A set of reusable PHP components and a full-stack framework. Used by many other projects (including Magento and Drupal).

## 5. Deployment with Composer
Always use `--no-dev` and `--optimize-autoloader` in production to keep the bundle small and fast.
```bash
composer install --no-dev --optimize-autoloader
```

> [!IMPORTANT]
> **Semantic Versioning (SemVer)**: 
> Composer uses SemVer (`Major.Minor.Patch`). 
> - Major: Breaking changes.
> - Minor: New features (backwards compatible).
> - Patch: Bug fixes (backwards compatible).
