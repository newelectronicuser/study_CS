# Security Best Practices

Security must be baked into the application from the start, not added as an afterthought.

## 1. SQL Injection
Attackers inject malicious SQL into your queries to steal or destroy data.
- **Solution**: Never use user input directly in a query. Use **PDO (PHP Data Objects)** with Prepared Statements.
```php
$stmt = $pdo->prepare('SELECT * FROM users WHERE email = :email');
$stmt->execute(['email' => $user_email]);
$user = $stmt->fetch();
```

## 2. Cross-Site Scripting (XSS)
Attackers inject malicious JavaScript into your pages, which then executes in other users' browsers.
- **Solution**: Escape every piece of data being output to HTML.
```php
echo htmlspecialchars($user_input, ENT_QUOTES, 'UTF-8');
```

## 3. Cross-Site Request Forgery (CSRF)
Attackers trick an authenticated user into performing an unwanted action (e.g., changing their password).
- **Solution**: Use CSRF tokens. A unique, secret, and unpredictable token is generated for every user session and must be sent back with every state-changing request (POST, PUT, DELETE).

## 4. Password Hashing
- **Never use MD5 or SHA1**. They are cryptographically broken and fast to crack with rainbows tables.
- **Solution**: Use `password_hash()` and `password_verify()`. They use strong, modern algorithms (like bcrypt or Argon2) and handle salting automatically.
```php
$hash = password_hash($password, PASSWORD_DEFAULT);
// To verify:
if (password_verify($password, $hash)) { /* ... */ }
```

## 5. Input Validation vs. Sanitization
- **Validation**: Checking if the data matches the expected format (e.g., is this a valid email?). Use `filter_var()`.
- **Sanitization**: Cleaning the data to make it safe for a specific context (e.g., removing HTML tags).

## 6. Remote Code Execution (RCE)
Avoid using dangerous functions like `eval()`, `exec()`, `passthru()`, or `shell_exec()` with user-controlled input.

> [!IMPORTANT]
> **Defense in Depth**: 
> Security relies on multiple layers. Use HTTPS, secure cookies (HTTPOnly/Secure), strong password hashing, and prepared statements together for the best protection.
