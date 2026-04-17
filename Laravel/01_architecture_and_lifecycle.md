# Architecture and Lifecycle

Understanding the core architecture of Laravel is essential for building scalable and maintainable applications.

## 1. Request Lifecycle
The entry point for all requests is `public/index.php`.
1.  **Composer Autoloader**: Loads all classes.
2.  **Bootstrap**: Retrieves an instance of the Laravel application (the "Service Container").
3.  **HTTP/Console Kernels**: The request is sent to the appropriate Kernel.
4.  **Service Providers**: The Kernel bootstraps the Service Providers (the most important part).
5.  **Router**: The request is handed to the Router, which directs it to a Controller or a closure.
6.  **Pipeline**: The request passes through Middlewares.
7.  **Response**: The Controller returns a response which travels back through the Middlewares to the user.

## 2. Service Container
The Service Container is a powerful tool for managing class dependencies and performing dependency injection.
- **Binding**: Registering a class or interface in the container.
- **Resolving**: Retrieving a class instance from the container (dependencies are automatically injected).

## 3. Service Providers
The central place of all Laravel application bootstrapping.
- They "provide" services to the container.
- **`register()` method**: Used only to bind things to the service container. (Never register event listeners or routes here).
- **`boot()` method**: Called after all other service providers have been registered. Used for actual bootstrapping (routing, events, etc.).

## 4. Facades
Facades provide a "static" interface to classes that are available in the application's service container.
- **Example**: `Cache::get('key')` is a facade for the `Illuminate\Cache\Repository` class.
- **Why?**: They provide a terse, memorable syntax while maintaining more testability and flexibility than traditional static methods.

## 5. Dependency Injection (DI)
Laravel allows you to type-hint dependencies in the constructors of your controllers, listeners, and middleware. The container will automatically resolve those dependencies.

> [!IMPORTANT]
> **Service Container vs. Service Providers**:
> - The **Container** is like a box where you store and retrieve objects.
> - **Providers** are the instructions on how to put objects into the box.
