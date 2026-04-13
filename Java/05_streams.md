# Java Streams API - Interview Notes 🌊

## 1. Introduction
The **Streams API**, introduced in Java 8, is used to process collections of objects in a functional-style manner. A stream is a **sequence of elements** that can be processed with a series of pipeline operations.

- **Key Feature**: Streams do not change the original data source; they provide the results as per the pipelined methods.
- **Components**: Source → Intermediate Operations → Terminal Operation.
- **Lazy Evaluation**: Intermediate operations are only executed when a terminal operation is invoked.

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

| Source | Example |
| :--- | :--- |
| Collection | `list.stream()` |
| Array | `Arrays.stream(arr)` |
| Varargs | `Stream.of(1, 2, 3)` |
| Infinite (generate) | `Stream.generate(Math::random).limit(5)` |
| Infinite (iterate) | `Stream.iterate(0, n -> n + 1).limit(10)` |
| Iterate with predicate (Java 9) | `Stream.iterate(1, n -> n <= 10, n -> n + 1)` |
| Primitive range | `IntStream.rangeClosed(1, 10)` |

```java
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> colStream = list.stream();

int[] arr = {1, 2, 3};
IntStream arrStream = Arrays.stream(arr);

Stream<Integer> factoryStream = Stream.of(1, 2, 3);

// Infinite streams — always pair with limit()
Stream.generate(Math::random).limit(5).forEach(System.out::println);
Stream.iterate(0, n -> n + 2).limit(5).forEach(System.out::println); // 0 2 4 6 8

// Java 9: iterate with stop condition
Stream.iterate(1, n -> n <= 10, n -> n + 1).forEach(System.out::println);

// Primitive ranges
IntStream.range(1, 4);         // 1 2 3 (exclusive end)
IntStream.rangeClosed(1, 4);   // 1 2 3 4 (inclusive end)
```

---

## 5. Mapping Elements
Mapping is used to transform elements in a stream.

- **`map()`**: One-to-one transformation (e.g., `String` → `Integer`).
- **`flatMap()`**: One-to-many transformation. Flattens a stream of collections (e.g., `Stream<List<Integer>>` → `Stream<Integer>`).
- **`mapToInt()` / `mapToLong()` / `mapToDouble()`**: Map to primitive streams (avoids boxing).

```java
// map() — one-to-one
List<String> names = List.of("alice", "bob");
List<Integer> nameLengths = names.stream()
    .map(String::length)
    .collect(Collectors.toList()); // [5, 3]

// flatMap() — flatten nested collections
List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4));
List<Integer> flat = nested.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList()); // [1, 2, 3, 4]

// Practical flatMap: tokenize sentences
List<String> sentences = List.of("Hello World", "Java Streams");
List<String> words = sentences.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .collect(Collectors.toList()); // [Hello, World, Java, Streams]

// mapToInt — primitive, avoids boxing
int totalLikes = movies.stream().mapToInt(Movie::getLikes).sum();
```

---

## 6. Filtering Elements
The `filter()` method is an intermediate operation that matches elements against a `Predicate`.
```java
// Simple filter
List<String> names = List.of("Anna", "Bob", "Alice");
names.stream()
     .filter(s -> s.startsWith("A"))
     .forEach(System.out::println); // Anna, Alice

// Chain multiple filters (equivalent to &&)
long count = movies.stream()
    .filter(m -> m.getGenre() == Genre.THRILLER)
    .filter(m -> m.getLikes() > 800)
    .count();

// Filter non-blank strings (common interview question)
List<String> clean = data.stream()
    .filter(s -> !s.isBlank())
    .collect(Collectors.toList());
```

---

## 7. Slicing Elements
Used to extract a sub-portion of a stream:
- **`limit(n)`**: Returns first `n` elements.
- **`skip(n)`**: Discards first `n` elements.
- **`takeWhile(predicate)`** (Java 9+): Returns elements until the predicate becomes false.
- **`dropWhile(predicate)`** (Java 9+): Drops elements until the predicate becomes false.

```java
// Pagination pattern
int pageSize = 2, pageNumber = 2;
movies.stream()
    .skip((long)(pageNumber - 1) * pageSize)
    .limit(pageSize)
    .collect(Collectors.toList());

// takeWhile — stops as soon as condition fails
Stream.of(2, 4, 6, 7, 8).takeWhile(n -> n % 2 == 0); // [2, 4, 6]

// dropWhile — drops until condition fails, keeps the rest
Stream.of(1, 2, 3, 10, 11).dropWhile(n -> n < 10);   // [10, 11]
```

---

## 8. Sorting Elements
Use the `sorted()` method. You can provide a custom `Comparator`.
```java
// Natural order
List<Integer> sorted = Stream.of(5, 1, 3).sorted().collect(Collectors.toList());

// Reverse order
Stream.of(5, 1, 3).sorted(Comparator.reverseOrder());

// Sort by field
movies.stream().sorted(Comparator.comparingInt(Movie::getLikes));

// Multi-key sort: genre asc, then likes desc
movies.stream()
    .sorted(Comparator.comparing(Movie::getGenre)
                      .thenComparingInt(Movie::getLikes)
                      .reversed());
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
List<String> result = movies.stream()
    .filter(m -> m.getLikes() > 700)
    .peek(m -> System.out.println("[after filter] " + m.getTitle()))
    .map(Movie::getTitle)
    .peek(t -> System.out.println("[after map]   " + t))
    .collect(Collectors.toList());
```

---

## 11. Simple Reducers
Terminal operations that reduce a stream to a single value:
- `count()`, `min(comparator)`, `max(comparator)`, `sum()` (on primitive streams).

```java
long n = movies.stream().filter(m -> m.getGenre() == Genre.ACTION).count();

Optional<Movie> least = movies.stream().min(Comparator.comparingInt(Movie::getLikes));
Optional<Movie> most  = movies.stream().max(Comparator.comparingInt(Movie::getLikes));

// findFirst — deterministic (respects encounter order)
Optional<Movie> first = movies.stream()
    .filter(m -> m.getGenre() == Genre.THRILLER)
    .findFirst();

// findAny — non-deterministic; faster in parallel streams
Optional<Movie> any = movies.parallelStream()
    .filter(m -> m.getGenre() == Genre.COMEDY)
    .findAny();
```

---

## 12. Matching
Short-circuit operations that return a `boolean`:

| Method | Description |
| :--- | :--- |
| `allMatch(p)` | True if **all** elements match `p` |
| `anyMatch(p)` | True if **at least one** element matches `p` |
| `noneMatch(p)` | True if **no** element matches `p` |

```java
boolean allHaveGenre  = movies.stream().allMatch(m -> m.getGenre() != null);
boolean hasBlockbuster = movies.stream().anyMatch(m -> m.getLikes() > 900);
boolean noFlop         = movies.stream().noneMatch(m -> m.getLikes() < 100);
```

---

## 13. Reducing a Stream
The `reduce()` method is a general-purpose reduction tool.
- **Format**: `T reduce(T identity, BinaryOperator<T> accumulator)`

```java
int sum = numbers.stream().reduce(0, Integer::sum);

// Without identity → Optional (stream may be empty)
Optional<Integer> product = numbers.stream().reduce((a, b) -> a * b);

// Preferred for field sums — avoids boxing
int totalLikes = movies.stream().mapToInt(Movie::getLikes).sum();
```

---

## 14. Collectors
The `collect()` method is a terminal operation that transforms stream elements into a different form.

| Collector | Result |
| :--- | :--- |
| `Collectors.toList()` | `List<T>` |
| `Collectors.toUnmodifiableList()` | Immutable `List<T>` |
| `Collectors.toSet()` | `Set<T>` (no duplicates) |
| `Collectors.toMap(k, v)` | `Map<K, V>` |
| `Collectors.toMap(k, v, mergeFunc)` | `Map<K, V>` with conflict resolution |
| `Collectors.joining(delim, prefix, suffix)` | Concatenated `String` |
| `Collectors.counting()` | `Long` count (used as downstream) |
| `Collectors.summingInt(fn)` | Sum of extracted int field |
| `Collectors.summarizingInt(fn)` | `IntSummaryStatistics` |

```java
// toList / toSet
List<String> titles = movies.stream().map(Movie::getTitle).collect(Collectors.toList());
Set<Genre>   genres = movies.stream().map(Movie::getGenre).collect(Collectors.toSet());

// toMap
Map<String, Integer> titleToLikes = movies.stream()
    .collect(Collectors.toMap(Movie::getTitle, Movie::getLikes));

// toMap with merge — keep the higher likes per genre
Map<Genre, Integer> genreMax = movies.stream()
    .collect(Collectors.toMap(Movie::getGenre, Movie::getLikes, Integer::max));

// joining
String csv     = movies.stream().map(Movie::getTitle).collect(Collectors.joining(", "));
String wrapped = movies.stream().map(Movie::getTitle).collect(Collectors.joining(", ", "[", "]"));

// summarizingInt
IntSummaryStatistics stats = movies.stream()
    .collect(Collectors.summarizingInt(Movie::getLikes));
System.out.printf("min=%d max=%d avg=%.1f%n", stats.getMin(), stats.getMax(), stats.getAverage());
```

---

## 15. Grouping Elements
The `groupingBy()` collector is analogous to SQL `GROUP BY`.

```java
// Simple grouping
Map<Genre, List<Movie>> byGenre = movies.stream()
    .collect(Collectors.groupingBy(Movie::getGenre));

// Downstream: counting
Map<Genre, Long> counts = movies.stream()
    .collect(Collectors.groupingBy(Movie::getGenre, Collectors.counting()));

// Downstream: mapping titles per genre
Map<Genre, List<String>> titlesByGenre = movies.stream()
    .collect(Collectors.groupingBy(
        Movie::getGenre,
        Collectors.mapping(Movie::getTitle, Collectors.toList())
    ));

// Downstream: sum of likes per genre
Map<Genre, Integer> likesByGenre = movies.stream()
    .collect(Collectors.groupingBy(Movie::getGenre, Collectors.summingInt(Movie::getLikes)));

// Multi-level grouping
Map<Genre, Map<String, List<Movie>>> multilevel = movies.stream()
    .collect(Collectors.groupingBy(
        Movie::getGenre,
        Collectors.groupingBy(m -> m.getLikes() >= 700 ? "Popular" : "Average")
    ));
```

---

## 16. Partitioning Elements
A specialized `groupingBy` that **always** produces a `Map<Boolean, List<T>>`.

```java
Map<Boolean, List<Movie>> partition = movies.stream()
    .collect(Collectors.partitioningBy(m -> m.getLikes() >= 700));

partition.get(true);  // popular movies
partition.get(false); // average movies

// With downstream collector
Map<Boolean, Long> partitionCount = movies.stream()
    .collect(Collectors.partitioningBy(
        m -> m.getGenre() == Genre.THRILLER,
        Collectors.counting()
    ));
```

---

## 17. Primitive Type Streams
Specialized streams to avoid **autoboxing/unboxing** overhead.

| Stream | Conversions |
| :--- | :--- |
| `IntStream` | `mapToInt()`, `IntStream.of()`, `IntStream.range()` |
| `LongStream` | `mapToLong()`, `LongStream.rangeClosed()` |
| `DoubleStream` | `mapToDouble()`, `DoubleStream.of()` |

```java
int sumTo100 = IntStream.rangeClosed(1, 100).sum(); // 5050

// summaryStatistics
IntSummaryStatistics stats = IntStream.of(3, 1, 4, 1, 5, 9).summaryStatistics();

// mapToInt on objects — preferred over boxing
int totalLikes = movies.stream().mapToInt(Movie::getLikes).sum();

// mapToObj — back to object stream
String result = IntStream.rangeClosed(1, 5)
    .mapToObj(Integer::toString)
    .collect(Collectors.joining("-")); // 1-2-3-4-5

// Long factorial
long factorial = LongStream.rangeClosed(1, 10).reduce(1L, (a, b) -> a * b);
```

---

## 18. Parallel Streams
> [!NOTE]
> Uses `ForkJoinPool.commonPool()`. Best for CPU-bound tasks on large datasets. Avoid for I/O-bound work or when order matters.

```java
// parallelStream() shortcut
List<String> titles = movies.parallelStream()
    .filter(m -> m.getLikes() > 600)
    .map(Movie::getTitle)
    .collect(Collectors.toList());

// .parallel() on any stream
long sum = IntStream.rangeClosed(1, 1_000_000)
    .parallel()
    .asLongStream()
    .sum();
```

> [!WARNING]
> Parallel streams share the **global** ForkJoinPool. For latency-sensitive applications, create a custom pool with `ForkJoinPool.submit()`.

---

## 19. Optional Chaining
Terminal operations like `findFirst()`, `min()`, `max()` return `Optional<T>`.

```java
// map() on Optional
Optional<String> bestTitle = movies.stream()
    .max(Comparator.comparingInt(Movie::getLikes))
    .map(Movie::getTitle);

// orElse / orElseGet / orElseThrow
String title = movies.stream()
    .filter(m -> m.getLikes() > 10_000)
    .map(Movie::getTitle)
    .findFirst()
    .orElse("No blockbuster found");

// ifPresentOrElse (Java 9+)
movies.stream()
    .filter(m -> m.getGenre() == Genre.COMEDY)
    .findFirst()
    .ifPresentOrElse(
        m -> System.out.println("Found: " + m.getTitle()),
        () -> System.out.println("Not found")
    );

// Optional.stream() (Java 9+) — bridge Optional into a Stream
movies.stream()
    .filter(m -> m.getGenre() == Genre.COMEDY)
    .findFirst()
    .stream()  // Optional<Movie> → Stream<Movie> (0 or 1 elements)
    .count();

// Null safety
String safe = Optional.ofNullable(nullableString)
    .map(String::toUpperCase)
    .orElse("DEFAULT");
```

---

## 20. Summary Table

| Operation Type | Examples | Description |
| :--- | :--- | :--- |
| **Intermediate (Lazy)** | `map`, `flatMap`, `filter`, `limit`, `skip`, `sorted`, `distinct`, `peek`, `takeWhile`, `dropWhile` | Returns a new stream. Not executed until a terminal call is made. |
| **Terminal** | `forEach`, `collect`, `reduce`, `count`, `min`, `max`, `findFirst`, `findAny`, `allMatch`, `anyMatch`, `noneMatch` | Produces a result or side-effect. Closes the stream. |

> [!IMPORTANT]
> **Stream characteristics**:
> - Streams are **not reusable**. Once a terminal operation is called, the stream is closed.
> - Intermediate operations are **lazy** — they only execute when a terminal operation pulls data.
> - `flatMap` is essential for preventing `Stream<Stream<T>>` nesting.
> - Prefer primitive streams (`IntStream`, `LongStream`, `DoubleStream`) over `Stream<Integer>` to avoid boxing overhead.
