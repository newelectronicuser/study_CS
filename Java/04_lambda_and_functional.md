# Lambda Expressions & Functional Interfaces — Interview Notes ⚡

## 1. Introduction
With Java 8, the language moved towards **Functional Programming** — functions became first-class citizens that can be stored in variables, passed as arguments, and returned from methods.

- **Objective**: Cleaner, concise, readable code — especially for Collections and Streams.

```java
// Pre-Java 8
for (Integer n : numbers) System.out.print(n);

// Java 8+
numbers.forEach(System.out::print); // method reference
```

---

## 2. Functional Interface
An interface with **exactly one** abstract method (**SAM — Single Abstract Method**).

```java
@FunctionalInterface
public interface Printer {
    void print(String message);       // single abstract method

    default void info() {             // allowed: default method
        System.out.println("Printer ready.");
    }
}
```

> [!IMPORTANT]
> `@FunctionalInterface` is optional but recommended — the compiler will enforce the SAM rule.

---

## 3. Anonymous Inner Class → Lambda Evolution

```java
// Old: Anonymous inner class (verbose)
Printer p = new Printer() {
    @Override
    public void print(String message) { System.out.println(message); }
};

// New: Lambda (concise)
Printer p = message -> System.out.println(message);

// Even shorter: Method reference
Printer p = System.out::println;
```

---

## 4. Lambda Syntax

```
(parameters) -> expression           // expression lambda — implicit return
(parameters) -> { statements; }      // block lambda — explicit return required
```

```java
// No parameters
Runnable r = () -> System.out.println("Hello");

// One parameter (parentheses optional)
Consumer<String> c = s -> System.out.println(s);

// Multiple parameters
BinaryOperator<Integer> add = (a, b) -> a + b;

// Block body
Function<Integer, Integer> factorial = n -> {
    int result = 1;
    for (int i = 2; i <= n; i++) result *= i;
    return result;
};

// Explicit type (rarely needed — type inference handles it)
Function<Integer, Integer> sq = (Integer n) -> n * n;
```

---

## 5. Variable Capture
Lambdas can capture variables from the enclosing scope.

| Variable type | Mutable inside lambda? | Rule |
| :--- | :---: | :--- |
| Local variable | ❌ | Must be **effectively final** |
| Instance field | ✅ | Accessed via `this` |
| Static field | ✅ | No restriction |

```java
int factor = 3; // effectively final (never reassigned)
Function<Integer, Integer> triple = n -> n * factor; // ✅ OK

// factor = 4; // ❌ would break lambda — not effectively final anymore

// Loop variable capture — must copy to effectively-final local
List<Runnable> tasks = new ArrayList<>();
for (int i = 0; i < 3; i++) {
    final int captured = i;  // copy is effectively final
    tasks.add(() -> System.out.println("Task " + captured));
}
```

---

## 6. Method References — 4 Kinds

| Kind | Syntax | Lambda Equivalent |
| :--- | :--- | :--- |
| **Static method** | `Math::max` | `(x, y) -> Math.max(x, y)` |
| **Instance (specific object)** | `str::toUpperCase` | `() -> str.toUpperCase()` |
| **Instance (arbitrary object)** | `String::toLowerCase` | `s -> s.toLowerCase()` |
| **Constructor** | `ArrayList::new` | `() -> new ArrayList<>()` |

```java
// a) Static
BinaryOperator<Integer> max = Math::max;

// b) Specific instance
String prefix = "Hello";
Supplier<String> greet = prefix::toUpperCase;

// c) Arbitrary instance (works on any String passed as argument)
Function<String, String> lower = String::toLowerCase;
list.forEach(System.out::println); // System.out is the specific instance

// d) Constructor reference
Supplier<List<String>> factory = ArrayList::new;
List<String> list = factory.get();

// e) Instance method with arg (BiFunction)
BiFunction<String, String, Boolean> startsWith = String::startsWith;
startsWith.apply("apple", "app"); // true
```

---

## 7. Built-in Functional Interfaces (`java.util.function`)

| Interface | Abstract Method | Input → Output | Use Case |
| :--- | :--- | :--- | :--- |
| `Consumer<T>` | `void accept(T t)` | T → void | Side effects (print, save) |
| `BiConsumer<T,U>` | `void accept(T t, U u)` | T, U → void | Map entries, 2-arg operations |
| `Supplier<T>` | `T get()` | () → T | Lazy init, factory, default value |
| `Function<T,R>` | `R apply(T t)` | T → R | Transform / map |
| `BiFunction<T,U,R>` | `R apply(T t, U u)` | T, U → R | Two-input transform |
| `Predicate<T>` | `boolean test(T t)` | T → boolean | Filter / validate |
| `BiPredicate<T,U>` | `boolean test(T t, U u)` | T, U → boolean | Two-input check |
| `UnaryOperator<T>` | `T apply(T t)` | T → T | Same-type transform |
| `BinaryOperator<T>` | `T apply(T t1, T t2)` | T, T → T | Same-type combine |

---

## 8. `Consumer<T>` — void accept(T t)

```java
Consumer<String> print  = s -> System.out.println("Print:  " + s);
Consumer<String> upper  = s -> System.out.println("Upper:  " + s.toUpperCase());

// Chaining — andThen() — all consumers operate on the SAME input
Consumer<String> both = print.andThen(upper);
both.accept("Java");
// Print:  Java
// Upper:  JAVA

// Practical: List.forEach uses Consumer
list.forEach(System.out::println);
```

---

## 9. `BiConsumer<T,U>` — void accept(T t, U u)

```java
BiConsumer<String, Integer> entry = (k, v) -> System.out.println(k + " → " + v);
entry.accept("Alice", 95);

// Map.forEach uses BiConsumer
scores.forEach((k, v) -> System.out.println(k + ": " + v));
```

---

## 10. `Supplier<T>` — T get()

```java
Supplier<Double> random = Math::random;       // no args, returns value
Supplier<List<String>> factory = ArrayList::new; // lazy construction

// Key pattern: Optional.orElseGet — avoids creating object if not needed
String val = optional.orElseGet(() -> "computed default");
```

> [!TIP]
> Use `Supplier` for **lazy evaluation** — the object/value is created only when `get()` is called.

---

## 11. `Function<T,R>` — R apply(T t)

```java
Function<String, Integer> length = String::length;
length.apply("hello"); // 5

// andThen: f.andThen(g) → g(f(x))
Function<Integer, Integer> add1   = n -> n + 1;
Function<Integer, Integer> times3 = n -> n * 3;
add1.andThen(times3).apply(1); // (1+1)*3 = 6

// compose: f.compose(g) → f(g(x))
add1.compose(times3).apply(1); // (1*3)+1 = 4

// identity() — no-op placeholder
Function<String, String> id = Function.identity(); // s -> s

// Type-changing chain
Function<String, Boolean> isEvenStr =
    ((Function<String, Integer>) Integer::parseInt)
        .andThen(n -> n % 2 == 0);
isEvenStr.apply("42"); // true
```

---

## 12. `BiFunction<T,U,R>` — R apply(T t, U u)

```java
BiFunction<String, Integer, String> repeat = (s, n) -> s.repeat(n);
repeat.apply("ha", 3); // hahaha

// andThen chains a Function after the BiFunction
BiFunction<Integer, Integer, String> multToStr =
    ((BiFunction<Integer, Integer, Integer>) (a, b) -> a * b)
        .andThen(n -> "Result=" + n);
multToStr.apply(4, 5); // Result=20
```

---

## 13. `Predicate<T>` — boolean test(T t)

```java
Predicate<String> isLong   = s -> s.length() > 5;
Predicate<String> hasUpper = s -> s.chars().anyMatch(Character::isUpperCase);

// Logical combinations
isLong.and(hasUpper);   // both must be true
isLong.or(hasUpper);    // at least one must be true
isLong.negate();         // logical NOT

// Predicate.not() — Java 11+, perfect with method references
list.stream().filter(Predicate.not(String::isBlank)).forEach(System.out::println);

// Practical: filter with composed predicates
Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<Integer> gt5    = n -> n > 5;
nums.stream().filter(isEven.and(gt5)).forEach(System.out::print); // 6 8 10
```

---

## 14. `BiPredicate<T,U>` — boolean test(T t, U u)

```java
BiPredicate<String, Integer> longerThan = (s, n) -> s.length() > n;
longerThan.test("hello", 3); // true

BiPredicate<String, String> startsWith = String::startsWith;
```

---

## 15. `UnaryOperator<T>` — extends `Function<T,T>`

```java
UnaryOperator<Integer> square    = n -> n * n;
UnaryOperator<Integer> increment = n -> n + 1;

increment.andThen(square).apply(1); // (1+1)^2 = 4
increment.compose(square).apply(1); // (1^2)+1 = 2

UnaryOperator<String> shout = s -> s.toUpperCase() + "!";

// Practical: List.replaceAll uses UnaryOperator
list.replaceAll(String::toUpperCase);
```

---

## 16. `BinaryOperator<T>` — extends `BiFunction<T,T,T>`

```java
BinaryOperator<Integer> add = (a, b) -> a + b;
add.apply(3, 4); // 7

// minBy / maxBy static factories
BinaryOperator<Integer> min = BinaryOperator.minBy(Comparator.naturalOrder());
BinaryOperator<Integer> max = BinaryOperator.maxBy(Comparator.naturalOrder());

// Practical: Stream.reduce uses BinaryOperator
int sum     = nums.stream().reduce(0, Integer::sum);
int product = nums.stream().reduce(1, (a, b) -> a * b);
Optional<Integer> opt = nums.stream().reduce((a, b) -> a * b); // no identity → Optional
```

---

## 17. Primitive Specialisations — Avoid Boxing Overhead

| Interface | Signature | Purpose |
| :--- | :--- | :--- |
| `IntFunction<R>` | `R apply(int value)` | int → R |
| `ToIntFunction<T>` | `int applyAsInt(T t)` | T → int |
| `IntUnaryOperator` | `int applyAsInt(int n)` | int → int |
| `IntBinaryOperator` | `int applyAsInt(int a, int b)` | int, int → int |
| `IntPredicate` | `boolean test(int n)` | int → boolean |
| `IntConsumer` | `void accept(int n)` | int → void |
| `IntSupplier` | `int getAsInt()` | () → int |

```java
ToIntFunction<String>  len      = String::length;   // no boxing
IntUnaryOperator       doubleIt = n -> n * 2;
IntBinaryOperator      addInts  = (a, b) -> a + b;
IntPredicate           isEven   = n -> n % 2 == 0;
```

---

## 18. Function Composition Pipeline

```java
// Each transformation step is a reusable function
UnaryOperator<String> trim      = String::trim;
UnaryOperator<String> toLower   = String::toLowerCase;
UnaryOperator<String> addPrefix = s -> "user_" + s;
Predicate<String>     nonEmpty  = s -> !s.isEmpty();

// Build a sanitize pipeline
Function<String, String> sanitize = trim.andThen(toLower).andThen(addPrefix);

for (String input : rawInputs) {
    String trimmed = trim.apply(input);
    if (nonEmpty.test(trimmed)) System.out.println(sanitize.apply(input));
}

// String formatting pipeline
Function<String, String> format =
    ((Function<String, String>) String::trim)
        .andThen(String::toUpperCase)
        .andThen(s -> "\"" + s + "\"");
format.apply("  hello  "); // "HELLO"
```

---

## 19. Higher-order Functions — Returning Lambdas

```java
// Method that returns a Predicate (parameterised behaviour)
static Predicate<Integer> greaterThan(int threshold) {
    return n -> n > threshold; // threshold is captured (effectively final)
}

Predicate<Integer> gt5  = greaterThan(5);
Predicate<Integer> gt10 = greaterThan(10);

// Method that returns a multiplier Function
static Function<Integer, Integer> multiplierOf(int factor) {
    return n -> n * factor;
}

Function<Integer, Integer> triple     = multiplierOf(3);
Function<Integer, Integer> quadruple  = multiplierOf(4);
Function<Integer, Integer> tripleThenQuadruple = triple.andThen(quadruple);
tripleThenQuadruple.apply(2); // (2*3)*4 = 24
```

---

## 20. Passing Lambdas as Parameters

```java
// Utility accepts any Consumer → caller decides behaviour
static <T> void processAll(List<T> list, Consumer<T> action) {
    for (T item : list) action.accept(item);
}
processAll(names, s -> System.out.println("Hello, " + s));

// Utility accepts any Predicate → caller decides filter rule
static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
    List<T> result = new ArrayList<>();
    for (T item : list) if (predicate.test(item)) result.add(item);
    return result;
}
filter(nums, n -> n % 2 == 0); // evens
filter(nums, n -> n > 5);      // > 5

// Utility accepts any Function → caller decides transformation
static <T, R> List<R> transform(List<T> list, Function<T, R> mapper) {
    List<R> result = new ArrayList<>();
    for (T item : list) result.add(mapper.apply(item));
    return result;
}
transform(nums, n -> n * 2);   // doubled
transform(nums, n -> n * n);   // squared
```

---

## 21. Summary Table

| Interface | Input | Output | Key Methods | Common Use |
| :--- | :---: | :---: | :--- | :--- |
| `Consumer<T>` | 1 | void | `accept`, `andThen` | `forEach`, print, save |
| `BiConsumer<T,U>` | 2 | void | `accept`, `andThen` | `Map.forEach` |
| `Supplier<T>` | 0 | 1 | `get` | Lazy init, `orElseGet` |
| `Function<T,R>` | 1 | 1 | `apply`, `andThen`, `compose` | `map` in streams |
| `BiFunction<T,U,R>` | 2 | 1 | `apply`, `andThen` | Two-arg transform |
| `Predicate<T>` | 1 | boolean | `test`, `and`, `or`, `negate` | `filter` in streams |
| `BiPredicate<T,U>` | 2 | boolean | `test`, `and`, `or` | Two-arg condition |
| `UnaryOperator<T>` | 1 | 1 (same) | `apply`, `andThen` | `replaceAll` |
| `BinaryOperator<T>` | 2 | 1 (same) | `apply` | `reduce` |

> [!IMPORTANT]
> **Key Interview Rules**:
> 1. Local variables captured by a lambda must be **effectively final**.
> 2. `andThen` = execute caller first, then argument: `f.andThen(g)` → `g(f(x))`.
> 3. `compose` = execute argument first, then caller: `f.compose(g)` → `f(g(x))`.
> 4. Use primitive specialisations (`IntFunction`, `ToIntFunction`, etc.) to avoid boxing overhead.
> 5. `Predicate.not(method::ref)` is the Java 11+ idiom for negated method reference filters.
> 6. A lambda is just syntactic sugar for an anonymous class implementing a `@FunctionalInterface`.
