# Object-Oriented PHP

Modern PHP is heavily object-oriented. Most frameworks (Laravel, Symfony) are built entirely on these principles.

## 1. Classes and Objects
- **Visibility**: `public` (accessible anywhere), `protected` (accessible in class and subclasses), `private` (only in class).
- **Constants**: Defined using the `const` keyword. Accessed via `self::CONST_NAME` or `ClassName::CONST_NAME`.
- **Static**: Methods or properties that belong to the class itself, not an instance.

## 2. Abstraction
- **Abstract Classes**: Cannot be instantiated. They serve as a template for subclasses. Can contain both implemented and abstract methods.
- **Interfaces**: Define a contract that a class must follow. All methods in an interface must be public and have no implementation.

## 3. Traits
Traits are a mechanism for code reuse in single-inheritance languages like PHP.
- **Why?**: A class can only extend one parent but can use multiple Traits.
- **Usage**:
```php
trait Loggable {
    public function log(string $msg) { /* ... */ }
}

class User {
    use Loggable;
}
```

## 4. Namespaces
Used to organize code and avoid name collisions between third-party packages.
- **Definition**: `namespace App\Models;`
- **Importing**: `use App\Models\User;`

## 5. Dependency Injection (DI)
The practice of passing an object's dependencies into it rather than creating them inside.
- **Dependency Injection Container (DIC)**: A tool for managing class dependencies and performing dependency injection automatically.

## 6. Magic Methods
Special methods that start with `__` and are called automatically in response to certain events.
- **`__construct()`**: Called when an object is created.
- **`__destruct()`**: Called when an object is destroyed or the script ends.
- **`__get()` / `__set()`**: Called when accessing/writing inaccessible properties.
- **`__call()`**: Called when calling an inaccessible method.
- **`__toString()`**: Called when an object is treated as a string.

> [!TIP]
> **Autoloading**: 
> Instead of manually including dozens of files, modern PHP uses **PSR-4** autoloading (usually managed by Composer) to load classes automatically based on their namespace and file path.
