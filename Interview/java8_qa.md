# Java 8 — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Lambda Expressions

**Q1. What is a lambda expression in Java 8? What problem does it solve?**

**A:** A lambda expression is an anonymous function (a block of code with parameters and a body) that can be treated as a first-class citizen in Java. It allows passing behavior directly to methods as arguments without wrapping it in an anonymous inner class.
*   *Problem Solved:* Eliminates the verbose boilerplate of anonymous inner classes when implementing single-method interfaces (functional interfaces), enabling functional programming patterns.

---

**Q2. What is a functional interface? Name five built-in functional interfaces from `java.util.function`.**

**A:** A functional interface is an interface that contains exactly **one abstract method**. It can have multiple `default` or `static` methods. They are usually annotated with `@FunctionalInterface` to enforce this rule at compile time.
*   *Five built-in functional interfaces:*
    1.  `Predicate<T>`
    2.  `Function<T, R>`
    3.  `Consumer<T>`
    4.  `Supplier<T>`
    5.  `BiFunction<T, U, R>`

---

**Q3. What is the difference between `Function<T,R>`, `Consumer<T>`, `Supplier<T>`, and `Predicate<T>`?**

**A:**
*   `Function<T, R>`: Takes an argument of type T and returns a result of type R. Abstract method: `R apply(T t)`.
*   `Consumer<T>`: Takes an argument of type T and returns no result (void). Used for side-effects. Abstract method: `void accept(T t)`.
*   `Supplier<T>`: Takes no arguments and returns a result of type T. Used for lazy generation. Abstract method: `T get()`.
*   `Predicate<T>`: Takes an argument of type T and returns a boolean. Used for filtering. Abstract method: `boolean test(T t)`.

---

**Q4. How do you compose two `Predicate`s using `and()`, `or()`, and `negate()`?**

**A:**
```java
Predicate<String> startsWithA = s -> s.startsWith("A");
Predicate<String> isLongerThan5 = s -> s.length() > 5;

// Composing Predicates
Predicate<String> compoundAnd = startsWithA.and(isLongerThan5);
Predicate<String> compoundOr = startsWithA.or(isLongerThan5);
Predicate<String> notStartsWithA = startsWithA.negate();
```

---

**Q5. What is a method reference? What are the four types of method references?**

**A:** A method reference is a shorthand syntax of a lambda expression that calls an existing method. Syntax: `ClassName::methodName`.
*   *Four types:*
    1.  **Static method reference:** `Math::abs` (equivalent to `x -> Math.abs(x)`)
    2.  **Instance method of a particular object:** `str::startsWith` (equivalent to `x -> str.startsWith(x)`)
    3.  **Instance method of an arbitrary object of a particular type:** `String::toUpperCase` (equivalent to `s -> s.toUpperCase()`)
    4.  **Constructor reference:** `ArrayList::new` (equivalent to `() -> new ArrayList()`)

---

**Q6. How do you capture variables from the enclosing scope in lambdas? What restriction applies?**

**A:** A lambda expression can capture local variables from its enclosing scope.
*   *Restriction:* The captured local variable must be **final** or **effectively final** (meaning its value is never modified after initialization). If you try to reassign the variable inside or outside the lambda, compilation fails with `local variables referenced from a lambda expression must be final or effectively final`. This prevents race conditions and thread safety issues in concurrent executions.

---

## Stream API

**Q7. What is a Stream in Java 8? How is it different from a Collection?**

**A:** A Stream is a sequence of elements supporting sequential and parallel aggregate operations.
*   *Differences:*
    *   **Data Storage:** A Collection is an in-memory data structure that holds all elements; a Stream does not store data, it operates on a data source (like a collection or array).
    *   **Mutability:** Collections are mutable (elements can be added/removed); Streams are immutable (processing a stream returns a new stream, leaving the source unmodified).
    *   **Execution:** Streams are lazy; computation only happens when a terminal operation is called. Collections are eagerly populated.
    *   **Consumption:** Streams can only be traversed once.

---

**Q8. What is the difference between intermediate and terminal operations?**

**A:**
*   **Intermediate Operations:** Return a new Stream. They are **lazy** and do not execute until a terminal operation is invoked. They are used to build a pipeline (e.g. `filter`, `map`, `sorted`).
*   **Terminal Operations:** Produce a non-stream result (like a List, a single value, or void) and close the stream. Calling a terminal operation triggers the execution of the entire pipeline (e.g. `collect`, `forEach`, `reduce`, `count`).

---

**Q9. Name five commonly used intermediate operations and three terminal operations.**

**A:**
*   *Intermediate Operations:* `filter()`, `map()`, `flatMap()`, `distinct()`, `sorted()`, `limit()`.
*   *Terminal Operations:* `collect()`, `forEach()`, `reduce()`, `count()`, `anyMatch()`.

---

**Q10. What is the difference between `map()` and `flatMap()`?**

**A:**
*   `map(Function<T, R>)`: Applies a function to each element of a stream and returns a stream of the transformed elements (one-to-one mapping). Returns a `Stream<R>`.
*   `flatMap(Function<T, Stream<R>>)`: Applies a function to each element that returns a stream, and then merges (flattens) all the individual streams into a single, unified stream (one-to-many mapping). Returns a `Stream<R>`. Used for flattening nested collections (e.g., converting `Stream<List<String>>` to `Stream<String>`).

---

**Q11. What is `reduce()` and how does it work?**

**A:** `reduce()` is a terminal operation that combines all stream elements into a single result by repeatedly applying an associative accumulation function.

```java
// Example: Summing numbers
int sum = List.of(1, 2, 3, 4).stream()
              .reduce(0, (accumulator, element) -> accumulator + element);
```
It takes an identity (initial value) and an accumulator function.

---

**Q12. What is `collect()` and how do `Collectors.groupingBy()` and `Collectors.partitioningBy()` work?**

**A:** `collect()` is a terminal operation that transforms stream elements into a different container (like a List, Set, or Map).
*   `Collectors.groupingBy(classifier)`: Groups elements into a `Map<K, List<T>>` based on a classification function.
*   `Collectors.partitioningBy(predicate)`: A special case of `groupingBy` that partitions elements into a `Map<Boolean, List<T>>` based on a predicate (true/false).

---

**Q13. What is the difference between `findFirst()` and `findAny()`?**

**A:**
*   `findFirst()`: Returns the very first element in the stream. Deterministic (always returns the same element for ordered streams).
*   `findAny()`: Returns any element from the stream. Non-deterministic, which allows it to be much faster in parallel streams because it doesn't wait to check if an element is the absolute first in the sequence.

---

**Q14. How does `filter()` + `map()` compare to a traditional for-loop in terms of performance?**

**A:** For simple operations, a traditional for-loop is slightly faster due to the overhead of creating Stream objects and lambdas. However, the performance gap is negligible for most business applications. Streams compile down to efficient loops, and for large datasets, parallel streams can easily outperform single-threaded loops. The primary benefit of streams is readability and maintainability.

---

**Q15. What is a parallel stream? When should you use it and what are the risks?**

**A:** A parallel stream splits the stream elements into multiple chunks and processes them concurrently using the common `ForkJoinPool`.
*   *When to use:* CPU-intensive computations on very large collections where ordering does not matter.
*   *Risks:* Sharing state causes race conditions; blocking operations (like thread sleep or I/O) tie up the common ForkJoinPool, degrading application-wide performance; high overhead of splitting/merging datasets (can make operations slower for small collections).

---

**Q16. What is `Stream.of()` vs `Arrays.stream()` vs `Collection.stream()`?**

**A:**
*   `Stream.of(T... values)`: Creates a stream from a comma-separated list of values.
*   `Arrays.stream(T[] array)`: Creates a stream from an array.
*   `Collection.stream()`: Creates a stream from a collection class (like List, Set).

---

**Q17. How do you sort a stream using a custom comparator?**

**A:** Use the `sorted(Comparator)` intermediate operation:
```java
List<Employee> sorted = employees.stream()
    .sorted(Comparator.comparing(Employee::getSalary).reversed())
    .collect(Collectors.toList());
```

---

**Q18. What is `Collectors.toUnmodifiableList()` vs `Collectors.toList()`?**

**A:**
*   `Collectors.toList()`: Returns a mutable `ArrayList` (implementation is not strictly guaranteed, but usually mutable).
*   `Collectors.toUnmodifiableList()` (Java 10+): Returns an immutable list. Attempting to modify this list (e.g. `add()`) throws `UnsupportedOperationException`.

---

**Q19. How would you find the second highest salary from a list of employees using streams?**

**A:**
```java
Optional<Double> secondHighest = employees.stream()
    .map(Employee::getSalary)
    .distinct()
    .sorted(Comparator.reverseOrder())
    .skip(1)
    .findFirst();
```

---

**Q20. How would you group employees by department and count them using streams?**

**A:**
```java
Map<String, Long> deptCounts = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
```

---

## Optional

**Q21. What is `Optional<T>` and why was it introduced?**

**A:** `Optional<T>` is a container object that may or may not contain a non-null value.
*   *Why introduced:* To provide a type-level representation of a potentially absent value, reducing the occurrence of `NullPointerException`s and preventing developers from having to return `null` from methods.

---

**Q22. What is the difference between `Optional.of()`, `Optional.ofNullable()`, and `Optional.empty()`?**

**A:**
*   `Optional.of(T value)`: Creates an Optional with a non-null value. If the value passed is null, it immediately throws `NullPointerException`.
*   `Optional.ofNullable(T value)`: Creates an Optional. If the value is null, it returns an empty Optional.
*   `Optional.empty()`: Returns an empty Optional instance.

---

**Q23. What is `orElse()` vs `orElseGet()` vs `orElseThrow()`?**

**A:**
*   `orElse(T other)`: Returns the value if present, otherwise returns `other`. **The `other` object is evaluated/constructed eagerly**, even if the Optional contains a value.
*   `orElseGet(Supplier<? extends T> other)`: Returns the value if present, otherwise evaluates the Supplier to construct the fallback. **Evaluated lazily** (more efficient if fallback construction is expensive).
*   `orElseThrow()`: Returns the value if present, otherwise throws a `NoSuchElementException` (or a custom exception if passed a supplier).

---

**Q24. How do you chain operations on an `Optional` using `map()` and `flatMap()`?**

**A:**
*   Use `map()` when the mapper function returns a raw object.
*   Use `flatMap()` when the mapper function itself returns an `Optional`.

```java
Optional<String> street = Optional.ofNullable(user)
    .flatMap(User::getAddress) // returns Optional<Address>
    .map(Address::getStreet);   // returns String
```

---

**Q25. What is the anti-pattern of using `isPresent()` + `get()`?**

**A:** Using `if (opt.isPresent()) { String val = opt.get(); }` is an anti-pattern because it replicates the exact same check-and-fetch logic of traditional `if (val != null)` checks, defeating the purpose of Optional. Instead, you should use functional fluent APIs like `ifPresent(Consumer)`, `map()`, or `orElse()`.

---

## Date & Time API

**Q26. What problems did `java.util.Date` and `Calendar` have? How does the new Date/Time API solve them?**

**A:**
*   *Problems:* `java.util.Date` was mutable (not thread-safe); index numbering was confusing (January was month 0); class design was poor; lacked timezone handling.
*   *Solution:* The new `java.time` API (JSR-310) is immutable (thread-safe), clear to read, uses standard calendars, and separates dates, times, durations, and timezones cleanly.

---

**Q27. What is the difference between `LocalDate`, `LocalTime`, `LocalDateTime`, and `ZonedDateTime`?**

**A:**
*   `LocalDate`: Stores date only (yyyy-MM-dd) with no timezone.
*   `LocalTime`: Stores time only (HH:mm:ss.nanos) with no timezone.
*   `LocalDateTime`: Stores date and time with no timezone.
*   `ZonedDateTime`: Stores date, time, and a specific timezone (e.g. `ZoneId` like "UTC" or "America/New_York").

---

**Q28. How do you parse and format dates using `DateTimeFormatter`?**

**A:** `DateTimeFormatter` is thread-safe and replaced the old `SimpleDateFormat`.
```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
// Format
String formatted = LocalDate.now().format(formatter);
// Parse
LocalDate parsed = LocalDate.parse("2026-07-02", formatter);
```

---

**Q29. What is a `Period` vs a `Duration`?**

**A:**
*   `Period`: Measures time in **date units** (years, months, days). e.g., `Period.between(startLocalDate, endLocalDate)`.
*   `Duration`: Measures time in **time units** (seconds, nanoseconds). e.g., `Duration.between(startTime, endTime)`.

---

**Q30. How do you convert between the old `Date` API and the new API?**

**A:** Use the intermediate `Instant` class:
```java
// Date to LocalDateTime
Date date = new Date();
LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

// LocalDateTime to Date
Date backToDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
```

---

## Default & Static Methods in Interfaces

**Q31. What is a default method in an interface? Why was it introduced?**

**A:** A default method is a method inside an interface declared with the `default` keyword that provides a default implementation.
*   *Why introduced:* To allow adding new methods to interfaces without breaking existing classes that implement the interface (backward compatibility). e.g. adding `stream()` to the `Collection` interface.

---

**Q32. What happens when a class implements two interfaces with the same default method?**

**A:** The compiler fails with a duplicate default method error (the diamond problem). The implementing class **must override** the method and explicitly choose which interface implementation to use or write a custom one.

```java
@Override
public void doSomething() {
    InterfaceA.super.doSomething(); // Choose Interface A's method
}
```

---

**Q33. What is a static method in an interface?**

**A:** A static method in an interface belongs to the interface class itself and cannot be overridden by implementing classes. It is called using the interface name: `InterfaceName.staticMethodName()`. Used to define utility methods.

---

**Q34. Can you override a default method in a class that implements the interface?**

**A:** Yes, default methods can be overridden in the implementing class like normal methods to provide custom behavior.

---

## Concurrency Improvements

**Q35. What is `CompletableFuture`? How is it different from `Future`?**

**A:** `CompletableFuture` implements `Future` and `CompletionStage`.
*   *Differences:*
    *   Traditional `Future` required blocking `.get()` calls or busy-waiting loops to check if a task completed.
    *   `CompletableFuture` allows non-blocking callback chaining (promises) that trigger automatically when a task finishes. It also supports manual completion and robust exception handling.

---

**Q36. How do you chain asynchronous tasks using `thenApply()`, `thenCompose()`, and `thenCombine()`?**

**A:**
*   `thenApply()`: Transforms the result of the previous task (returns another value). Similar to `map`.
*   `thenCompose()`: Chains another asynchronous task that returns a `CompletableFuture`. Similar to `flatMap`.
*   `thenCombine()`: Runs two independent asynchronous tasks in parallel and combines their results when both complete.

---

**Q37. What is `exceptionally()` and `handle()` in `CompletableFuture`?**

**A:**
*   `exceptionally(Function<Throwable, T>)`: Catch block. Executes only if an exception is thrown in the chain, returning a fallback value.
*   `handle(BiFunction<T, Throwable, R>)`: Executes always, regardless of success or failure. It receives both the result and the exception, allowing you to recover or transform the output.

---

**Q38. What is the difference between `runAsync()` and `supplyAsync()`?**

**A:**
*   `runAsync(Runnable)`: Runs an asynchronous task that does **not** return a value (void).
*   `supplyAsync(Supplier<T>)`: Runs an asynchronous task that returns a value of type T.

---

## Coding Problems (Java 8 Style)

**Q39. Write a program to find the frequency of each character in a string using streams.**

**A:**
```java
String input = "hello world";
Map<Character, Long> charCount = input.chars()
    .mapToObj(c -> (char) c)
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
```

---

**Q40. Write a program to find all duplicate elements in a list using streams.**

**A:**
```java
List<Integer> numbers = List.of(1, 2, 3, 2, 4, 3, 5);
Set<Integer> duplicates = numbers.stream()
    .filter(n -> Collections.frequency(numbers, n) > 1)
    .collect(Collectors.toSet());
```
*Alternative (Faster, $O(n)$):*
```java
Set<Integer> seen = new HashSet<>();
Set<Integer> duplicates = numbers.stream()
    .filter(n -> !seen.add(n))
    .collect(Collectors.toSet());
```

---

**Q41. Write a program to flatten a `List<List<Integer>>` into a single `List<Integer>`.**

**A:**
```java
List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4));
List<Integer> flat = nested.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList());
```

---

**Q42. Write a program to find the sum of all even numbers in a list using streams.**

**A:**
```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
int sumOfEvens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .mapToInt(Integer::intValue)
    .sum();
```

---

**Q43. Write a program to convert a list of strings to uppercase and sort them alphabetically.**

**A:**
```java
List<String> words = List.of("banana", "apple", "cherry");
List<String> sortedUpper = words.stream()
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());
```

---

**Q44. Write a program to find the employee with the highest salary using streams.**

**A:**
```java
Optional<Employee> richest = employees.stream()
    .max(Comparator.comparingDouble(Employee::getSalary));
```

---

**Q45. Write a program to partition a list of numbers into even and odd using `partitioningBy`.**

**A:**
```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);
Map<Boolean, List<Integer>> evenOrOdd = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
```

---

**Q46. Write a program to join a list of strings with a delimiter using `Collectors.joining()`.**

**A:**
```java
List<String> names = List.of("Alice", "Bob", "Charlie");
String result = names.stream()
    .collect(Collectors.joining(", ", "[", "]")); // returns "[Alice, Bob, Charlie]"
```

---

**Q47. Write a program to remove duplicates from a list while maintaining order using streams.**

**A:**
```java
List<Integer> numbers = List.of(1, 2, 2, 3, 1, 4);
List<Integer> distinctOrdered = numbers.stream()
    .distinct()
    .collect(Collectors.toList());
```

---

**Q48. Write a program to group a list of strings by their length using `groupingBy`.**

**A:**
```java
List<String> words = List.of("a", "bb", "cc", "ddd");
Map<Integer, List<String>> grouped = words.stream()
    .collect(Collectors.groupingBy(String::length));
```

---
