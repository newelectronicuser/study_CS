# Java Generics — Interview Notes 🧬

## 1. Introduction
**Generics** (Java 5+) let you write type-safe, reusable code by parameterising classes, interfaces, and methods with **type arguments**.

### Key Terminology
| Term | Meaning | Example |
| :--- | :--- | :--- |
| **Generics** | The overall language feature | — |
| **Generic Class** | Class with type parameter(s) | `class Box<T>` |
| **Generic Method** | Method with its own type parameter | `<T> T identity(T t)` |
| **Parameterised Type** | Instantiation with a concrete type | `Box<String>` |
| **Type Parameter** | Placeholder declared at definition | `T`, `E`, `K`, `V` |
| **Type Argument** | Concrete type supplied at use | `String` in `Box<String>` |
| **Raw Type** | Generic type used without type arg | `Box` (pre-Java 5 style) |
| **Type Erasure** | Removal of type info at compile time | `Box<String>` → `Box` at runtime |

**Primary Goal**: Move type errors from **runtime** to **compile time**.

---

## 2. The Need for Generics

```java
// Pre-Generics — raw type, no type safety
List list = new ArrayList();
list.add("Hello");
list.add(42);                   // accidentally added an int — compiler silent!

String s = (String) list.get(0); // manual cast required
String bad = (String) list.get(1); // ClassCastException at RUNTIME ← bug discovered too late
```

```java
// With Generics — compile-time safety
List<String> list = new ArrayList<>();
list.add("Hello");
// list.add(42);               // ← COMPILE ERROR — caught immediately

String s = list.get(0);        // no cast needed
```

---

## 3. Generic Classes

```java
// T is a type parameter — placeholder for any reference type
public class Box<T> {
    private T content;

    public void set(T content) { this.content = content; }
    public T get()             { return content; }

    @Override
    public String toString()   { return "Box[" + content + "]"; }
}

// Usage
Box<String>  strBox = new Box<>();
strBox.set("Hello");
String s = strBox.get();         // no cast — compiler knows it's a String

Box<Integer> intBox = new Box<>();
intBox.set(42);
int n = intBox.get();            // autoboxed

// Diamond operator <> — type inferred from the left side (Java 7+)
Box<Double> dBox = new Box<>();  // Java infers <Double>
```

> [!NOTE]
> **Common type parameter conventions**: `T` (Type), `E` (Element in collections), `K` (Key), `V` (Value), `N` (Number), `R` (Return type).

---

## 4. Multiple Type Parameters

```java
public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) { this.key = key; this.value = value; }
    public K getKey()   { return key; }
    public V getValue() { return value; }

    @Override
    public String toString() { return key + "=" + value; }
}

Pair<String, Integer> score = new Pair<>("Alice", 95);
System.out.println(score.getKey() + ": " + score.getValue()); // Alice: 95

// Map.Entry<K,V> is the most common two-parameter generic type in Java
Map<String, Integer> map = new HashMap<>();
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " → " + entry.getValue());
}
```

---

## 5. Generic Methods
A method can introduce its own type parameters, independent of the class's type parameters.

```java
// Static generic method — type inferred at call site
public static <T> void printArray(T[] arr) {
    for (T item : arr) System.out.print(item + " ");
    System.out.println();
}

// Type parameter appears before return type
public static <T> T identity(T value) { return value; }

// Generic method in a non-generic class
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}

// Usage — type argument inferred automatically
Integer[] ints = {3, 1, 4, 1, 5};
printArray(ints);                    // <Integer> inferred
System.out.println(max("apple", "cherry")); // cherry
System.out.println(max(10, 25));           // 25

// Explicit type argument (rarely needed)
ExceptionsDemo.<String>identity("hello");
```

---

## 6. Generics and Primitive Types
Generics work with **reference types only**. Primitives must use their **wrapper classes**.

```java
// Box<int>    // ← COMPILE ERROR — primitives not allowed

Box<Integer> intBox = new Box<>();
intBox.set(42);       // autoboxing: int → Integer
int val = intBox.get(); // unboxing: Integer → int
```

**Why?** Generics are implemented via **Type Erasure** — type parameters become `Object`. Since primitives (`int`, `double`, etc.) don't extend `Object`, they can't be used.

---

## 7. Bounded Type Parameters

### Upper Bound — `extends`
Restricts `T` to a type **or any of its subtypes**.

```java
// T must be a Number or subclass (Integer, Double, Long …)
public class NumberBox<T extends Number> {
    private T value;
    public NumberBox(T value) { this.value = value; }

    // Can call Number methods because T is guaranteed to be a Number
    public double doubleValue() { return value.doubleValue(); }
}

NumberBox<Integer> iBox = new NumberBox<>(42);
NumberBox<Double>  dBox = new NumberBox<>(3.14);
// NumberBox<String> sBox = new NumberBox<>("hi"); // ← COMPILE ERROR

// Generic method with upper bound
public static <T extends Number> double sum(List<T> list) {
    double total = 0;
    for (T n : list) total += n.doubleValue();
    return total;
}
```

### Multiple Bounds
```java
// Must satisfy ALL bounds; class bound must come FIRST
public <T extends Comparable<T> & Cloneable> T findMin(List<T> list) {
    return list.stream().min(Comparator.naturalOrder()).orElseThrow();
}
```

---

## 8. Type Erasure — Critical Interview Topic

The Java compiler **erases** all generic type information after type-checking:

1. Replaces type parameters with their bound (`Object` if unbounded, or the bound type).
2. Inserts casts where needed.
3. Generates **bridge methods** to preserve polymorphism.

```java
// Source code
public class Box<T> {
    private T content;
    public T get() { return content; }
}

// After erasure (what the JVM actually sees)
public class Box {
    private Object content;
    public Object get() { return content; }
}
```

**Consequences of erasure**:

```java
// At runtime, these are the SAME class
List<String>  ls = new ArrayList<>();
List<Integer> li = new ArrayList<>();
System.out.println(ls.getClass() == li.getClass()); // true — both are ArrayList

// CANNOT do instanceof with parameterised type
// if (ls instanceof List<String>) { }  // ← COMPILE ERROR

// Arrays of generic types are NOT allowed (array type must be known at runtime)
// T[] arr = new T[10];                 // ← COMPILE ERROR

// Workaround
@SuppressWarnings("unchecked")
T[] arr = (T[]) new Object[10];         // common pattern — safe if you control usage
```

---

## 9. Generic Classes and Inheritance — Invariance

> [!IMPORTANT]
> **`List<String>` is NOT a subtype of `List<Object>`** — even though `String extends Object`.

```java
// This is correct — String IS-A Object
Object obj = "hello"; // ✅ OK

// This is ILLEGAL
List<Object> objects = new ArrayList<String>(); // ← COMPILE ERROR

// Why? If it were allowed:
// objects.add(42);            // would corrupt the String list!
// String s = strings.get(0);  // ClassCastException at runtime
```

This property is called **Invariance** — generic types are invariant by default.

| Relationship | Array | Generic |
| :--- | :--- | :--- |
| `String[]` → `Object[]` | ✅ Covariant (allowed) | — |
| `List<String>` → `List<Object>` | — | ❌ Invariant (NOT allowed) |

> [!NOTE]
> Arrays **are** covariant, which allowed bugs — `Object[] arr = new String[5]; arr[0] = 42;` causes `ArrayStoreException` at runtime. Generics fix this at compile time.

---

## 10. Wildcards (`?`)
Wildcards loosen generic type restrictions. Used primarily in **method parameters** — not in variable declarations.

### A. Upper Bounded Wildcard — `? extends T` (Producer / Read-only)

```java
// Accepts List<Integer>, List<Double>, List<Number> — any subtype of Number
public static double sumList(List<? extends Number> list) {
    double sum = 0;
    for (Number n : list) sum += n.doubleValue(); // read as Number — safe
    return sum;
}

sumList(List.of(1, 2, 3));         // List<Integer> ✅
sumList(List.of(1.1, 2.2, 3.3));  // List<Double>  ✅

// Cannot add to an upper-bounded wildcard list — type is unknown at compile time
// list.add(42); // ← COMPILE ERROR — compiler can't verify 42 is safe
```

### B. Lower Bounded Wildcard — `? super T` (Consumer / Write-only)

```java
// Accepts List<Integer>, List<Number>, List<Object> — any supertype of Integer
public static void fillIntegers(List<? super Integer> list, int count) {
    for (int i = 0; i < count; i++) list.add(i); // safe — list accepts at least Integer
}

List<Number> nums = new ArrayList<>();
fillIntegers(nums, 5); // ✅
List<Object> objs = new ArrayList<>();
fillIntegers(objs, 3); // ✅

// Can only read as Object from a lower-bounded wildcard
// Number n = list.get(0); // ← COMPILE ERROR — might be Object, might be Number
Object o = list.get(0); // only safe as Object
```

### C. Unbounded Wildcard — `?`

```java
// Accepts any parameterised type — useful for printing / type-agnostic operations
public static void printList(List<?> list) {
    for (Object o : list) System.out.print(o + " ");
    System.out.println();
}

printList(List.of(1, 2, 3));      // ✅
printList(List.of("a", "b"));     // ✅
```

### PECS Principle — Producer Extends, Consumer Super
> **P**roducer **E**xtends — use `? extends T` when you only **read** from a collection.<br>
> **C**onsumer **S**uper — use `? super T` when you only **write into** a collection.

```java
// Classic Collections.copy() — uses both
public static <T> void copy(List<? super T> dest,    // consumer — writes
                             List<? extends T> src) { // producer — reads
    for (T t : src) dest.add(t);
}
```

---

## 11. The `Comparable<T>` Interface with Generics
The most common use of bounded generics in interviews.

```java
// findMax works for any type that can compare itself
public static <T extends Comparable<T>> T findMax(List<T> list) {
    if (list.isEmpty()) throw new IllegalArgumentException("empty list");
    T max = list.get(0);
    for (T item : list)
        if (item.compareTo(max) > 0) max = item;
    return max;
}

System.out.println(findMax(List.of(3, 1, 4, 1, 5, 9))); // 9
System.out.println(findMax(List.of("banana", "apple", "cherry"))); // cherry

// Generic sort — T must be Comparable
public static <T extends Comparable<T>> void bubbleSort(T[] arr) {
    for (int i = 0; i < arr.length - 1; i++)
        for (int j = 0; j < arr.length - 1 - i; j++)
            if (arr[j].compareTo(arr[j + 1]) > 0) {
                T tmp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = tmp;
            }
}
```

---

## 12. Generic Interfaces

```java
// Generic interface
public interface Repository<T, ID> {
    T findById(ID id);
    void save(T entity);
    List<T> findAll();
}

// Implementation fixes the type parameters
public class UserRepository implements Repository<User, Integer> {
    @Override public User findById(Integer id) { /* DB call */ return null; }
    @Override public void save(User user) { /* insert */ }
    @Override public List<User> findAll() { return List.of(); }
}
```

---

## 13. Common Interview Gotchas

```java
// 1. Cannot instantiate a type parameter
// T obj = new T(); // ← COMPILE ERROR (type is erased at runtime)
// Use a Supplier<T> instead:
public static <T> T create(java.util.function.Supplier<T> factory) {
    return factory.get();
}
String s = create(String::new); // ✅

// 2. Cannot use instanceof with parameterised type
// if (obj instanceof List<String>) // ← COMPILE ERROR
if (obj instanceof List<?>) { }     // ✅ — unbounded wildcard is OK

// 3. Static fields/methods cannot use instance type parameters
public class Box<T> {
    // private static T instance; // ← COMPILE ERROR — T belongs to instance, not class
    private static Object instance;  // use Object or a separate static generic method
}

// 4. Generic array creation is forbidden
// T[] arr = new T[10]; // ← COMPILE ERROR
@SuppressWarnings("unchecked")
T[] arr = (T[]) new Object[10]; // ← common workaround

// 5. Overloading with type erasure — these are the SAME signature after erasure
// public void process(List<String> list) { }
// public void process(List<Integer> list) { } // ← COMPILE ERROR — duplicate

// 6. Checked exceptions and generics — can't throw generic type directly
// public <T extends Exception> void doSomething() throws T { } // works, but niche
```

---

## 14. Summary Table

| Feature | Syntax | Purpose |
| :--- | :--- | :--- |
| Generic class | `class Box<T>` | Type-safe reusable container |
| Generic method | `<T> T identity(T t)` | Type-safe reusable operation |
| Upper bound | `T extends Number` | Restrict T to Number subtypes |
| Multiple bounds | `T extends A & B` | Satisfy multiple constraints |
| Wildcard (any) | `List<?>` | Accept any parameterised list |
| Upper wildcard | `List<? extends T>` | Read-only, accepts subtypes |
| Lower wildcard | `List<? super T>` | Write-only, accepts supertypes |
| PECS | extends=read, super=write | Choose wildcard direction |
| Type erasure | `Box<String>` → `Box` | Runtime has no generic info |
| Invariance | `List<String> ≠ List<Object>` | Generics are not covariant |
| Diamond `<>` | `new Box<>()` | Type inference (Java 7+) |

> [!IMPORTANT]
> **Key Interview Rules**:
> 1. `List<String>` is **not** a subtype of `List<Object>` — generics are **invariant**.
> 2. Generic type info is **erased** at runtime — `List<String>` and `List<Integer>` are both `List`.
> 3. Cannot do `new T()`, `new T[]`, or `instanceof List<String>` — all type-erased.
> 4. Use `? extends T` to **read** (producer); `? super T` to **write** (consumer) — **PECS**.
> 5. Static members cannot reference the class's instance type parameter.
> 6. Multiple bounds: class bound comes **first** — `<T extends MyClass & InterfaceA & InterfaceB>`.
> 7. Generics only work with reference types — use wrapper classes for primitives.
