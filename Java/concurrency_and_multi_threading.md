# Concurrency & Multi-threading - Interview Notes 🧵

## 1. Introduction
**Concurrency** is the ability of a program to handle multiple tasks at the same time. **Multi-threading** is a specific form of concurrency where a single process spawns multiple threads to perform different tasks simultaneously.

- **Objective**: Improve application performance and responsiveness.
- **Concurrency vs. Parallelism**: 
    - Concurrency is about *dealing* with lots of things at once (Task switching).
    - Parallelism is about *doing* lots of things at once (Multi-core execution).

---

## 2. Process and Threads
- **Process**: An instance of a program being executed. It has its own memory space (Heap/Stack).
- **Thread**: A light-weight unit of execution within a process.
    - Threads share the **Heap** memory but have their own **Stack**.
    - Context switching between threads is faster than processes.

---

## 3. Starting a Thread
In Java, you can start a thread in two main ways:
1.  **Extending `Thread` Class**:
    ```java
    class MyThread extends Thread {
        public void run() { ... }
    }
    new MyThread().start();
    ```
2.  **Implementing `Runnable` Interface** (Preferred):
    ```java
    Thread thread = new Thread(() -> System.out.println("Running..."));
    thread.start();
    ```
> [!TIP]
> Prefer `Runnable` because Java supports only single inheritance, and it separates the task from the execution mechanism.

---

## 4. Pausing a Thread
- **`Thread.sleep(ms)`**: Puts the current thread into a **Timed Waiting** state. It does NOT release any locks it currently holds.
- It throws a checked exception: `InterruptedException`.

---

## 5. Joining a Thread
- **`thread.join()`**: The current thread will wait until the target `thread` has finished its execution.
- Useful for coordinating dependencies between threads (e.g., waiting for a background data load to finish).

---

## 6. Interrupting a Thread
- **`thread.interrupt()`**: Sends a signal to the thread that it should stop what it's doing.
- It doesn't force the thread to stop; the thread must check its own interrupted status (`isInterrupted()`) or catch `InterruptedException`.

---

## 7. Concurrency Issues
The two biggest challenges in multi-threading are:
1.  **Visibility**: One thread changes a variable in its CPU cache, but other threads still see the old value from Main Memory.
2.  **Atomicity**: An operation like `count++` is not atomic; it involves three steps (Read, Increment, Write). Multiple threads can interleave these steps.

---

## 8. Race Conditions
A race condition occurs when the final result depends on the timing/interleaving of thread execution.
- **Common Pattern**: "Check-then-Act" (e.g., `if (x == 10) x++`). Between the check and the act, another thread might have changed `x`.

---

## 9. Strategies for Thread Safety
- **Immutability**: Make objects unchangeable (e.g., `String`).
- **Confinement**: Don't share variables between threads.
- **Synchronization**: Use locks to control access to shared state.

---

## 10. Confinement (ThreadLocal)
- **`ThreadLocal<T>`**: Provides variables that can only be read and written by the same thread.
- Each thread that accesses such a variable has its own, independently initialized copy.

---

## 11. Locks (ReentrantLock)
Part of the `java.util.concurrent.locks` package.
- **Capabilities**: Explicit `lock()` and `unlock()`, timing out while waiting for a lock, and fair locking.
```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock(); // Always unlock in finally!
}
```

---

## 12. The synchronized Keyword
The simplest way to implement thread safety using **Intrinsic Locks** (Monitors).
- **Synchronized Method**: Locks the entire object (`this`).
- **Synchronized Block**: Allows finer-grained locking on a specific object.

---

## 13. The volatile Keyword
Ensures that a variable is always read from and written to **Main Memory**, not CPU caches.
- **Guarantees Visibility**, but NOT Atomicity.
- Use it for flags (e.g., `volatile boolean stop = false`).

---

## 14. Thread Signalling (wait/notify)
Low-level mechanism for threads to communicate.
- **`wait()`**: Causes the current thread to wait until another thread invokes `notify()`.
- **`notify()` / `notifyAll()`**: Wakes up waiting threads.
> [!IMPORTANT]
> These methods MUST be called inside a **synchronized** block/method.

---

## 15. Atomic Objects
Classes like `AtomicInteger`, `AtomicBoolean`, and `AtomicReference` use low-level **CPU instructions (CAS - Compare and Swap)** to ensure atomicity without locking.
- **Benefit**: Much faster than `synchronized` under low to moderate contention.

---

## 16. Adders
Introduced in Java 8 (`LongAdder`, `DoubleAdder`).
- **Why?** When many threads update a single `AtomicLong`, they contend on one variable. 
- **Solution**: `LongAdder` maintains a set of variables to distribute the contention and sums them up only when requested. Use it for high-concurrency counters.

---

## 17. Synchronized Collections
Methods like `Collections.synchronizedList(list)` wrap an existing collection to make it thread-safe.
- **Drawback**: They use a single lock for the entire collection, which limits performance under high concurrency.

---

## 18. Concurrent Collections
Found in `java.util.concurrent`.
- **`ConcurrentHashMap`**: Uses "Lock Stripping" (locking only segments of the map) to allow multiple threads to read/write concurrently.
- **`CopyOnWriteArrayList`**: Creates a fresh copy of the underlying array for every modification. Great for frequent reads and rare writes.

---

## 19. Summary table
| Feature | Guaranteed Visibility? | Guaranteed Atomicity? | Blocks Thread? |
| :--- | :---: | :---: | :---: |
| **volatile** | ✅ Yes | ❌ No | ❌ No |
| **synchronized** | ✅ Yes | ✅ Yes| ✅ Yes |
| **AtomicInteger**| ✅ Yes | ✅ Yes| ❌ No (CAS) |
| **ReentrantLock**| ✅ Yes | ✅ Yes| ✅ Yes |
