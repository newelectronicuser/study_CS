# Controllers and Validation

Controllers group related request handling logic into a single class. Validation ensures that the incoming data is safe and correct.

## 1. Controllers
- **Resource Controllers**: Automatically register routes for all standard CRUD operations.
```bash
php artisan make:controller PhotoController --resource
```
- **Single Action Controllers**: Controllers that only handle one action. Use the `__invoke` method.

## 2. Validation
Laravel provides several ways to validate your application's incoming data.
- **Manual Validation**: Using the `validate` method on the `Request` object inside the controller.
- **Form Requests**: Dedicated classes that contain validation and authorization logic. This keeps your controllers clean.
```bash
php artisan make:request StoreUserRequest
```

## 3. Dependency Injection in Controllers
You may type-hint any dependencies your controller may need in its constructor. They will automatically be resolved and injected into the controller instance.

## 4. API Resources
When building an API, you may need a transformation layer that sits between your Eloquent models and the JSON responses that are actually returned to your application's users.
```php
return new UserResource($user);
```

## 5. Blade Templating
Blade is the powerful templating engine included with Laravel.
- **Components**: Reusable blocks of HTML/logic.
- **Directives**: Syntactic sugar for PHP control structures (`@if`, `@foreach`, `@auth`).
- **Inheritance**: Using `@extends` and `@section` to define a layout and fill it in.

## 6. CSRF Protection
Laravel automatically generates a CSRF "token" for each active user session managed by the application. This token is used to verify that the authenticated user is the person actually making the requests to the application.

> [!TIP]
> **Always use Form Requests** for complex validation logic. It makes your code more testable and reusable, and keeps your controllers focused on orchestration rather than data checking.
