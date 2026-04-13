# Java Streams Quick Revision 🚀

A concise guide to common Stream API operations for rapid interview prep and daily use.

---

## 💎 1. Basic Reductions & Transformations

### Find Maximum / Minimum Value
```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
Optional<Integer> max = nums.stream().reduce(Integer::max); // Optional[10]
Optional<Integer> min = nums.stream().min(Comparator.naturalOrder()); // Optional[1]
```

### String Transformation (toUpperCase)
```java
List<String> fruits = List.of("apple", "mango", "banana");
List<String> upper = fruits.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### Sum of a Field
```java
// Prefer mapToInt → no boxing
int totalLikes = movies.stream().mapToInt(Movie::getLikes).sum();

// Or reduce
int sum = nums.stream().reduce(0, Integer::sum);
```

---

## 🔍 2. Filtering & String Utilities

### Filter Empty / Blank Strings

| Method | Description |
| :--- | :--- |
| `isEmpty()` | Returns true if length is 0. |
| `isBlank()` | Returns true if empty or whitespace-only. |

```java
List<String> data = List.of("apple", "", "  ", "mango");

// Filter blank strings
List<String> clean = data.stream()
    .filter(s -> !s.isBlank())
    .distinct()
    .collect(Collectors.toList());
```

### 🛡️ Null Safety with Optional
```java
String nullStr = null;

// Defensive check
if (nullStr == null || nullStr.isBlank()) System.out.println("Null or blank");

// Elegant Optional chain
String result = Optional.ofNullable(nullStr)
    .map(String::toUpperCase)
    .orElse("DEFAULT");
```

---

## 🗺️ 3. Map Iteration (HashMap)

```java
Map<String, Integer> basket = new HashMap<>();
basket.put("mango", 10);
basket.put("apple", 6);

// forEach on map
basket.forEach((key, val) -> System.out.println(key + " = " + val));

// Stream entrySet
basket.entrySet().stream()
    .filter(e -> e.getValue() > 7)
    .forEach(e -> System.out.println(e.getKey()));
```

---

## 🔢 4. Predicates & Retrievals

### Filter & Sum
```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

// Numbers > 5
nums.stream().filter(x -> x > 5).forEach(System.out::print); // 678 9

// Sum
int sum = nums.stream().reduce(0, Integer::sum);

// allMatch / anyMatch / noneMatch
boolean allPos  = nums.stream().allMatch(x -> x > 0);
boolean hasNeg  = nums.stream().anyMatch(x -> x < 0);
boolean noNeg   = nums.stream().noneMatch(x -> x < 0);
```

### findAny() vs findFirst()
> [!NOTE]
> `findAny()` is faster in **parallel** streams as it doesn't guarantee order. `findFirst()` always returns the first element in encounter order.

```java
Optional<Integer> any   = nums.stream().findAny();
Optional<Integer> first = nums.stream().findFirst();
```

---

## ✂️ 5. Slicing — Pagination Pattern

```java
// Pagination: page 2, 3 items per page
int pageSize = 3, page = 2;
List<Movie> pageResults = movies.stream()
    .skip((long)(page - 1) * pageSize)
    .limit(pageSize)
    .collect(Collectors.toList());

// takeWhile (Java 9+)
Stream.of(2, 4, 6, 7, 8).takeWhile(n -> n % 2 == 0); // [2, 4, 6]

// dropWhile (Java 9+)
Stream.of(1, 2, 3, 10, 11).dropWhile(n -> n < 10);   // [10, 11]
```

---

## 🔀 6. Advanced Mapping

### Extract First Word from String
```java
List<String> names = List.of("Tom Cruise", "Brad Pitt", "Will Smith");
List<String> firstNames = names.stream()
    .map(name -> name.trim().split("\\s")[0])
    .toList(); // [Tom, Brad, Will]
```

### flatMap — Flatten Nested Collections
```java
List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4));
List<Integer> flat = nested.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList()); // [1, 2, 3, 4]

// Tokenize sentences
List<String> words = List.of("Hello World", "Java Streams").stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .collect(Collectors.toList());
```

---

## 🗂️ 7. Grouping & Partitioning

```java
// groupingBy — SQL: GROUP BY
Map<Genre, List<Movie>> byGenre = movies.stream()
    .collect(Collectors.groupingBy(Movie::getGenre));

// groupingBy + counting downstream
Map<Genre, Long> counts = movies.stream()
    .collect(Collectors.groupingBy(Movie::getGenre, Collectors.counting()));

// groupingBy + sum downstream
Map<Genre, Integer> likesByGenre = movies.stream()
    .collect(Collectors.groupingBy(Movie::getGenre, Collectors.summingInt(Movie::getLikes)));

// partitioningBy — always Map<Boolean, List<T>>
Map<Boolean, List<Movie>> partition = movies.stream()
    .collect(Collectors.partitioningBy(m -> m.getLikes() >= 700));
partition.get(true);   // popular
partition.get(false);  // average
```

---

## 🔗 8. Collectors — toMap & joining

```java
// toMap — key must be unique or supply merge function
Map<String, Integer> titleToLikes = movies.stream()
    .collect(Collectors.toMap(Movie::getTitle, Movie::getLikes));

// toMap with merge function to handle key conflicts
Map<Genre, Integer> genreMax = movies.stream()
    .collect(Collectors.toMap(Movie::getGenre, Movie::getLikes, Integer::max));

// joining
String csv     = movies.stream().map(Movie::getTitle).collect(Collectors.joining(", "));
String wrapped = movies.stream().map(Movie::getTitle).collect(Collectors.joining(", ", "[", "]"));
```

---

## 🔢 9. Primitive Streams (IntStream / LongStream / DoubleStream)

```java
// Avoid boxing — use primitive streams for numeric ops
int sumTo100 = IntStream.rangeClosed(1, 100).sum();  // 5050

OptionalDouble avg = IntStream.of(10, 20, 30).average(); // 20.0

IntSummaryStatistics stats = IntStream.of(3, 1, 4, 1, 5, 9).summaryStatistics();
// stats.getMin(), getMax(), getSum(), getAverage(), getCount()

// mapToInt on object stream
int total = movies.stream().mapToInt(Movie::getLikes).sum();

// mapToObj — primitive → object stream
String joined = IntStream.rangeClosed(1, 5)
    .mapToObj(Integer::toString)
    .collect(Collectors.joining("-")); // "1-2-3-4-5"

// Long factorial
long factorial10 = LongStream.rangeClosed(1, 10).reduce(1L, (a, b) -> a * b);
```

---

## ⚡ 10. Parallel Streams

> [!NOTE]
> Use parallel streams for CPU-bound tasks on **large** data collections. Avoid them for I/O-bound operations or when thread-safety is a concern.

```java
// parallelStream() — uses ForkJoinPool.commonPool()
long sum = IntStream.rangeClosed(1, 1_000_000).parallel().asLongStream().sum();

List<String> titles = movies.parallelStream()
    .filter(m -> m.getLikes() > 600)
    .map(Movie::getTitle)
    .collect(Collectors.toList());
```

---

## 🔗 11. Optional Chaining

```java
// map Optional value
Optional<String> best = movies.stream()
    .max(Comparator.comparingInt(Movie::getLikes))
    .map(Movie::getTitle);

// orElse / orElseGet / orElseThrow
String title = movies.stream()
    .filter(m -> m.getLikes() > 10_000)
    .map(Movie::getTitle)
    .findFirst()
    .orElse("None");

// ifPresentOrElse (Java 9+)
movies.stream()
    .filter(m -> m.getGenre() == Genre.COMEDY)
    .findFirst()
    .ifPresentOrElse(
        m -> System.out.println("Found: " + m.getTitle()),
        () -> System.out.println("Not found")
    );

// Optional.stream() (Java 9+)
long found = movies.stream()
    .filter(m -> m.getGenre() == Genre.COMEDY)
    .findFirst()
    .stream()   // 0 or 1 element stream
    .count();
```

---

> [!TIP]
> **Stream Efficiency Quick Rules**:
> 1. Prefer `mapToInt/Long/Double()` over `map()` for numeric fields.
> 2. Use `toList()` (Java 16+ immutable) vs `collect(Collectors.toList())` (mutable).
> 3. `findFirst()` for sequential, `findAny()` for parallel.
> 4. Prefer `reduce(identity, op)` over a no-identity reduce when the stream could be empty.
> 5. Parallel streams share the global pool — custom `ForkJoinPool` for latency-sensitive apps.
