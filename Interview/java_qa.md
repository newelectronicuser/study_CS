# Java — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## OOP & Language Fundamentals

**Q1. What are the four pillars of OOP? Give Java examples.**

**A:**
- **Encapsulation**: Binding data and methods. Use `private` fields with public getters/setters.
- **Inheritance**: `class Dog extends Animal {}` — reuse parent behavior.
- **Polymorphism**: A `Shape` reference can hold a `Circle` or `Rectangle`; method calls resolve at runtime (overriding).
- **Abstraction**: Hide implementation behind interfaces or abstract classes. `List` interface vs `ArrayList` implementation.

---

**Q2. What is the difference between an abstract class and an interface?**

**A:**

| Feature | Abstract Class | Interface |
|---|---|---|
| Instantiation | Cannot | Cannot |
| Constructor | Has one | None |
| Fields | Any (state) | `public static final` only |
| Methods | Abstract + concrete | Abstract + default + static (Java 8+) |
| Inheritance | Single (extends) | Multiple (implements) |

Use abstract class when sharing code/state among related classes. Use interface to define a contract and support multiple implementations.

---

**Q3. What is the difference between `==` and `.equals()`?**

**A:** `==` compares object references (memory addresses) for non-primitives, or actual values for primitives. `.equals()` compares logical equality — its behavior is defined by the class. For `String`, `.equals()` compares character content while `==` checks if they point to the same object.

---

**Q4. What is the `hashCode()` contract?**

**A:** The contract:
1. If `a.equals(b)` is true, then `a.hashCode() == b.hashCode()` must also be true.
2. The reverse is not required (hash collision is allowed).

If you override `equals()`, you **must** override `hashCode()`, otherwise objects that are logically equal will behave incorrectly in `HashMap`, `HashSet`, etc. (they'd end up in different buckets).

---

**Q5. What is the difference between `final`, `finally`, and `finalize()`?**

**A:**
- `final`: Keyword preventing modification — final variable can't be reassigned, final method can't be overridden, final class can't be subclassed.
- `finally`: Block in try-catch-finally that always executes, used for cleanup.
- `finalize()`: Deprecated method called by GC before an object is collected. Unreliable and replaced by `Cleaner` / try-with-resources.

---

## Collections Framework

**Q6. How does a `HashMap` work internally?**

**A:** `HashMap` is backed by an array of `Node` entries (buckets). When you `put(key, value)`:
1. Compute `key.hashCode()`, then bit-mask with the array length to find the bucket index.
2. If the bucket is empty, insert the node. If not (collision), traverse the linked list (or Red-Black tree if size > 8 — added in Java 8) to find or append the node.
3. If load factor (default 0.75) is exceeded, the map doubles in size and rehashes.

---

**Q7. What is the difference between `HashMap`, `LinkedHashMap`, and `TreeMap`?**

**A:**
- `HashMap`: No order guarantee. O(1) get/put average.
- `LinkedHashMap`: Maintains **insertion order** (or access order for LRU). Backed by HashMap + doubly linked list.
- `TreeMap`: Keys sorted in **natural order** or by a custom `Comparator`. O(log n) operations (Red-Black tree).

---

**Q8. What is a `ConcurrentHashMap`?**

**A:** A thread-safe `HashMap` using segment-level (Java 7) or bucket-level CAS/locking (Java 8+). Unlike `Hashtable` (whole-map lock), `ConcurrentHashMap` allows concurrent reads and segment-parallel writes, providing much better throughput. It does NOT allow null keys or values.

---

**Q9. What is fail-fast vs fail-safe iterators?**

**A:**
- **Fail-fast**: Throws `ConcurrentModificationException` if the collection is modified during iteration. Used by `ArrayList`, `HashMap` iterators (checks `modCount`).
- **Fail-safe**: Operates on a snapshot copy. Modification doesn't throw exceptions but you may see stale data. Used by `CopyOnWriteArrayList`, `ConcurrentHashMap` iterators.

---

## Memory Management & JVM

**Q10. What are the major memory areas in the JVM?**

**A:**
- **Heap**: Object allocation. Divided into Young Gen (Eden + Survivor spaces) and Old Gen.
- **Stack**: Per-thread. Stores stack frames (local variables, return addresses).
- **Metaspace** (Java 8+): Class metadata. Replaced PermGen.
- **Code Cache**: JIT-compiled native code.
- **PC Register**: Per-thread program counter.

---

**Q11. How does generational garbage collection work?**

**A:** Most objects die young (weak generational hypothesis). GC splits the heap into generations:
- **Young Gen (Minor GC)**: New objects allocated here. Survivors are promoted to Old Gen after several GCs.
- **Old Gen (Major/Full GC)**: Long-lived objects. Full GC is expensive.

This design avoids scanning the entire heap for every collection — most collections only scan the small Young Gen.

---

**Q12. What is a memory leak in Java? Give an example.**

**A:** A memory leak occurs when objects are no longer needed but still referenced, preventing GC from collecting them. Examples:
- Static collections that grow unboundedly: `static List<Object> cache = new ArrayList<>();`
- Listeners not deregistered
- ThreadLocal variables not removed after thread reuse in a thread pool
- Outer class holding reference to inner class instance preventing collection

---

## Exception Handling

**Q13. What is the difference between checked and unchecked exceptions?**

**A:**
- **Checked**: Extend `Exception` (not `RuntimeException`). Must be declared in `throws` or caught. Examples: `IOException`, `SQLException`. Force the caller to handle exceptional conditions.
- **Unchecked**: Extend `RuntimeException`. Not required to be declared or caught. Examples: `NullPointerException`, `IllegalArgumentException`. Represent programming errors.

---

**Q14. What is try-with-resources?**

**A:** A Java 7+ feature that automatically closes resources implementing `AutoCloseable`. The resource is closed at the end of the block, even if an exception occurs, without needing a `finally` block.

```java
try (InputStream is = new FileInputStream("file.txt");
     BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
    // use br
} // is and br auto-closed here
```

---

## Design Patterns

**Q15. What is the Singleton pattern? How do you make it thread-safe?**

**A:** Singleton ensures only one instance of a class exists. Thread-safe approaches:
1. **Eager initialization**: `private static final Singleton INSTANCE = new Singleton();` — always safe.
2. **Synchronized method**: `public static synchronized Singleton getInstance()` — safe but slow.
3. **Double-checked locking with volatile**: Check twice with `volatile` instance to avoid synchronization after initialization.
4. **Enum Singleton**: `public enum Singleton { INSTANCE; }` — JVM guarantees single instance, serialization-safe.

---

**Q16. What is the Builder pattern?**

**A:** Builder separates the construction of a complex object from its representation, enabling step-by-step construction. Preferred when a constructor has many parameters (especially optional ones). Makes code readable and avoids telescoping constructors.

```java
User user = new User.Builder("John", "Doe")
    .email("john@example.com")
    .age(30)
    .build();
```

---

**Q17. What is the difference between `String`, `StringBuilder`, and `StringBuffer`?**

**A:**
- `String`: Immutable. Each concatenation creates a new object. Safe for concurrent access.
- `StringBuilder`: Mutable. Not thread-safe. Faster for single-threaded string building.
- `StringBuffer`: Mutable. Thread-safe (synchronized methods). Slower than `StringBuilder`. Use in multi-threaded contexts where a shared buffer is mutated.

---

## Generics

**Q18. What is type erasure in Java generics?**

**A:** Java generics are compile-time only. At runtime, generic type parameters are erased (replaced with `Object` or the upper bound). This maintains backward compatibility with pre-generics code. Consequence: you cannot do `new T()`, `instanceof T`, or create arrays of `T` at runtime.

---

**Q19. What is the difference between `List<?>`, `List<? extends T>`, and `List<? super T>`?**

**A:**
- `List<?>`: Unknown type. Can read as `Object`, cannot add (except null). Wildcard.
- `List<? extends T>`: Upper-bounded wildcard. Can read as T, cannot add (producer). "Producer Extends" (PECS).
- `List<? super T>`: Lower-bounded wildcard. Can add T or subtypes, read as Object. "Consumer Super" (PECS).

---

**Q20. What is reflection in Java and what are the risks?**

**A:** Reflection allows inspecting and manipulating classes, methods, and fields at runtime without knowing them at compile time. Uses: frameworks (Spring, Hibernate), testing, serialization.

Risks: bypasses compile-time safety checks, slower than direct calls, can break encapsulation (access private fields), security vulnerabilities, and makes code harder to reason about. Use sparingly and prefer alternatives when possible.

---
