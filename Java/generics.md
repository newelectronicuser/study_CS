# Java Generics - Interview Notes 🧬

## 1. Introduction
**Generics** were introduced in Java 5 to provide **stronger type checks at compile time** and to support generic programming. They allow types (classes, interfaces, and methods) to be parameters when defining them.

- **Primary Goal**: Type Safety.
- **Key Benefit**: Eliminates the need for explicit casting and prevents `ClassCastException` at runtime.

---

## 2. The Need for Generics
Before Generics, collections could store any `Object`. This led to two major issues:
1.  **No Type Safety**: You could accidentally add an `Integer` to a list of `String`.
2.  **Explicit Casting**: You had to manually cast objects when retrieving them.

```java
// Pre-Generics
List list = new ArrayList();
list.add("Hello");
String s = (String) list.get(0); // Manual cast required
```

---

## 3. A Poor Solution (Object-based)
Using the `Object` class as a "generic" type was the standard before Java 5.
```java
public class MyList {
    private Object[] items = new Object[10];
    public void add(Object item) { ... }
    public Object get(int index) { ... }
}
```
**Why this is "poor":**
- The compiler doesn't know what's inside the list. 
- You discover type mismatches only at **Runtime**, which is too late.

---

## 4. Generic Classes
Generic classes are defined with a type parameter in angle brackets (`<>`).
```java
public class Box<T> {
    private T content;
    public void set(T content) { this.content = content; }
    public T get() { return content; }
}

// Usage
Box<String> stringBox = new Box<>();
stringBox.set("Secret");
String s = stringBox.get(); // No cast needed!
```
> [!NOTE]
> Common type parameters: `T` (Type), `E` (Element), `K` (Key), `V` (Value), `N` (Number).

---

## 5. Generics and Primitive Types
Generics **cannot** be used with primitive types (like `int`, `double`, `char`).
- **Why?** Because Java Generics are implemented using **Type Erasure**, which replaces type parameters with `Object`. Primitives do not inherit from `Object`.
- **Solution**: Use wrapper classes (`Integer`, `Double`, etc.). Java performs **Autoboxing** and **Unboxing** automatically.

---

## 6. Constraints (Bounded Type Parameters)
You can restrict the types that can be used as type arguments using the `extends` keyword.
```java
public class MathBox<T extends Number> {
    private T value;
    // T can only be Number or its subclasses (Integer, Double, etc.)
}
```
- **Multiple Bounds**: `<T extends Comparable<T> & Serializable>` (Class must come first, followed by interfaces).

---

## 7. Type Erasure
This is a critical interview topic. **Type Erasure** is the process by which the compiler:
1. Replaces all type parameters in generic types with their bounds (or `Object` if unbounded).
2. Inserts necessary casts to ensure type safety.
3. Generates bridge methods to preserve polymorphism.

**Result**: At **Runtime**, Generic information is gone. `List<String>` and `List<Integer>` both become just `List` (raw type).

---

## 8. Comparable Interface
Generics are often used with the `Comparable` interface to ensure that elements can be compared (for sorting).
```java
public <T extends Comparable<T>> T findMax(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

---

## 9. Generic Methods
Methods can have their own type parameters, independent of the class.
```java
public static <T> void printArray(T[] array) {
    for (T element : array) {
        System.out.println(element);
    }
}
```

---

## 10. Multiple Type Parameters
You can define multiple type parameters by separating them with commas.
```java
public class Pair<K, V> {
    private K key;
    private V value;
    // ...
}
```

---

## 11. Generic Classes and Inheritance
This is a common "gotcha" in interviews. 
- **Invariance**: `List<String>` is **NOT** a subclass of `List<Object>`.
- Even though `String` is an `Object`, the containers are not compatible. This prevents you from adding an `Integer` to a list that was originally intended for `String`.

---

## 12. Wildcards (`?`)
Wildcards represent an unknown type.

### A. Upper Bounded Wildcard (`? extends T`)
Allows any type that is a subtype of `T`. Used for **Reading** (Producer).
```java
public void process(List<? extends Number> list) {
    // We can read as Number, but can't add to it safely.
}
```

### B. Lower Bounded Wildcard (`? super T`)
Allows any type that is a supertype of `T`. Used for **Writing** (Consumer).
```java
public void addNumbers(List<? super Integer> list) {
    list.add(10); // Safe because the list is at least an Integer list
}
```

### C. Unbounded Wildcard (`?`)
Allows any type. Equivalent to `? extends Object`.

**PECS Principle**: 
- **P**roducer **E**xtends (Use `extends` if you only read from a collection).
- **C**onsumer **S**uper (Use `super` if you only write into a collection).

---

## 13. Summary Checklist
- ✅ **Type Safety**: Shift errors from runtime to compile time.
- ✅ **Code Reuse**: Write once, use for any object type.
- ✅ **Erasure**: Remember that Generics are a compile-time feature.
- ✅ **PECS**: Remember when to use `extends` vs `super`.
- ✅ **Invariance**: Remember `List<Integer>` is not a `List<Number>`.
