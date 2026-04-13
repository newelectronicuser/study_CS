# Java Memory Management — Interview Notes 🧠

## 1. Overview

Java memory is managed automatically by the **JVM**. Unlike C/C++, developers don't manually call `malloc`/`free` — the **Garbage Collector (GC)** identifies and reclaims unreachable memory.

**Why this matters in interviews**: Memory questions test understanding of JVM internals, performance tuning, `String` pool behaviour, and common leak patterns.

---

## 2. JVM Runtime Data Areas

```
┌─────────────────────────────────────────────────────────┐
│                        JVM Process                      │
│                                                         │
│  ┌──────────┐  ┌──────────┐   ┌─────────────────────┐   │
│  │ Thread 1 │  │ Thread 2 │   │      Heap (Shared)  │   │
│  │  Stack   │  │  Stack   │   │  ┌───────────────┐  │   │
│  │ (local   │  │ (local   │   │  │ Young Gen     │  │   │
│  │  vars,   │  │  vars,   │   │  │ Eden | S0 | S1│  │   │
│  │  frames) │  │  frames) │   │  ├───────────────┤  │   │
│  └──────────┘  └──────────┘   │  │ Old Gen       │  │   │
│                               │  │ (Tenured)     │  │   │
│  ┌──────────────────────────┐ │  └───────────────┘  │   │
│  │  Metaspace (Native Mem)  │ └─────────────────────┘   │
│  │  class defs, statics     │                           │
│  └──────────────────────────┘                           │
└─────────────────────────────────────────────────────────┘
```

---

## 3. Stack Memory

| Property      | Detail                                                                                 |
| :------------ | :------------------------------------------------------------------------------------- |
| **Stores**    | Method frames, local primitives (`int`, `double`), object **references** (not objects) |
| **Scope**     | **Thread-private** — each thread has its own stack                                     |
| **Lifecycle** | LIFO — frame pushed on call, popped on return                                          |
| **Size**      | Fixed per thread (configurable via `-Xss`)                                             |
| **Error**     | `StackOverflowError` — infinite recursion                                              |

```java
void method() {
    int x = 5;              // x lives on the Stack (primitive)
    String s = "hello";     // s (reference) lives on Stack; "hello" object lives on Heap
    User u = new User();    // u (reference) on Stack; User object on Heap
}
// After method returns: x, s, u are all popped off the stack
```

---

## 4. Heap Memory

| Property      | Detail                                               |
| :------------ | :--------------------------------------------------- |
| **Stores**    | ALL objects and arrays created with `new`            |
| **Scope**     | **Shared** — every thread reads/writes the same heap |
| **Lifecycle** | Managed by the Garbage Collector                     |
| **Size**      | Configurable: `-Xms` (initial), `-Xmx` (maximum)     |
| **Error**     | `OutOfMemoryError: Java heap space`                  |

```java
// Objects always live on the Heap
User user = new User("Alice");   // User object → Heap
int[] arr  = new int[100];       // array → Heap
String s   = new String("hi");   // String object → Heap (NOT the pool)
```

> [!IMPORTANT]
> The **reference variable** (e.g., `user`) lives on the Stack. The **object itself** (the `User` data) lives on the Heap. This distinction is critical for pass-by-value questions.

---

## 5. Metaspace (Java 8+)

- Stores **class metadata**: class definitions, method bytecode, static variables and constants.
- Uses **native (OS) memory**, not the JVM heap — grows dynamically by default.
- Replaced **PermGen** in Java 8. PermGen had a fixed max size and caused `OutOfMemoryError: PermGen space` when too many classes were loaded (common in app servers).
- Can still OOM if class-loading leaks occur: `-XX:MaxMetaspaceSize=256m` caps it.

---

## 6. Stack vs Heap — Quick Reference

|                 | Stack                          | Heap               |
| :-------------- | :----------------------------- | :----------------- |
| **Stores**      | Primitives, references, frames | Objects, arrays    |
| **Shared?**     | No (thread-private)            | Yes (all threads)  |
| **GC managed?** | No (auto LIFO)                 | Yes                |
| **Speed**       | Faster                         | Slower             |
| **Size**        | Small (MB)                     | Large (GB)         |
| **Error**       | `StackOverflowError`           | `OutOfMemoryError` |

---

## 7. The Generational Heap

**Generational Hypothesis**: Most objects die young. The heap is split so short-lived objects are collected cheaply and frequently.

```
Heap
├── Young Generation  (Minor GC — fast, frequent)
│   ├── Eden Space    — all new objects born here
│   ├── Survivor S0   — objects that survived at least 1 Minor GC
│   └── Survivor S1   — one of S0/S1 is always empty (copied back and forth)
│
└── Old Generation / Tenured  (Major GC — slow, infrequent)
    └── Objects promoted after surviving N Minor GCs (default threshold: 15)
```

### GC cycle flow

1. Object created → **Eden**.
2. Eden fills → **Minor GC**: live objects copied to a Survivor space; age counter incremented.
3. After reaching **tenuring threshold** → object **promoted** to **Old Gen**.
4. Old Gen fills → **Major / Full GC** (expensive — Stop-the-World for longer).

---

## 8. Garbage Collection — Key Concepts

### Reachability & GC Roots

An object is **eligible for GC** when no GC Root can reach it.

**GC Roots are**:

- Local variables in active thread stacks
- Static variables (class-level)
- Active Java threads themselves
- JNI (native code) references

```java
User u = new User("Alice"); // reachable via local var 'u' (GC Root)
u = null;                   // 'u' no longer holds reference → User object unreachable
                            // NOW eligible for GC (but not immediately collected)
```

### Mark and Sweep (basic algorithm)

1. **Mark** — traverse from GC roots; mark all reachable objects.
2. **Sweep** — delete all unmarked (unreachable) objects.
3. **Compact** — (optional) slide remaining objects together to eliminate fragmentation.

### Stop-The-World (STW)

A pause where **all application threads are halted** so the GC can safely move or delete objects without concurrent modifications. The goal of modern GCs is to minimize this pause.

---

## 9. Modern Garbage Collectors

| Collector       | Default Since  | Best For                      | Pause Goal           |
| :-------------- | :------------- | :---------------------------- | :------------------- |
| **Serial GC**   | —              | Single-threaded / small heaps | N/A                  |
| **Parallel GC** | Java 8         | Throughput-first (batch jobs) | High throughput      |
| **G1GC**        | Java 9         | General purpose               | Predictable < 200 ms |
| **ZGC**         | Java 15 (prod) | Ultra-low latency             | < 1 ms               |
| **Shenandoah**  | Java 12        | Low pause, large heaps        | < 10 ms              |

```bash
# JVM GC flags
-XX:+UseG1GC              # use G1 (default Java 9+)
-XX:+UseZGC               # use ZGC
-XX:MaxGCPauseMillis=200  # G1 soft pause target
-Xms512m -Xmx4g           # initial and max heap size
-verbose:gc               # print GC events to stdout
```

---

## 10. String Pool (String Intern Pool)

The JVM maintains a special area in the **Heap** (moved from PermGen to Heap in Java 7) for string literals — the **String Constant Pool**.

```java
String a = "hello";           // from pool — same object
String b = "hello";           // from pool — same object as 'a'
String c = new String("hello"); // NEW object on heap — NOT from pool

System.out.println(a == b);   // true  — same pool reference
System.out.println(a == c);   // false — different objects
System.out.println(a.equals(c)); // true — same content

// intern() — forces a string onto the pool
String d = c.intern();
System.out.println(a == d);   // true — d now points to the pool object
```

> [!IMPORTANT]
> Always use `.equals()` to compare String **content**. Using `==` compares **references** (memory addresses), which gives unpredictable results.

---

## 11. Pass-by-Value vs Pass-by-Reference

> [!IMPORTANT]
> **Java is strictly pass-by-value.** Always. No exceptions.

```java
// Primitive — value is COPIED; original is unchanged
void increment(int x) { x++; }  // x is a copy
int n = 5;
increment(n);
System.out.println(n); // still 5

// Object — the REFERENCE (address) is COPIED
//          you can mutate the object's state through the copy
void setName(User u) { u.setName("Bob"); } // modifies the object on the heap
User user = new User("Alice");
setName(user);
System.out.println(user.getName()); // "Bob" — object state changed

// But you cannot reassign the caller's reference
void reassign(User u) { u = new User("Carol"); } // only local copy is changed
User user2 = new User("Alice");
reassign(user2);
System.out.println(user2.getName()); // "Alice" — original reference unchanged
```

---

## 12. Memory Leaks

A **memory leak** happens when objects are no longer used but are still **reachable from a GC Root** — so GC can never collect them.

### Common Causes

| Cause                        | Example                                                            | Fix                                      |
| :--------------------------- | :----------------------------------------------------------------- | :--------------------------------------- |
| **Static collections**       | `static List<Object> cache = new ArrayList<>()` that grows forever | Bounded cache, `WeakHashMap`             |
| **Unclosed resources**       | `Connection`, `InputStream` never closed                           | `try-with-resources`                     |
| **Non-static inner classes** | Anonymous listener holds implicit ref to outer class               | Use static inner class or weak reference |
| **ThreadLocal leaks**        | `ThreadLocal` values not removed in thread pools                   | Call `threadLocal.remove()` in finally   |
| **Listeners / callbacks**    | Event listeners registered but never deregistered                  | Deregister in lifecycle method           |
| **Classloader leaks**        | App servers reloading classes without unloading old ones           | Diagnose with MAT heap dump              |

```java
// ThreadLocal leak — common in web servers with thread pools
ThreadLocal<User> ctx = new ThreadLocal<>();
try {
    ctx.set(currentUser);
    // ... process request ...
} finally {
    ctx.remove(); // MUST remove — thread is reused, old value stays otherwise
}
```

---

## 13. `WeakReference`, `SoftReference`, `PhantomReference`

| Type                  | GC Behaviour                                     | Use Case                |
| :-------------------- | :----------------------------------------------- | :---------------------- |
| `StrongReference`     | Never collected if reachable                     | Normal variables        |
| `WeakReference<T>`    | Collected at next GC                             | `WeakHashMap`, caches   |
| `SoftReference<T>`    | Collected only when memory is low                | Memory-sensitive caches |
| `PhantomReference<T>` | Never returns object; used with `ReferenceQueue` | Post-GC cleanup hooks   |

```java
// WeakReference — cache that doesn't prevent GC
WeakReference<byte[]> weakRef = new WeakReference<>(new byte[1024]);
byte[] data = weakRef.get(); // null if GC has collected it
if (data == null) System.out.println("Object was garbage collected");

// WeakHashMap — keys are weakly referenced; entry auto-removed when key is GC'd
Map<Object, String> cache = new WeakHashMap<>();
Object key = new Object();
cache.put(key, "value");
key = null; // key becomes unreachable → entry will be removed by GC
```

---

## 14. Diagnosing Memory Issues

### JVM Flags for Monitoring

```bash
-XX:+HeapDumpOnOutOfMemoryError      # auto-dump heap on OOM
-XX:HeapDumpPath=/tmp/heap.hprof     # dump file location
-Xlog:gc*                            # log all GC events (Java 9+)
-XX:+PrintGCDetails                  # detailed GC log (Java 8)
```

### Tools

| Tool            | Purpose                                                         |
| :-------------- | :-------------------------------------------------------------- |
| **VisualVM**    | Real-time heap/thread monitoring; trigger GC manually           |
| **Eclipse MAT** | Deep heap dump analysis; find leak suspects                     |
| **jmap**        | Generate heap dump: `jmap -dump:format=b,file=heap.hprof <pid>` |
| **jstat**       | Live GC stats: `jstat -gcutil <pid> 1000` (every 1 s)           |
| **jconsole**    | JMX monitoring GUI                                              |

---

## 15. Interview Checklist

| Question                           | Key Answer                                                              |
| :--------------------------------- | :---------------------------------------------------------------------- |
| Stack vs Heap?                     | Stack = thread-private, LIFO, primitives + refs; Heap = shared, objects |
| Where do local variables go?       | Primitives on Stack; if object, reference on Stack, object on Heap      |
| What causes `StackOverflowError`?  | Infinite/deep recursion exceeds stack depth                             |
| What causes `OutOfMemoryError`?    | Heap full (leak or too-small heap), or Metaspace full                   |
| Is Java pass-by-value?             | **Yes, always.** Object reference value is copied, not the object       |
| Can you force GC?                  | `System.gc()` is only a hint — JVM can ignore it                        |
| What is a GC Root?                 | Always-reachable object: stack var, static field, active thread         |
| What is a memory leak in Java?     | Object unreachable by app but still reachable by GC root                |
| String `==` vs `.equals()`?        | `==` compares references; `.equals()` compares content                  |
| What replaced PermGen?             | **Metaspace** (Java 8) — uses native memory, auto-grows                 |
| What is Stop-the-World?            | JVM pauses all threads during GC to safely move objects                 |
| What is `intern()`?                | Forces string into the pool; enables `==` comparison                    |
| Difference between Minor/Major GC? | Minor GC = Young Gen (fast); Major/Full GC = Old Gen (slow, STW)        |

> [!IMPORTANT]
> **Top 3 things to always say in memory interviews**:
>
> 1. Java is **pass-by-value** — the reference value is copied, not the object.
> 2. `==` on Strings compares **references**; always use `.equals()` for content.
> 3. Memory leaks in Java happen when objects are **still referenced** (GC can't clean) but no longer needed by the application — most commonly via static fields and unclosed resources.
