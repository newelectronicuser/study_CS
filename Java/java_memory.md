# Java Memory Management - Interview Study Guide 🧠

## 1. Overview
Java memory management is automated by the **Java Virtual Machine (JVM)**. Unlike C/C++, developers don't need to manually allocate and free memory (via `malloc`/`free`). Instead, the **Garbage Collector (GC)** handles the cleanup of unreachable objects.

---

## 2. JVM Memory Structure (Runtime Data Areas)
The JVM divides memory into several distinct areas. The most critical distinction is between **Stack** and **Heap**.

### 🥞 Stack Memory
*   **Purpose**: Stores method call frames, local primitive variables (`int`, `double`), and **references** (pointers) to objects on the heap.
*   **Scope**: **Thread-private**. Each thread has its own stack.
*   **Lifecycle**: LIFO (Last-In-First-Out). Allocated when a method is called, destroyed when it returns.
*   **Errors**: `StackOverflowError` (common in infinite recursion).

### 🏔️ Heap Memory
*   **Purpose**: Stores **all objects** and arrays created with the `new` keyword.
*   **Scope**: **Shared** across all threads.
*   **Lifecycle**: Managed by the Garbage Collector.
*   **Errors**: `OutOfMemoryError: Java heap space`.

### 📂 Metaspace (Native Memory)
*   **Purpose**: Stores class metadata (class definitions, methods, static variables).
*   **Note**: Replaced **PermGen** starting from Java 8. It uses **native memory** (outside the JVM heap), making it less susceptible to `OutOfMemoryError` unless there are massive class-loading leaks.

---

## 3. The Generational Heap (Generational Hypothesis)
Most objects in Java are short-lived ("die young"). To optimize performance, the Heap is divided into:

### A. Young Generation
1.  **Eden Space**: Where all new objects are initially created.
2.  **Survivor Spaces (S0 & S1)**: When Eden fills up, a **Minor GC** moves surviving objects here.
    *   One survivor space is always empty while the other is in use.

### B. Old Generation (Tenured)
*   Objects that "survive" multiple rounds of Minor GC in the Young Gen are **promoted** here.
*   Collection here is called **Major GC** or **Full GC**, which is more expensive than Minor GC.

---

## 4. Garbage Collection (GC) 🧹
The process of identifying and deleting unreachable objects to reclaim memory.

### Key Concepts:
*   **Stop-the-World (STW)**: A pause where all application threads are halted so the GC can safely move or delete objects.
*   **Mark and Sweep**: The basic algorithm—Mark reachable objects; delete the rest.

### Modern Collectors:
| Collector | Best For... | Key Feature |
| :--- | :--- | :--- |
| **G1GC** | General Purpose | Predictable pause times. Default in modern JDKs. |
| **ZGC** | Ultra-Low Latency | Sub-millisecond pauses. Great for multi-terabyte heaps. |
| **Parallel** | High Throughput | Uses multiple threads for GC; best for batch processing. |

---

## 5. Identifying Memory Leaks 🔎
A memory leak occurs when objects are no longer used by the application but are still **reachable from a GC Root** (so they can't be collected).

### Common Causes:
1.  **Static Fields**: Static collections that grow indefinitely.
2.  **Unclosed Resources**: Not closing Database connections or File streams.
3.  **Inner Classes**: Anonymous inner classes holding an implicit reference to the outer class.

### Tools for Analysis:
*   **VisualVM**: Real-time heap monitoring.
*   **Eclipse MAT (Memory Analyzer Tool)**: Deep analysis of heap dumps (`.hprof` files).

---

## 6. Interview "Gotchas" ❓

> [!IMPORTANT]
> **Is Java Pass-by-Value or Pass-by-Reference?**
> Java is strictly **Pass-by-Value**. For objects, the "value" being passed is the **reference**(memory address). You cannot change the original pointer, but you can change the state of the object it points to.

**Q: Can we force Garbage Collection?**
A: No. `System.gc()` is only a *hint* to the JVM. The JVM can choose to ignore it. Never rely on it in production code.

**Q: What is a GC Root?**
A: Objects that are always reachable. Examples: local variables in active stacks, active threads, and static variables. The GC starts its search from these roots.
