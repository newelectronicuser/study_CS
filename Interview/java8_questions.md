# Java 8 Programs — Interview Questions

> Mid to Senior Level | Software Developer Role

---

## Lambda Expressions

1. What is a lambda expression in Java 8? What problem does it solve?
2. What is a functional interface? Name five built-in functional interfaces from `java.util.function`.
3. What is the difference between `Function<T,R>`, `Consumer<T>`, `Supplier<T>`, and `Predicate<T>`?
4. How do you compose two `Predicate`s using `and()`, `or()`, and `negate()`?
5. What is a method reference? What are the four types of method references?
6. How does a lambda expression capture variables from the enclosing scope? What restriction applies?

---

## Stream API

7. What is a Stream in Java 8? How is it different from a Collection?
8. What is the difference between intermediate and terminal operations?
9. Name five commonly used intermediate operations and three terminal operations.
10. What is the difference between `map()` and `flatMap()`?
11. What is `reduce()` and how does it work?
12. What is `collect()` and how do `Collectors.groupingBy()` and `Collectors.partitioningBy()` work?
13. What is the difference between `findFirst()` and `findAny()`?
14. How does `filter()` + `map()` compare to a traditional for-loop in terms of performance?
15. What is a parallel stream? When should you use it and what are the risks?
16. What is `Stream.of()` vs `Arrays.stream()` vs `Collection.stream()`?
17. How do you sort a stream using a custom comparator?
18. What is `Collectors.toUnmodifiableList()` vs `Collectors.toList()`?
19. How would you find the second highest salary from a list of employees using streams?
20. How would you group employees by department and count them using streams?

---

## Optional

21. What is `Optional<T>` and why was it introduced?
22. What is the difference between `Optional.of()`, `Optional.ofNullable()`, and `Optional.empty()`?
23. What is `orElse()` vs `orElseGet()` vs `orElseThrow()`?
24. How do you chain operations on an `Optional` using `map()` and `flatMap()`?
25. What is the anti-pattern of using `isPresent()` + `get()`?

---

## Date & Time API

26. What problems did `java.util.Date` and `Calendar` have? How does the new Date/Time API solve them?
27. What is the difference between `LocalDate`, `LocalTime`, `LocalDateTime`, and `ZonedDateTime`?
28. How do you parse and format dates using `DateTimeFormatter`?
29. What is a `Period` vs a `Duration`?
30. How do you convert between the old `Date` API and the new API?

---

## Default & Static Methods in Interfaces

31. What is a default method in an interface? Why was it introduced?
32. What happens when a class implements two interfaces with the same default method?
33. What is a static method in an interface?
34. Can you override a default method in a class that implements the interface?

---

## Concurrency Improvements

35. What is `CompletableFuture`? How is it different from `Future`?
36. How do you chain asynchronous tasks using `thenApply()`, `thenCompose()`, and `thenCombine()`?
37. What is `exceptionally()` and `handle()` in `CompletableFuture`?
38. What is the difference between `runAsync()` and `supplyAsync()`?

---

## Coding Problems (Java 8 Style)

39. Write a program to find the frequency of each character in a string using streams.
40. Write a program to find all duplicate elements in a list using streams.
41. Write a program to flatten a `List<List<Integer>>` into a single `List<Integer>`.
42. Write a program to find the sum of all even numbers in a list using streams.
43. Write a program to convert a list of strings to uppercase and sort them alphabetically.
44. Write a program to find the employee with the highest salary using streams.
45. Write a program to partition a list of numbers into even and odd using `partitioningBy`.
46. Write a program to join a list of strings with a delimiter using `Collectors.joining()`.
47. Write a program to remove duplicates from a list while maintaining order using streams.
48. Write a program to group a list of strings by their length using `groupingBy`.

---
