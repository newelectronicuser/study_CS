# Modern PHP (7.x and 8.x)

PHP has undergone a massive transformation since version 7.0, becoming significantly faster and more typed.

## 1. PHP 7.x Highlights
- **Return Type Declarations**: Specify the type of value a function returns.
- **Null Coalescing Operator (`??`)**: Syntactic sugar for `isset($a) ? $a : $b`.
- **Spaceship Operator (`<=>`)**: Used for comparison (returns -1, 0, or 1).
- **Throwable Interface**: Improved error handling as most errors now implement `Throwable`.

## 2. PHP 8.0 Highlights
- **JIT (Just-In-Time) Compiler**: Improves performance for CPU-intensive tasks.
- **Match Expression**: A more powerful version of `switch` that returns a value and uses strict comparison.
- **Named Arguments**: Pass arguments to a function by name rather than position.
- **Attributes**: Native metadata (Annotations) in the code.
- **Constructor Property Promotion**: Declare and initialize class properties directly in the constructor.
```php
public function __construct(
    public string $name,
    private int $age,
) {}
```
- **Nullsafe Operator (`?->`)**: Call methods or access properties on potentially null objects without an error.

## 3. PHP 8.1 & 8.2 Highlights
- **Enums**: Native support for Enumerations.
- **Readonly Properties**: Properties that can only be initialized once.
- **Readonly Classes**: All properties in the class are automatically readonly.
- **Intersection Types**: Ensuring a variable implements multiple interfaces (e.g., `Iterator & Countable`).
- **Disjoint Normal Form (DNF) Types**: Combining union and intersection types.

## 4. Union Types (PHP 8.0)
Allows a variable to have more than one possible type.
```php
public function process(int|float $number): void { /* ... */ }
```

> [!IMPORTANT]
> **Performance Improvements**:
> PHP 7.0 was twice as fast as PHP 5.6. PHP 8.x continues this trend with the JIT compiler and internal engine optimizations. Always prefer the latest stable version for performance and security.
