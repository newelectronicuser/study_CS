# Request and Session Management

Handling state and user interaction is at the core of web development in PHP.

## 1. Handling HTTP Requests
- **GET**: Data is part of the URL. Use for searching, sorting, and non-sensitive data.
- **POST**: Data is part of the request body. Use for creating/updating data and sensitive information (passwords).
- **Headers**: Use `header()` to send raw HTTP headers (e.g., `header('Location: /login.php');`). Must be called before any output is sent.

## 2. Cookies
Data stored on the client side (browser).
- **Setting**: `setcookie('name', 'value', time() + 3600, '/');`
- **Security**:
    - `HTTPOnly`: Prevents JavaScript from accessing the cookie (mitigates XSS).
    - `Secure`: Ensures cookie is sent only over HTTPS.
    - `SameSite`: Prevents CSRF by controlling when cookies are sent on cross-site requests.

## 3. Sessions
Data stored on the server side, identified by a session ID in a cookie.
- **Starting**: `session_start();` must be called at the beginning of the script.
- **Usage**: Data is stored in the `$_SESSION` superglobal.
- **Destruction**: `session_destroy();` clears the session data on the server.

## 4. Output Buffering
Allows you to store the output of a script in memory before sending it to the browser.
- **`ob_start()`**: Starts buffering.
- **`ob_get_clean()`**: Returns the buffer content and stops buffering.
- **Why?**: Useful for sending headers later in the script or generating static HTML files.

## 5. Middleware Pattern
Commonly used in modern frameworks. A layer that wraps the request/reponse cycle.
- **Example**: An authentication middleware checks if a user is logged in before allowing them to access a route.

> [!CAUTION]
> **Session Fixation**: 
> Always call `session_regenerate_id(true);` after a user logs in to prevent attackers from using a pre-determined session ID to hijack the user's session.
