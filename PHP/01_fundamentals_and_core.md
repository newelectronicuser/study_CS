# Fundamentals and Core Concepts

PHP (Hypertext Preprocessor) is a widely-used open source general-purpose scripting language that is especially suited for web development.

## 1. PHP Execution Model
PHP is typically run on a server as a **FastCGI** process.
- **Client Request**: Browser sends an HTTP request to the web server (Nginx/Apache).
- **Web Server**: Receives the request and forwards it to the **PHP-FPM** (FastCGI Process Manager) master process.
- **PHP-FPM**: The master process assigns the request to a worker process. The worker process parses and executes the PHP script, then returns the HTML/JSON response back to the web server.

## 2. Core Syntax and Data Types
- **Tags**: PHP code starts with `<?php` and ends with `?>` (though the closing tag is omitted in pure PHP files).
- **Data Types**:
    - **Scalar**: `bool`, `int`, `float`, `string`.
    - **Compound**: `array`, `object`, `callable`, `iterable`.
    - **Special**: `resource`, `null`.
- **Strict Typing**: Use `declare(strict_types=1);` at the top of the file to enforce type safety in function parameters and return types.

## 3. Superglobals
Built-in variables that are always available in all scopes.
- **`$_GET`**: Data passed via URL query parameters.
- **`$_POST`**: Data passed via HTTP POST request (body).
- **`$_REQUEST`**: Merges `$_GET`, `$_POST`, and `$_COOKIE`. (Avoid using for security).
- **`$_SERVER`**: Information about the server environment, headers, and paths.
- **`$_SESSION`**: Data stored on the server for a specific user session.
- **`$_COOKIE`**: Data stored in the user's browser.
- **`$_FILES`**: Information about uploaded files.
- **`$_ENV`**: Environment variables.

## 4. `echo` vs `print`
- **`echo`**: Has no return value and can take multiple parameters (though rarely used). It is slightly faster.
- **`print`**: Has a return value of `1`, so it can be used in expressions.

## 5. Include vs Require
- **`include`**: If the file is missing, it skips it and continues with a warning.
- **`require`**: If the file is missing, it throws a fatal error and stops execution.
- **`_once` versions**: Ensure the file is only loaded once per script execution.

> [!IMPORTANT]
> **Static vs Dynamic**: PHP is a dynamically typed language by default, but modern versions (7.x and 8.x) have introduced strong typing features that make it feel more like a statically typed language when used correctly.
