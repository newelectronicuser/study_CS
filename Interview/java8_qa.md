# Java 8 — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Lambda Expressions

**Q1. What is a lambda expression and what problem does it solve?**

**A:** A lambda expression is an anonymous function that can be passed as an argument or stored in a variable. It enables functional programming in Java, replacing verbose anonymous inner class boilerplate.

```java
// Before Java 8
Runnable r = new Runnable() {
    public void run() { System.out.println("Hello"); }
};
// Java 8 lambda
Runnable r = () -> System.out.println("Hello");
```

---

**Q2. What is a functional interface? Name five built-in ones.**

**A:** A functional interface has exactly one abstract method and is annotated with `@FunctionalInterface`. Built-ins:
- `Function<T,R>`: `R apply(T t)` — transforms T to R
- `Consumer<T>`: `void accept(T t)` — consumes T, no return
- `Supplier<T>`: `T get()` — produces T, no input
- `Predicate<T>`: `boolean test(T t)` — tests a condition
- `BiFunction<T,U,R>`: `R apply(T t, U u)` — two inputs, one output

---

**Q3. What are the four types of method references?**

**A:**
- **Static**: `ClassName::staticMethod` → `Math::abs`
- **Instance (specific object)**: `instance::method` → `str::toLowerCase`
- **Instance (arbitrary object of type)**: `ClassName::instanceMethod` → `String::toUpperCase`
- **Constructor**: `ClassName::new` → `ArrayList::new`

---

**Q4. How does a lambda capture variables from the enclosing scope?**

**A:** Lambdas can capture local variables and parameters from the enclosing method, but those variables must be **effectively final** (not modified after assignment). They may freely capture instance/static fields. Captured local variables are copied into the lambda — this is why they must be immutable.

---

## Stream API

**Q5. What is the difference between `map()` and `flatMap()`?**

**A:** `map()` transforms each element one-to-one (returns a stream of the same size). `flatMap()` transforms each element into a stream (one-to-many) and then flattens all those streams into one.

```java
// map: ["hello", "world"] -> [5, 5]
list.stream().map(String::length)

// flatMap: ["a b", "c d"] -> ["a", "b", "c", "d"]
list.stream().flatMap(s -> Arrays.stream(s.split(" ")))
```

---

**Q6. What is `reduce()` and how does it work?**

**A:** `reduce()` is a terminal operation that combines stream elements into a single result using a BinaryOperator.

```java
int sum = IntStream.rangeClosed(1, 10).reduce(0, Integer::sum); // 55
Optional<Integer> max = Stream.of(3,1,4,1,5).reduce(Integer::max); // 5
```

---

**Q7. How do `Collectors.groupingBy()` and `partitioningBy()` work?**

**A:**
- `groupingBy(classifier)`: Groups elements into a `Map<K, List<T>>` by a classification function.
- `partitioningBy(predicate)`: A special case of groupingBy — returns `Map<Boolean, List<T>>` splitting elements into two groups.

```java
// Group employees by department
Map<String, List<Employee>> byDept =
    employees.stream().collect(Collectors.groupingBy(Employee::getDept));

// Partition by salary > 50000
Map<Boolean, List<Employee>> partitioned =
    employees.stream().collect(Collectors.partitioningBy(e -> e.getSalary() > 50000));
```

---

**Q8. What is a parallel stream and what are the risks?**

**A:** `parallelStream()` splits the stream into sub-tasks processed by the ForkJoinPool. Useful for CPU-intensive operations on large datasets. Risks:
- Not always faster (overhead of splitting/merging)
- Order is not guaranteed unless you use `forEachOrdered()`
- Unsafe if the stream operations have side effects or use non-thread-safe shared state
- Poor performance for I/O-bound tasks

---

**Q9. How would you find the second highest salary using streams?**

**A:**
```java
Optional<Integer> secondHighest = salaries.stream()
    .distinct()
    .sorted(Comparator.reverseOrder())
    .skip(1)
    .findFirst();
```

---

**Q10. How would you group employees by department and count them?**

**A:**
```java
Map<String, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept, Collectors.counting()));
```

---

## Optional

**Q11. What is `Optional<T>` and why was it introduced?**

**A:** `Optional` is a container that may or may not contain a non-null value. Introduced to explicitly model the absence of a value, reducing `NullPointerException`s and forcing callers to handle the absent case. It's most useful as a return type for methods that might not find a result.

---

**Q12. What is `orElse()` vs `orElseGet()` vs `orElseThrow()`?**

**A:**
- `orElse(default)`: Always evaluates and returns `default` if empty (even if it's an expensive computation).
- `orElseGet(Supplier)`: Lazily evaluates the supplier only if the Optional is empty. Prefer this for expensive defaults.
- `orElseThrow(Supplier)`: Throws the supplied exception if empty. Good for validation.

---

**Q13. What is the anti-pattern of using `isPresent()` + `get()`?**

**A:** Using `if (opt.isPresent()) { opt.get() }` is equivalent to a null check — it defeats the purpose of Optional. Prefer: `opt.ifPresent(val -> use(val))` or `opt.map(...)` or `opt.orElse(...)` for a more functional, safe approach.

---

## Date & Time API

**Q14. What problems did `java.util.Date` have? How does the new API solve them?**

**A:** `java.util.Date` was mutable (not thread-safe), had confusing month/year offsets (months are 0-indexed), mixed date and time, and its API was poorly designed. `java.time` (JSR-310):
- Immutable and thread-safe
- Clear separation: `LocalDate`, `LocalTime`, `LocalDateTime`, `ZonedDateTime`, `Instant`
- Intuitive API: `LocalDate.now()`, `date.plusDays(7)`, `DateTimeFormatter`
- Properly handles timezones with `ZoneId`

---

**Q15. What is a `Period` vs a `Duration`?**

**A:**
- `Period`: Date-based amount of time (years, months, days). `Period.between(startDate, endDate)`.
- `Duration`: Time-based amount (hours, minutes, seconds, nanoseconds). `Duration.between(startTime, endTime)`.

---

## CompletableFuture

**Q16. What is `CompletableFuture` and how is it different from `Future`?**

**A:** `Future` (Java 5) represents an async result but blocks on `get()` and has no composition support. `CompletableFuture` (Java 8) is non-blocking and composable:
- Chain with `thenApply()`, `thenCompose()`, `thenCombine()`
- Handle errors with `exceptionally()` or `handle()`
- Combine multiple futures with `allOf()` or `anyOf()`
- Can be manually completed with `complete(value)`

---

**Q17. How do `thenApply()`, `thenCompose()`, and `thenCombine()` differ?**

**A:**
- `thenApply(Function)`: Transforms the result. Like `map()`. For synchronous transformation.
- `thenCompose(Function)`: Chains async operations. Like `flatMap()`. Avoids `CompletableFuture<CompletableFuture<T>>`.
- `thenCombine(other, BiFunction)`: Combines results of two independent futures when both complete.

---

## Coding Answers

**Q18. Frequency of each character in a string using streams.**

```java
Map<Character, Long> freq = str.chars()
    .mapToObj(c -> (char) c)
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
```

---

**Q19. Find duplicate elements in a list.**

```java
Set<Integer> seen = new HashSet<>();
List<Integer> duplicates = list.stream()
    .filter(n -> !seen.add(n))
    .distinct()
    .collect(Collectors.toList());
```

---

**Q20. Flatten a `List<List<Integer>>` into a `List<Integer>`.**

```java
List<Integer> flat = listOfLists.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList());
```

---

**Q21. Join a list of strings with a delimiter.**

```java
String result = List.of("Java", "Python", "Go")
    .stream()
    .collect(Collectors.joining(", ", "[", "]"));
// Output: [Java, Python, Go]
```

---

**Q22. Remove duplicates from a list maintaining order.**

```java
List<String> unique = list.stream()
    .distinct()
    .collect(Collectors.toList());
```

---

**Q23. Group strings by their length.**

```java
Map<Integer, List<String>> byLength = words.stream()
    .collect(Collectors.groupingBy(String::length));
```

---

**Q24. Convert a list of strings to uppercase and sort alphabetically.**

```java
List<String> result = list.stream()
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());
```

---
