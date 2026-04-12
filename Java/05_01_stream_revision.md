# Java Streams Quick Revision 🚀

A concise guide to common Stream API operations for rapid interview prep and daily use.

---

## 💎 1. Basic Reductions & Transformations
Simple operations to extract or modify values in a collection.

### Find Maximum Value
```java
List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
var max = list.stream().reduce(Integer::max);
System.out.println(max); // Optional[10]
```

### String Transformation (to UpperCase)
```java
List<String> list4 = List.of("apple", "mango", "banana");
var upperCaseList = list4.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

upperCaseList.forEach(System.out::println);
```

---

## 🔍 2. Filtering & String Utilities
Handling empty strings, blanks, and null safety.

### Filter Empty/Blank Strings
| Method | Description |
| :--- | :--- |
| `isEmpty()` | Returns true if length is 0. |
| `isBlank()` | Returns true if string is empty or contains only whitespace. |

```java
List<String> list5 = List.of("apple", "   ", "mango", "mango");
List<String> list6 = List.of("apple", "", "mango", "mango");

// Filter out empty strings and duplicates
var trimmedList = list6.stream()
    .filter(str -> !str.trim().isEmpty())
    .distinct()
    .toList();

// Handling blank strings (whitespace)
list5.stream()
    .filter(String::isBlank)
    .distinct()
    .forEach(System.out::print);
```

### 🛡️ Null Safety with Optional
```java
String str = null;
// Defensive check
if (str == null || str.trim().isEmpty()) {
    System.out.println("String is null or blank");
}

// Elegant check using Optional
String op = Optional.ofNullable(str).orElseGet(() -> "Unknown");
```

---

## 🗺️ 3. Map Iteration (HashMap)
Effective ways to iterate through key-value pairs.

```java
HashMap<String, Integer> fruitBasket = new HashMap<>();
fruitBasket.put("mango", 10);
fruitBasket.put("apple", 6);

// Simple forEach (Key-Value)
fruitBasket.forEach((key, value) -> System.out.println(key + " = " + value));

// Iterate over EntrySet
for (var entry : fruitBasket.entrySet()) {
    System.out.println(entry.getKey() + " " + entry.getValue());
}
```

---

## 🔢 4. Predicates & Retrievals
Filtering numbers and fetching specific elements.

### Filter & Sum
```java
List<Integer> list7 = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

// Find numbers > 5
list7.stream().filter(x -> x > 5).forEach(System.out::print);

// Calculate Sum
var sum = list7.stream().reduce(0, Integer::sum);
```

### findAny() vs findFirst()
> [!NOTE]
> `findAny()` is faster in parallel streams as it doesn't care about order. `findFirst()` always returns the first element in the encounter order.

```java
var any = list7.stream().findAny();    // Returns any element
var first = list4.stream().findFirst(); // Returns "apple"
```

---

## ✂️ 5. Advanced Mapping & Matching
Manipulating complex strings and validating collections.

### Extract First Word from String
```java
List<String> names = List.of("Tom Cruise", "Brad Pitt", "Will Smith");
var firstNames = names.stream()
    .map(name -> name.trim().split("\\s")[0])
    .toList();

System.out.println(firstNames); // [Tom, Brad, Will]
```

### Predicate Matching
Check if conditions apply to all or any elements.

```java
List<Integer> list8 = List.of(1, -6, 8, 6, -7);

// Check if all numbers are positive
boolean allPositive = list8.stream().allMatch(x -> x > 0); 

// Check if any number is negative
boolean hasNegative = list8.stream().anyMatch(x -> x < 0);

System.out.println("All positive: " + allPositive);
```

---

> [!TIP]
> **Stream Efficiency**: Always use `toList()` (Java 16+) or `collect(Collectors.toList())` based on whether you need a mutable or immutable list.
