# Java — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## OOP & Language Fundamentals

**Q1. What are the four pillars of OOP? Give Java examples for each.**

**A:**
*   **Encapsulation:** Hiding internal state by making fields `private` and exposing access via `public` getters/setters.
*   **Inheritance:** Reusing code by extending a parent class. `class Dog extends Animal { ... }`
*   **Polymorphism:** The ability of an object to take many forms.
    *   *Compile-time:* Method overloading.
    *   *Runtime:* Method overriding, where a parent reference points to a child object: `List<String> list = new ArrayList<>();`.
*   **Abstraction:** Hiding complex implementation details and exposing only the essential interface. Done via `abstract class` or `interface`.

---

**Q2. What is the difference between an abstract class and an interface in Java?**

**A:**
*   **Abstract Class:** Can have state (instance variables), constructors, private/protected/public methods, and multiple inheritance is not supported (a class can only `extend` one class).
*   **Interface:** Prior to Java 8, could only have `public static final` constants and abstract methods. Java 8 added `default` and `static` methods. Java 9 added `private` methods. A class can implement multiple interfaces. Interfaces cannot hold instance state (variables).

---

**Q3. When would you use an abstract class over an interface?**

**A:** Use an **abstract class** when you want to share a common, stateful implementation among closely related classes (e.g., sharing common helper methods, private fields, and constructors). Use an **interface** when defining a common contract or capability (like `Runnable` or `Serializable`) that can be implemented by entirely unrelated classes.

---

**Q4. What is method overloading vs method overriding? What rules apply?**

**A:**
*   **Method Overloading:** Same method name, different parameter signature (type, number, or order) within the same class. It is compile-time polymorphism. Returing type can be different.
*   **Method Overriding:** Redefining a parent class method in the child class with the exact same name, parameters, and return type. It is runtime polymorphism.
    *   *Rules for Overriding:* Access level cannot be more restrictive than the parent's. Cannot override static/final methods. Return type can be covariant (subtype of parent's return type). Cannot throw broader checked exceptions.

---

**Q5. What is the difference between `==` and `.equals()` in Java?**

**A:**
*   `==`: Compares reference addresses in memory for object references (checks if they point to the exact same object). For primitives, it compares actual values.
*   `.equals()`: A method in `java.lang.Object` that, by default, behaves like `==`. However, classes like `String`, `Integer`, and `Double` override it to compare the logical values/contents rather than reference addresses.

---

**Q6. What is the `hashCode()` contract? Why must you override `hashCode()` when you override `equals()`?**

**A:** The contract states:
1. If two objects are equal according to `.equals()`, they must return the same `hashCode()`.
2. If two objects have the same `hashCode()`, they are *not* required to be equal (hash collision).

*Why override:* Hash-based collections (like `HashMap`, `HashSet`) use `hashCode()` to find the bucket index for storing/retrieving objects. If you override `equals()` but not `hashCode()`, two logically equal objects will have different hashes, meaning they will be placed in different buckets and the collection won't be able to retrieve them correctly.

---

**Q7. What is the difference between `final`, `finally`, and `finalize()`?**

**A:**
*   `final`: Access modifier. Applied to a class (cannot be subclassed), method (cannot be overridden), or variable (value cannot be changed once assigned/made constant).
*   `finally`: A block used in try-catch-finally statements to execute cleanup code. It executes regardless of whether an exception is thrown or caught.
*   `finalize()`: A protected method on `Object` called by the Garbage Collector before reclaiming memory. It is deprecated since Java 9 because it is unreliable and can lead to performance issues.

---

**Q8. What is a static initializer block? When is it executed?**

**A:** A static initializer block is a block of code marked with `static { ... }` in a class. It is used to initialize static variables. It executes exactly once when the JVM loads the class into memory (prior to object creation or static method access).

---

**Q9. What is the difference between `static` and `instance` methods?**

**A:**
*   **Static Methods:** Belong to the class itself. Can be called without instantiating the class (e.g. `Math.abs()`). Can only access other static variables/methods; cannot use `this` or `super`.
*   **Instance Methods:** Belong to instances (objects) of the class. Require object instantiation to be called. Can access both static and instance variables/methods.

---

**Q10. What are access modifiers in Java? Explain `public`, `protected`, `package-private`, and `private`.**

**A:**
*   `public`: Accessible from any class in any package.
*   `protected`: Accessible within the same package, and by subclasses in other packages.
*   *Package-private (default, no keyword):* Accessible only within the same package.
*   `private`: Accessible only within the declaring class.

---

## Collections Framework

**Q11. What is the difference between `ArrayList` and `LinkedList`?**

**A:**
*   `ArrayList`: Backed by a dynamic array. Fast search/read operations ($O(1)$ by index). Slow insert/delete ($O(n)$ because it requires shifting elements) when inserting in the middle. Requires resizing once capacity is filled.
*   `LinkedList`: Backed by a doubly linked list. Slow search/read operations ($O(n)$ since it must traverse nodes). Fast insert/delete ($O(1)$ once the node reference is found). Consumes more memory per element due to forward/backward pointers.

---

**Q12. What is the difference between `HashMap`, `LinkedHashMap`, and `TreeMap`?**

**A:**
*   `HashMap`: Offers $O(1)$ average time complexity for read/write. No order guarantees.
*   `LinkedHashMap`: Extends `HashMap` with a doubly linked list. Maintains the **insertion order** (or access order). Slightly more memory overhead.
*   `TreeMap`: Backed by a Red-Black Tree. Maintains keys in **natural sorted order** (or custom comparator). Offers $O(\log n)$ performance.

---

**Q13. How does a `HashMap` work internally? What is hashing collision resolution?**

**A:** A `HashMap` uses an array of buckets. When calling `put(key, value)`:
1.  It calculates the `key.hashCode()`, applies a hash function, and uses modulo arithmetic to find the array bucket index.
2.  If the bucket is empty, it inserts a new Node.
3.  **Collision Resolution:** If the bucket already contains nodes (hash collision), it uses a linked list to chain the nodes. Since Java 8, if the linked list size exceeds a threshold (8) and total map capacity is $\geq 64$, the bucket transitions from a linked list to a **Red-Black Tree** to improve search time from $O(n)$ to $O(\log n)$.

---

**Q14. What is the difference between `HashSet` and `TreeSet`?**

**A:**
*   `HashSet`: Backed internally by a `HashMap` (using elements as keys and a dummy object as value). Offers $O(1)$ performance. No order guarantees.
*   `TreeSet`: Backed internally by a `TreeMap` (using a Red-Black Tree). Offers $O(\log n)$ performance. Maintains elements in sorted order.

---

**Q15. What is a `ConcurrentHashMap` and how does it differ from `HashMap`?**

**A:** `ConcurrentHashMap` is thread-safe and designed for high-concurrency environments.
*   *Differences:*
    *   `HashMap` is not thread-safe.
    *   `Hashtable` or `Collections.synchronizedMap()` lock the entire map, blocking all readers and writers.
    *   `ConcurrentHashMap` uses bucket-level locks (synchronizing on the head node of buckets) and CAS (Compare-And-Swap) operations. This allows multiple threads to read and write concurrently to different buckets without blocking each other. It does not allow `null` keys or values.

---

**Q16. What is fail-fast vs fail-safe iterators?**

**A:**
*   **Fail-Fast Iterators:** Directly operate on the collection's structure. If the collection is modified while iterating (by adding/removing elements outside the iterator's own methods), it immediately throws `ConcurrentModificationException`. (e.g., iterators of `ArrayList`, `HashSet`).
*   **Fail-Safe (Weakly Consistent) Iterators:** Operate on a clone or snapshot of the collection (or use concurrent structures). They allow modifications during iteration without throwing exceptions. (e.g., iterators of `CopyOnWriteArrayList`, `ConcurrentHashMap`).

---

**Q17. What is the difference between `Comparable` and `Comparator`?**

**A:**
*   `Comparable`: Defines the natural ordering of objects. The class itself implements `Comparable<T>` and overrides the `compareTo(T o)` method. Modifies the class.
*   `Comparator`: Defines a custom ordering. Created as a separate class or lambda implementing `Comparator<T>` and overriding `compare(T o1, T o2)`. Does not require modifying the target class.

---

**Q18. When would you use a `PriorityQueue`?**

**A:** Use a `PriorityQueue` when you need to retrieve elements based on priority rather than FIFO order. It is backed by a binary heap, where the head of the queue is always the smallest (min-heap) or largest (max-heap) element. Useful for algorithms like Dijkstra's shortest path, Prim's minimum spanning tree, or job scheduling systems.

---

**Q19. What is the difference between `Stack` and `Deque` in Java?**

**A:**
*   `Stack`: A legacy, synchronized class that extends `Vector`. Thread-safe but slow due to lock overhead.
*   `Deque` (Double Ended Queue): An interface (implemented by `ArrayDeque` or `LinkedList`) supporting element insertion/removal on both ends. It is unsynchronized, faster, and the preferred way to implement a stack (LIFO) or queue (FIFO) in modern Java.

---

**Q20. What is `Collections.unmodifiableList()` vs `List.copyOf()`?**

**A:**
*   `Collections.unmodifiableList(list)`: Returns a read-only **wrapper view** of the source list. If the original list is modified, those changes are reflected in the unmodifiable list.
*   `List.copyOf(collection)` (Java 10+): Returns an independent, **immutable copy** of the collection. Modifications to the source collection have no effect on the returned list.

---

## Memory Management & JVM

**Q21. What is the JVM architecture? Explain the major memory areas.**

**A:** The JVM consists of the ClassLoader subsystem, Execution Engine, and Runtime Data Areas:
*   **Method Area / Metaspace:** Stores class structure, metadata, static variables, and constant pool. Since Java 8, it resides in native memory (Metaspace) rather than JVM heap.
*   **JVM Heap:** Shared memory containing all objects and arrays. Divided into Young and Old generations.
*   **JVM Stacks:** Per-thread allocation. Stores local variables and stack frames for method calls.
*   **PC Registers:** Tracks the current execution instruction address for each thread.
*   **Native Method Stacks:** Holds state for JNI/native methods.

---

**Q22. What is the difference between heap and stack memory in Java?**

**A:**
*   **Heap Memory:** Shared across all threads. Used for dynamic allocation of all objects. Managed by the Garbage Collector. Lives as long as there is an active reference.
*   **Stack Memory:** Private to each thread. Used to store local variables, primitive values, and references to objects on the heap. Managed automatically on method call/return (LIFO). Fast access, small size limit (can throw `StackOverflowError`).

---

**Q23. How does garbage collection work in Java? Explain the generational GC model.**

**A:** Garbage Collection identifies and reclaims unreachable objects. The **Generational model** groups objects by age:
*   **Young Generation:** Where all new objects are allocated. Divided into **Eden** and two **Survivor** spaces (S0, S1). Most objects die young. Minor GC runs here frequently, copying survivors between S0/S1.
*   **Old Generation (Tenured):** Long-lived objects are promoted here after surviving a threshold number of minor GCs. Full GC runs here less frequently because scanning the entire Old Gen is slow.

---

**Q24. What are the different GC algorithms available in the JVM (G1, ZGC, Shenandoah, etc.)?**

**A:**
*   **G1 (Garbage First):** Default since Java 9. Divides the heap into equal-sized regions and collects regions with the most garbage first. Focuses on low, predictable pause times.
*   **ZGC (Z Garbage Collector):** Ultra-low latency collector (sub-millisecond pause times) designed for terabyte-scale heaps. Performs garbage collection concurrently with application threads.
*   **Shenandoah:** Similar to ZGC; performs evacuation concurrently with application threads to reduce pauses to milliseconds.
*   **Parallel GC:** Maximizes throughput by using multiple threads for GC, but stops application execution completely (Stop-The-World).

---

**Q25. What is a memory leak in Java? Give an example.**

**A:** A memory leak occurs when objects that are no longer needed by the application are still referenced, preventing the Garbage Collector from reclaiming their memory. Over time, this leads to `OutOfMemoryError`.
*   *Example:* Adding items to a static collection and never removing them.

```java
public class Cache {
    private static final List<Object> leak = new ArrayList<>();
    public static void add(Object obj) {
        leak.add(obj); // Will never be garbage collected because static list lives forever
    }
}
```

---

**Q26. What is the `WeakReference`, `SoftReference`, and `PhantomReference`?**

**A:**
*   **SoftReference:** Kept in memory unless the JVM is running out of memory (typically used for memory-sensitive caches).
*   **WeakReference:** Cleared by the GC immediately on the next garbage collection cycle if no strong references to the object exist. Useful for metadata maps (`WeakHashMap`).
*   **PhantomReference:** Used for cleanup operations after the object is finalized (replaces `finalize()`). Must be used with a `ReferenceQueue`.

---

**Q27. What is class loading? Explain parent delegation model.**

**A:** Class loading is the process by which the JVM loads `.class` bytecode files into memory. The **Parent Delegation Model** dictates that when a ClassLoader needs to load a class:
1.  It delegates the request to its parent ClassLoader first.
2.  This goes up to the **Bootstrap ClassLoader**.
3.  If the parent cannot find the class, the child ClassLoader attempts to load it. This prevents malicious class overrides (e.g. attempting to override `java.lang.String`).

---

**Q28. How do you tune JVM heap size in a production environment?**

**A:** Set JVM flags at startup:
*   `-Xms<size>`: Set initial heap size.
*   `-Xmx<size>`: Set maximum heap size. For production, setting `-Xms` equal to `-Xmx` prevents latency spikes caused by heap resizing.
*   `-XX:MetaspaceSize` and `-XX:MaxMetaspaceSize` to configure class metadata limits.
*   Use monitoring tools (JConsole, VisualVM, Prometheus JVM exporter) to analyze GC pauses and allocation rates before tuning.

---

## Exception Handling

**Q29. What is the difference between checked and unchecked exceptions?**

**A:**
*   **Checked Exceptions:** Classes extending `Exception` (excluding `RuntimeException`). The compiler forces the developer to handle them using `try-catch` or declare them using `throws`. Represent external conditions (e.g., `IOException`, `SQLException`).
*   **Unchecked Exceptions:** Classes extending `RuntimeException` or `Error`. The compiler does not require explicit handling. Represent programming errors (e.g., `NullPointerException`, `IndexOutOfBoundsException`).

---

**Q30. What is the exception hierarchy in Java?**

**A:** All exceptions stem from the `Throwable` class:
*   `Throwable`
    *   `Error`: Serious system-level failures that applications should not try to catch (e.g. `OutOfMemoryError`, `StackOverflowError`).
    *   `Exception`:
        *   *Checked Exceptions* (e.g. `IOException`).
        *   `RuntimeException` (Unchecked Exceptions, e.g. `NullPointerException`).

---

**Q31. What is the try-with-resources statement and how does it work?**

**A:** It is a try statement that declares one or more resources (like file streams, database connections). The resources must implement the `java.lang.AutoCloseable` or `java.io.Closeable` interface. The JVM automatically closes these resources at the end of the block (even if exceptions occur), eliminating the need for boilerplate `finally` blocks.

---

**Q32. What is the difference between `throw` and `throws`?**

**A:**
*   `throw`: Keyword used inside a method body to explicitly throw a single exception instance. `throw new IllegalArgumentException();`
*   `throws`: Keyword used in a method signature to declare which exceptions the method might throw to its callers. `public void readFile() throws IOException { ... }`

---

**Q33. What is a multi-catch block (`catch (ExA | ExB e)`)?**

**A:** Introduced in Java 7, it allows catching multiple unrelated exceptions in a single `catch` block. The exception variable `e` is implicitly `final`, meaning you cannot reassign it inside the block.

```java
try {
    // operations
} catch (IOException | SQLException e) {
    logger.error("Failed to execute task", e);
}
```

---

**Q34. When should you create a custom exception class?**

**A:** Create a custom exception when the standard JDK exceptions (like `IllegalArgumentException` or `IllegalStateException`) do not capture the business-specific domain failure, or when you need to attach specific error codes, metadata, or recovery instructions to the exception payload for upstream handlers.

---

## Design Patterns

**Q35. What is the Singleton pattern? How do you make it thread-safe?**

**A:** Singleton ensures a class has only one instance and provides a global access point. Thread-safe implementations:
*   **Double-Checked Locking (Lazy Initialization):**
```java
public class Singleton {
    private static volatile Singleton instance; // volatile is crucial!
    private Singleton() {}
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```
*   **Bill Pugh Holder (Preferred):** Uses static inner helper class (lazy and thread-safe without synchronization).
```java
public class Singleton {
    private Singleton() {}
    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }
    public static Singleton getInstance() { return Holder.INSTANCE; }
}
```

---

**Q36. What is the Builder pattern? When is it preferred over constructors?**

**A:** The Builder pattern separates the construction of a complex object from its representation.
*   *Preferred over constructors* when a class has many fields, particularly when many of those fields are optional. It prevents "telescoping constructor" anti-patterns and ensures the object is immutable once built.

---

**Q37. What is the Factory Method vs Abstract Factory pattern?**

**A:**
*   **Factory Method:** Defines an interface for creating a single object, but lets subclasses decide which class to instantiate (polymorphic object creation).
*   **Abstract Factory:** Provides an interface for creating families of related or dependent objects without specifying their concrete classes (e.g. creating UI components like buttons and textfields for either Windows or macOS theme).

---

**Q38. What is the Observer pattern? How is it implemented in Java?**

**A:** Defines a one-to-many dependency between objects, so that when one object changes state, all its dependents (observers) are notified automatically. In modern Java, it is implemented using custom listeners, event listener frameworks (like Spring Event Publisher), or reactive programming classes (`Flow.Publisher` and `Flow.Subscriber` in Java 9+).

---

**Q39. What is the Strategy pattern? Give a use case.**

**A:** Defines a family of algorithms, encapsulates each one, and makes them interchangeable. It allows the algorithm to vary independently from the clients that use it.
*   *Use Case:* Implementing payment processing where a user can select different payment methods (Credit Card, PayPal, Crypto). Each payment method implements a common `PaymentStrategy` interface.

---

**Q40. What is the Decorator pattern? How does it differ from inheritance?**

**A:**
*   **Decorator Pattern:** Dynamically attaches additional responsibilities to an object at runtime by wrapping it. (e.g. wrapping a `FileInputStream` with a `BufferedInputStream`). It favors composition over inheritance.
*   *Difference:* Inheritance extends a class's behavior statically at compile time. Decorator expands behavior dynamically at runtime without affecting other instances of the same class.

---

**Q41. What is dependency injection and how does it relate to the IoC principle?**

**A:**
*   **IoC (Inversion of Control):** A design principle where the control flow of a program is inverted — instead of code orchestrating dependency creation, an external framework handles execution flow.
*   **Dependency Injection (DI):** A concrete pattern implementing IoC. Instead of a class instantiating its dependencies internally, the dependencies are "injected" from the outside (via constructor or setter methods) by an IoC container (e.g. Spring).

---

## Generics & Type System

**Q42. What are Java generics and why are they used?**

**A:** Generics enable types (classes and methods) to be parameterized. They provide **compile-time type safety**, eliminating the need for explicit type casting and catching type errors at compile time rather than throwing `ClassCastException` at runtime.

---

**Q43. What is type erasure in Java generics?**

**A:** Type erasure means that generic type parameters are used only for compile-time safety checks. Once code compiles, the JVM replaces all generic type parameters with their bound (e.g., `Object` or the upper bound). Consequently, generic type information is **not** available at runtime (you cannot do `new T()` or `instanceof T`).

---

**Q44. What is the difference between `List<?>`, `List<? extends T>`, and `List<? super T>`?**

**A:**
*   `List<?>` (Unbounded wildcard): Represents a list of an unknown type. Read operations return `Object`. Cannot add any elements to this list (except `null`).
*   `List<? extends T>` (Upper-bound wildcard): Represents a list of T or subtypes of T. You can read elements as type T. Cannot add elements to this list (read-only/Producer).
*   `List<? super T>` (Lower-bound wildcard): Represents a list of T or supertypes of T. You can safely add elements of type T or its subtypes (Writer/Consumer).
*   *Rule of thumb:* PECS (Producer Extends, Consumer Super).

---

**Q45. What is a bounded type parameter?**

**A:** A bounded type parameter restricts the types that can be used as arguments in a generic class/method. For example, `<T extends Number>` restricts T to `Number` or its subclasses (like `Integer`, `Double`).

---

## Advanced Java

**Q46. What is reflection in Java? What are the risks of using it?**

**A:** Reflection allows inspecting and modifying classes, methods, fields, and constructors at runtime.
*   *Risks:* Performance overhead (bypasses JIT optimization); security vulnerabilities (can bypass `private` access modifiers via `setAccessible(true)`); breaks compile-time type safety; breaks code maintainability if internal class names change.

---

**Q47. What is the difference between serialization and deserialization?**

**A:**
*   **Serialization:** Converting an object's state into a byte stream (so it can be written to disk, databases, or sent over a network). Done by implementing `java.io.Serializable`.
*   **Deserialization:** Reconstructing the object state from a byte stream back into a live Java object.

---

**Q48. What is `transient` keyword used for?**

**A:** The `transient` keyword is applied to variables to prevent them from being serialized. When an object is serialized, `transient` fields are skipped, and their default values (e.g., `null` for objects, `0` for numeric primitives) are assigned upon deserialization. Useful for sensitive data (passwords) or runtime-only state (threads, db connections).

---

**Q49. What are annotations in Java? How do you create a custom annotation?**

**A:** Annotations are a form of metadata metadata that can be applied to classes, methods, fields, etc. They do not directly affect program execution but can be processed by compilers or runtime frameworks.
*   *Creating a custom annotation:*
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyCustomAnnotation {
    String value() default "Default Value";
}
```

---

**Q50. What is the difference between `String`, `StringBuilder`, and `StringBuffer`?**

**A:**
*   `String`: Immutable. Operations (like concatenation) create new String objects. Safe for concurrency.
*   `StringBuilder`: Mutable. Not thread-safe (unsynchronized methods). Fast; the preferred choice for single-threaded string manipulations.
*   `StringBuffer`: Mutable. Thread-safe (synchronized methods). Slower due to lock overhead.

---

**Q51. How does the `String` pool (intern pool) work?**

**A:** The String Pool is a storage area in the JVM Heap. When you create a String literal (e.g. `String s = "Hello";`), the JVM checks the pool. If it already exists, the reference is shared; if not, a new string is added to the pool. Creating a string using `new String("Hello")` forces the creation of a new object on the heap outside the pool. Calling `s.intern()` adds a heap-created string to the pool.

---

**Q52. What is the difference between an inner class, a static nested class, and an anonymous class?**

**A:**
*   **Inner Class:** Non-static class defined inside an outer class. Holds an implicit reference to the outer class instance and can access its private members.
*   **Static Nested Class:** Static class defined inside an outer class. Cannot access non-static members of the outer class directly. Does not hold a reference to an outer class instance.
*   **Anonymous Class:** An inner class declared and instantiated in a single expression without a name. Used for single-use implementations (e.g. creating runtime event listeners or comparators).

---
