# Routing and Middleware

Routing is the process of mapping URLs to specific actions in your application.

## 1. Basic Routing
Routes are typically defined in `routes/web.php` or `routes/api.php`.
```php
Route::get('/greeting', function () {
    return 'Hello World';
});
```

## 2. Route Parameters
Capturing segments of the URI within the route.
```php
Route::get('/user/{id}', function ($id) {
    return 'User '.$id;
});
```

## 3. Route Groups
Allow you to share route attributes, such as middleware or namespaces, across a large number of routes without needing to define those attributes on every single route.
```php
Route::middleware(['auth'])->group(function () {
    Route::get('/profile', function () { /* ... */ });
    Route::get('/settings', function () { /* ... */ });
});
```

## 4. Middleware
Middleware provides a convenient mechanism for filtering HTTP requests entering your application.
- **Global Middleware**: Run during every HTTP request to your application (e.g., `CheckForMaintenanceMode`).
- **Route Middleware**: Assigned to specific routes or groups.
- **Example**: Authentication middleware (`auth`) redirects unauthenticated users to the login page.

## 5. Route Model Binding
Automatically inject model instances directly into your routes.
```php
// In routes/web.php
Route::get('/users/{user}', function (User $user) {
    return $user->email;
});
```
Laravel will automatically find the user with the ID matching the `{user}` segment.

## 6. Rate Limiting
Laravel includes a powerful and customizable rate limiting service that you can use to limit the amount of traffic for a given route or group of routes.

> [!TIP]
> **Named Routes**: 
> Always use named routes (`->name('profile')`). This allows you to generate URLs or redirects without being coupled to the actual URI, making it easier to change URLs later without breaking your application.
