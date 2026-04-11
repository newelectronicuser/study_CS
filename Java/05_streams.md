# Java Streams API - Interview Notes 🌊

## 1. Introduction
The **Streams API**, introduced in Java 8, is used to process collections of objects in a functional-style manner. A stream is a **sequence of elements** that can be processed with a series of pipeline operations.

- **Key Feature**: Streams do not change the original data source; they provide the results as per the pipelined methods.
- **Components**: Source -> Intermediate Operations -> Terminal Operation.

---

## 2. Imperative vs. Declarative Programming
- **Imperative**: Focuses on **how** to perform a task (loops, conditionals, state changes). 
    *Example*: Managing index in a `for` loop.
- **Declarative**: Focuses on **what** you want to achieve.
    *Example*: "Filter results that match X and collect them into a list."

---

## 3. Imperative vs. Functional Programming
Functional programming is a subset of declarative programming that emphasizes **immutability** and **pure functions** (no side effects). Streams allow you to write functional-style code in Java.

---

## 4. Creating a Stream
There are multiple ways to initialize a stream:
- **From Collections**: `list.stream()` or `map.entrySet().stream()`.
- **From Arrays**: `Arrays.stream(array)`.
- **Factory Methods**: 
    - `Stream.of(1, 2, 3)`
    - `Stream.iterate(0, n -> n + 1).limit(10)` (Infinite streams)
    - `Stream.generate(Math::random).limit(5)`

```java
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> colStream = list.stream();

String[] arr = {"x", "y", "z"};
Stream<String> arrStream = Arrays.stream(arr);

Stream<Integer> factoryStream = Stream.of(1, 2, 3);
```

---

## 5. Mapping Elements
Mapping is used to transform elements in a stream.

- **map()**: One-to-one transformation (e.g., `String` to `Integer`).
- **flatMap()**: One-to-many transformation. Flattens a stream of collections (e.g., `Stream<List<Integer>>` becomes `Stream<Integer>`).

```java
// map() example
List<String> names = List.of("alice", "bob");
List<Integer> nameLengths = names.stream()
    .map(String::length)
    .collect(Collectors.toList()); // [5, 3]

// flatMap() example
List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4));
List<Integer> flat = nested.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList()); // [1, 2, 3, 4]
```

---

## 6. Filtering Elements
The `filter()` method is an intermediate operation that matches elements against a `Predicate`.
```java
List<String> names = List.of("Anna", "Bob", "Alice");
names.stream()
     .filter(s -> s.startsWith("A"))
     .forEach(System.out::println); // Anna, Alice
```

---

## 7. Slicing Elements
Used to extract a sub-portion of a stream:
- **limit(n)**: Returns first `n` elements.
- **skip(n)**: Discards first `n` elements.
- **takeWhile(predicate)**: Returns elements until the predicate becomes false.
- **dropWhile(predicate)**: Drops elements until the predicate becomes false.

```java
Stream.of(1, 2, 3, 4, 5).limit(3); // [1, 2, 3]
Stream.of(1, 2, 3, 4, 5).skip(2);  // [3, 4, 5]

// Java 9+: Stops as soon as condition fails
Stream.of(1, 2, 3, 4, 1).takeWhile(n -> n < 4); // [1, 2, 3]

// Java 9+: Drops until condition fails, then keeps the rest
Stream.of(1, 2, 3, 4, 1).dropWhile(n -> n < 4); // [4, 1]
```

---

## 8. Sorting Elements
Use the `sorted()` method. You can provide a custom `Comparator`.
```java
// Natural order
List<Integer> sorted = Stream.of(5, 1, 3).sorted().collect(Collectors.toList()); // [1, 3, 5]

// Custom Comparator
List<String> byLength = Stream.of("apple", "pie", "banana")
    .sorted(Comparator.comparingInt(String::length))
    .collect(Collectors.toList()); // [pie, apple, banana]
```

---

## 9. Getting Unique Elements
The `distinct()` method uses `hashCode()` and `equals()` to remove duplicates from a stream.

```java
List<Integer> unique = Stream.of(1, 2, 2, 3, 1)
    .distinct()
    .collect(Collectors.toList()); // [1, 2, 3]
```

---

## 10. Peeking Elements
The `peek()` method is primarily for **debugging**. It allows you to see the elements as they pass through the pipeline without consuming the stream.
> [!CAUTION]
> Do not use `peek()` for production logic; use it only to understand the state of the stream during testing.

```java
long count = Stream.of("a", "b", "c")
    .peek(e -> System.out.println("Processing: " + e))
    .count();
```

---

## 11. Simple Reducers
Terminal operations that reduce a stream to a single value:
- `count()`, `min(comparator)`, `max(comparator)`, `sum()` (on primitive streams).

```java
long totalElements = Stream.of(1, 2, 3).count(); // 3

Optional<Integer> maxVal = Stream.of(1, 5, 3)
    .max(Comparator.naturalOrder()); // Optional[5]
```

---

## 12. Reducing a Stream
The `reduce()` method is a general-purpose reduction tool.
- **Format**: `T reduce(T identity, BinaryOperator<T> accumulator)`
```java
int sum = numbers.stream().reduce(0, Integer::sum);
```

---

## 13. Collectors
The `collect()` method is a terminal operation that transforms stream elements into a different form (usually a Collection).
- `Collectors.toList()`
- `Collectors.toSet()`
- `Collectors.toMap(keyMapper, valueMapper)`
- `Collectors.joining(", ")` (for concatenating strings)

```java
List<String> items = List.of("apple", "banana", "apple");

Set<String> uniqueItems = items.stream().collect(Collectors.toSet());

Map<String, Integer> lengthMap = items.stream()
    .distinct()
    .collect(Collectors.toMap(s -> s, String::length)); // {banana=6, apple=5}

String csv = items.stream().collect(Collectors.joining(", ")); // "apple, banana, apple"
```

---

## 14. Grouping Elements
The `groupingBy()` collector is similar to "GROUP BY" in SQL.
```java
Map<User.Role, List<User>> usersByRole = users.stream()
    .collect(Collectors.groupingBy(User::getRole));
```

---

## 15. Partitioning Elements
A specialized version of `groupingBy` that always uses a boolean key.
```java
Map<Boolean, List<Student>> passFail = students.stream()
    .collect(Collectors.partitioningBy(s -> s.getGrade() > 50));
```

---

## 16. Primitive Type Streams
Specialized streams to avoid the overhead of **Autoboxing** and **Unboxing**.
- **IntStream**, **LongStream**, **DoubleStream**.
- They provide specialized reducers like `sum()`, `average()`, and `summaryStatistics()`.

```java
int sum = IntStream.rangeClosed(1, 5).sum(); // 15

IntSummaryStatistics stats = IntStream.of(1, 2, 3, 4, 5).summaryStatistics();
System.out.println(stats.getMax()); // 5
System.out.println(stats.getAverage()); // 3.0
```

---

## 17. Summary Table
| Operation Type | Examples | Description |
| :--- | :--- | :--- |
| **Intermediate (Lazy)** | `map`, `filter`, `limit`, `sorted` | Returns a new stream. Not executed until a terminal call is made. |
| **Terminal** | `forEach`, `collect`, `reduce`, `count` | Produces a result or side-effect. Closes the stream. |

> [!IMPORTANT]
> **Stream characteristic**: Streams are **not reusable**. Once a terminal operation is called, the stream is closed and cannot be used again.
