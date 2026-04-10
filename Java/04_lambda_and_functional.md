# Lambda Expressions & Functional Interfaces - Interview Notes ⚡

## 1. Introduction
With Java 8, the language moved towards **Functional Programming**. This shift allowed developers to treat functions as first-class citizens, meaning they can be passed as arguments, returned from methods, and stored in variables.

- **Objective**: To support cleaner, more concise, and readable code, especially when working with collections (Streams).

---

## 2. Functional Interfaces
A **Functional Interface** is an interface that contains **exactly one** abstract method. It can have multiple `default` or `static` methods.
- **Annotation**: `@FunctionalInterface` (Optional but recommended to prevent accidental addition of methods).
- **SAM Principle**: Single Abstract Method.

---

## 3. Anonymous Inner Classes (Pre-Java 8)
Before lambdas, we used anonymous inner classes to provide implementation on the fly.
```java
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button Clicked!");
    }
});
```
**The Problem**: Boilerplate code (verbose) and hard to read.

---

## 4. Lambda Expressions
A **Lambda Expression** is an anonymous function (no name, no return type, no modifier). It provides a concise way to implement a functional interface.

**Syntax**: `(parameters) -> { body }`

```java
// Expression Lambda (implicit return)
(a, b) -> a + b;

// Block Lambda (explicit return)
(a, b) -> {
    int sum = a + b;
    return sum;
};
```

---

## 5. Variable Capture
Lambdas can access variables from their enclosing scope. However:
- **Rule**: Local variables accessed by a lambda must be **final** or **effectively final** (assigned only once).
- **Why?** Since lambdas might run in another thread, Java captures the *value* of the variable. If the variable changes later in the main thread, the behavior becomes unpredictable.

---

## 6. Method References
Method references act as a syntactic shorthand for lambdas that only call an existing method.

| Type | Syntax | Lambda Equivalent |
| :--- | :--- | :--- |
| **Static** | `Math::max` | `(x, y) -> Math.max(x, y)` |
| **Instance (Specific)** | `str::toUpperCase` | `() -> str.toUpperCase()` |
| **Arbitrary Instance** | `String::toLowerCase` | `(s) -> s.toLowerCase()` |
| **Constructor** | `ArrayList::new` | `() -> new ArrayList<>()` |

---

## 7. Built-in Functional Interfaces
The `java.util.function` package provides a set of reusable functional interfaces.

| Interface | Method | Description |
| :--- | :--- | :--- |
| **Consumer<T>** | `void accept(T t)` | Takes an input, returns nothing. |
| **Supplier<T>** | `T get()` | Takes nothing, returns an object. |
| **Function<T, R>** | `R apply(T t)` | Takes an input, returns a result. |
| **Predicate<T>** | `boolean test(T t)` | Takes an input, returns a boolean. |

---

## 8. The Consumer Interface
Used when you want to perform an operation on an object without returning anything (Side-effect).
```java
Consumer<String> printer = s -> System.out.println(s);
printer.accept("Hello Functions!");
```

---

## 9. Chaining Consumer
Use `andThen()` to run multiple consumers sequentially on the same input.
```java
Consumer<String> c1 = s -> System.out.println(s.toUpperCase());
Consumer<String> c2 = s -> System.out.println(s.toLowerCase());
c1.andThen(c2).accept("Java"); 
// Output: JAVA \n java
```

---

## 10. The Supplier Interface
Used for **lazy generation** of values. It doesn't take any parameters but returns a result.
```java
Supplier<Double> randomGen = () -> Math.random();
System.out.println(randomGen.get());
```

---

## 11. The Function Interface
Represents a function that transforms an input of type `T` to a result of type `R`.
```java
Function<String, Integer> lengthFunc = s -> s.length();
Integer len = lengthFunc.apply("Antigravity"); // 11
```

---

## 12. Composing & Chaining Functions
Functions can be composed using `andThen()` and `compose()`.

- **andThen**: f before g (`f.andThen(g)` -> `g(f(x))`)
- **compose**: g before f (`f.compose(g)` -> `f(g(x))`)

```java
Function<Integer, Integer> add = i -> i + 1;
Function<Integer, Integer> mult = i -> i * 2;

// Output: (1 + 1) * 2 = 4
System.out.println(add.andThen(mult).apply(1));

// Output: (1 * 2) + 1 = 3
System.out.println(add.compose(mult).apply(1));
```

---

## 13. The Predicate Interface
Used for **filtering** or **conditional checking**.
```java
Predicate<String> isLong = s -> s.length() > 5;
System.out.println(isLong.test("Java")); // false
```

---

## 14. Combining Predicates
Predicates can be combined using boolean logic methods like `and()`, `or()`, and `negate()`.
```java
Predicate<String> hasA = s -> s.contains("a");
Predicate<String> hasB = s -> s.contains("b");

Predicate<String> both = hasA.and(hasB);
System.out.println(both.test("apple and banana")); // true
```

---

## 15. The UnaryOperator & BinaryOperator
Specialized versions of `Function` where all types are the same.
- **UnaryOperator<T>**: Extends `Function<T, T>`.
- **BinaryOperator<T>**: Extends `BiFunction<T, T, T>`.

```java
UnaryOperator<Integer> square = x -> x * x;
BinaryOperator<Integer> add = (x, y) -> x + y;
```

---

## 16. Summary
| Interface | Input | Output | Purpose |
| :--- | :---: | :---: | :--- |
| **Consumer** | 1 | 0 | Do something with input. |
| **Supplier** | 0 | 1 | Generate object. |
| **Function** | 1 | 1 | Transform input. |
| **Predicate**| 1 | Boolean | Filter/Test input. |
| **UnaryOp**  | 1 | 1 (Same) | Transform same type. |
| **BinaryOp** | 2 | 1 (Same) | Combine two same types. |
