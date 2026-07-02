# Threading Concepts — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

**Q1. What is the difference between a process and a thread?**

**A:**
*   **Process:** An executing instance of a program with its own isolated address space, memory, file descriptors, and security context. Processes do not share memory directly and communicate via IPC (sockets, pipes).
*   **Thread:** A lightweight unit of execution within a process. Multiple threads share the process's memory (heap, method area) but maintain their own call stacks, registers, and program counters. Creating/switching threads is faster than processes.

---

**Q2. What is a race condition? Give an example.**

**A:** A race condition is a concurrency bug where the output of a program depends on the relative timing or interleaving of execution threads. It occurs when multiple threads access shared mutable data concurrently, and at least one write occurs without synchronization.
*   *Example:* Non-atomic increment (`count++`). It consists of three steps: read `count`, add 1, write back. If two threads read `count = 5` simultaneously, they will both write back `6`, resulting in a lost update (correct total should be `7`).

---

**Q3. What is thread safety? How do you achieve it in Java?**

**A:** Thread safety means that a class or method behaves correctly (maintains its invariants) when invoked by multiple concurrent threads, without requiring additional synchronization by the caller.
*   *How to achieve it:*
    1.  **Immutability:** Make objects stateless/read-only (e.g. `final` classes/fields).
    2.  **Synchronization/Locks:** Use `synchronized` keyword, `ReentrantLock`, or `StampedLock`.
    3.  **Atomic variables:** Use `AtomicInteger`, `AtomicReference` (uses CPU CAS).
    4.  **Concurrent Collections:** Use `ConcurrentHashMap`, `CopyOnWriteArrayList`.
    5.  **Thread Confinement:** Keep mutable variables private to a single thread (`ThreadLocal`).

---

**Q4. What is the difference between `synchronized` methods and `synchronized` blocks?**

**A:**
*   **Synchronized Method:** Locks the entire method. If it's an instance method, it locks on `this` object instance. If it's static, it locks on the class's `Class` object.
*   **Synchronized Block:** Locks only a specific block of code inside a method. It allows locking on a custom monitor object (`synchronized(lockObject) { ... }`). It is preferred because it reduces scope, increasing concurrency by keeping lock hold times minimal.

---

**Q5. What is a monitor in Java? How does it relate to `synchronized`?**

**A:** A monitor is an internal synchronization mechanism (associated with every object in Java) that controls concurrent thread access to resources. Only one thread can own the monitor at a time. The `synchronized` keyword translates to `MONITORENTER` and `MONITOREXIT` bytecode instructions, which acquire and release the monitor lock of the target object.

---

**Q6. What is the `volatile` keyword? What guarantees does it provide?**

**A:** `volatile` is applied to variables. It provides two key guarantees:
1.  **Visibility:** Writes to a volatile variable are immediately flushed to main memory, and reads are fetched directly from main memory (bypassing CPU caches). This ensures all threads see the most up-to-date value.
2.  **Instruction Reordering:** It prevents compile-time and CPU instruction reordering around the variable by inserting memory barriers.
*   *Note:* `volatile` does **not** guarantee atomicity (e.g., `volatile count++` is still unsafe).

---

**Q7. What is the difference between `volatile` and `synchronized`?**

**A:**
*   `volatile`: Only guarantees visibility and prevents reordering. It is non-blocking (threads never block or context-switch on volatile). Can only be applied to variables.
*   `synchronized`: Guarantees both **visibility and atomicity** (mutual exclusion). It is blocking (threads wait to acquire locks, causing context-switch overhead). Can be applied to blocks and methods.

---

**Q8. What is the happens-before relationship in Java's memory model?**

**A:** It is a set of formal rules defining memory visibility guarantees in concurrent programs. If action A "happens-before" action B, the Java Memory Model guarantees that B will see all memory updates made by A.
*   *Key Rules:*
    *   **Monitor Lock Rule:** An unlock on a monitor happens-before every subsequent lock on the same monitor.
    *   **Volatile Variable Rule:** A write to a volatile field happens-before every subsequent read of that same field.
    *   **Thread Start Rule:** A call to `Thread.start()` happens-before any actions in the started thread.
    *   **Thread Join Rule:** Any action in a thread happens-before any successful return from a `join()` on that thread.

---

**Q9. What is thread starvation and how is it different from a deadlock?**

**A:**
*   **Thread Starvation:** A scenario where a thread is perpetually denied CPU time or lock acquisition because other threads are prioritized (e.g., using unfair locks or bad thread priorities). The starved thread is active and ready to run but cannot progress.
*   **Deadlock:** A state where two or more threads are permanently blocked waiting for resources held by each other. No thread can progress.

---

**Q10. What is a livelock? Give an example.**

**A:** A livelock is a scenario where threads continuously change their states in response to each other, but without making any actual progress. The threads are active and consuming CPU (unlike deadlock where they are blocked/sleeping).
*   *Example:* Two polite people attempting to pass each other in a narrow hallway. Both step to the left at the same time, then both step to the right at the exact same time, repeatedly blocking each other's path.

---

## Thread Lifecycle & Management

**Q11. What are the states in a Java thread's lifecycle?**

**A:** The `Thread.State` enum defines:
1.  **NEW:** Thread is created but not yet started (before `start()` is called).
2.  **RUNNABLE:** Thread is executing in the JVM (can be running or waiting for CPU time from the OS).
3.  **BLOCKED:** Thread is waiting to acquire a monitor lock to enter a synchronized block/method.
4.  **WAITING:** Thread is waiting indefinitely for another thread to perform an action (e.g., via `Object.wait()`, `Thread.join()`, or `LockSupport.park()`).
5.  **TIMED_WAITING:** Thread is waiting for a specified timeout period (e.g., `Thread.sleep(ms)`, `Object.wait(timeout)`).
6.  **TERMINATED:** Thread has completed execution.

---

**Q12. What is the difference between `sleep()`, `wait()`, `yield()`, and `join()`?**

**A:**
*   `sleep(timeout)`: Static method of `Thread`. Puts the current thread to sleep. **Does not release locks**.
*   `wait()`: Instance method of `Object`. Releases the object monitor lock and puts the thread in `WAITING` state. Must be called inside a synchronized context.
*   `yield()`: Static method of `Thread`. A hint to the scheduler that the current thread is willing to yield its current CPU slice. Scheduler is free to ignore it.
*   `join()`: Instance method of `Thread`. Blocks the calling thread until the target thread terminates.

---

**Q13. What is the difference between `notify()` and `notifyAll()`?**

**A:**
*   `notify()`: Wakes up a single thread waiting on the object's monitor. Which thread gets woken up is non-deterministic (JVM dependent). Can lead to deadlocks if the wrong thread is woken.
*   `notifyAll()`: Wakes up all threads waiting on the object's monitor. More robust and preferred, as it ensures all threads get to re-evaluate their waiting conditions.

---

**Q14. What is a daemon thread? How do you create one?**

**A:** A daemon thread is a low-priority background thread that provides service support to user threads (e.g. Garbage Collection).
*   *Behavior:* The JVM exits automatically when all user (non-daemon) threads complete, forcefully killing any remaining daemon threads.
*   *Creation:* Set `thread.setDaemon(true)` **before** calling `thread.start()`.

---

**Q15. How do you stop a thread safely in Java?**

**A:** Do not use `Thread.stop()` as it is deprecated and highly unsafe (forces the thread to release all locks, leaving shared objects in corrupted states).
*   *Safe approach:* Use a volatile boolean flag or the built-in thread interruption mechanism. The thread should check the flag or interruption status periodically and terminate its run loop gracefully.

```java
public void run() {
    while (!Thread.currentThread().isInterrupted()) {
        // process tasks
    }
}
```

---

**Q16. What is thread interruption? How does `isInterrupted()` differ from `interrupted()`?**

**A:** Interruption is a cooperative mechanism where a thread requests another thread to stop by setting its interruption status flag.
*   `isInterrupted()`: Instance method. Queries the thread's interruption status flag and returns boolean. **Does not change/clear the flag**.
*   `Thread.interrupted()`: Static method. Queries the interruption status of the *current* thread and **clears the flag** (resets it to false).

---

## Java Concurrency Utilities

**Q17. What is an `ExecutorService`? How is it different from manually creating threads?**

**A:** `ExecutorService` is an asynchronous execution framework that manages a pool of worker threads.
*   *Differences:* Manually spawning `new Thread()` on every task is expensive (thread creation/teardown overhead, risk of resource exhaustion). `ExecutorService` decouples task submission from execution, reuses threads, manages queues, and handles worker lifecycle.

---

**Q18. What are the different types of thread pools provided by `Executors`? When do you use each?**

**A:**
*   `newFixedThreadPool(n)`: Fixed number of threads. Uses an unbounded queue. Best for predictable, steady workloads.
*   `newCachedThreadPool()`: Dynamically spawns threads as needed and reclaims idle ones. Best for many short-lived, low-latency tasks.
*   `newSingleThreadExecutor()`: Single thread executes tasks sequentially. Best for order-dependent execution.
*   `newScheduledThreadPool(n)`: Supports delayed or periodic task execution.

---

**Q19. What is a `Future` and a `Callable`? How do they differ from `Runnable`?**

**A:**
*   `Callable<V>`: Similar to `Runnable` but its `call()` method returns a result of type V and can throw checked exceptions.
*   `Runnable`: Its `run()` method returns void and cannot throw checked exceptions.
*   `Future<V>`: Represents the pending result of an asynchronous computation. You can call `.get()` (blocking) to retrieve the value, `.cancel()` to abort execution, or check `.isDone()`.

---

**Q20. What is `CompletableFuture`? How does it support non-blocking async programming?**

**A:** Introduced in Java 8, `CompletableFuture` implements `Future` and `CompletionStage`. It supports pipeline chaining via callbacks (like `.thenApply()`, `.thenAccept()`, `.exceptionally()`) that execute automatically when tasks complete. This avoids blocking threads while waiting for I/O operations or background computations to finish.

---

**Q21. What is a `CountDownLatch`? Give a real-world use case.**

**A:** A synchronization utility that allows one or more threads to wait until a set of operations being performed in other threads completes. It is initialized with a count. Threads call `await()` to block, and workers call `countDown()` to decrement the counter. Once the count reaches 0, the latch opens and waiting threads proceed. It **cannot be reset** or reused.
*   *Use Case:* An orchestrator waiting for three configuration services to initialize before launching the web server.

---

**Q22. What is a `CyclicBarrier`? How is it different from a `CountDownLatch`?**

**A:** A synchronization utility that allows a set of threads to all wait for each other to reach a common barrier point before proceeding.
*   *Differences:*
    *   `CountDownLatch` is one-use; `CyclicBarrier` **can be reset** and reused.
    *   `CountDownLatch` counts down events; `CyclicBarrier` counts down threads.
    *   `CyclicBarrier` can run a barrier action runnable when all threads reach the point.

---

**Q23. What is a `Semaphore` and when would you use it?**

**A:** A Semaphore maintains a set of permits. Threads call `acquire()` to get a permit (blocks if none are available) and `release()` to return it.
*   *Use Case:* Throttling access to a limited resource (e.g. limiting database connections to 10 concurrently to prevent overloading).

---

**Q24. What is a `Phaser` and how does it generalize `CyclicBarrier`?**

**A:** `Phaser` is a flexible synchronization barrier that supports dynamic registration of participating threads. Unlike `CyclicBarrier` and `CountDownLatch` (where the number of threads/counts is fixed at creation), a `Phaser` allows threads to register and deregister dynamically at runtime. It is organized into "phases".

---

**Q25. What is `BlockingQueue`? Name three implementations and their differences.**

**A:** A `Queue` that supports operations that wait for the queue to become non-empty when retrieving, and wait for space to become available when storing.
*   *Three implementations:*
    1.  `ArrayBlockingQueue`: Bounded, backed by an array. Fixed capacity, low memory overhead.
    2.  `LinkedBlockingQueue`: Optionally bounded, backed by linked nodes. High throughput.
    3.  `SynchronousQueue`: Zero capacity. Each insert operation must wait for a corresponding remove operation by another thread.

---

**Q26. What is the difference between `LinkedBlockingQueue` and `ArrayBlockingQueue`?**

**A:**
*   `ArrayBlockingQueue` uses a single lock for both put and take operations. Circular array buffer. Must specify capacity.
*   `LinkedBlockingQueue` uses two separate locks (one for puts, one for takes), allowing higher concurrency. High garbage collection overhead due to creating Node objects on insertions. Defaults to unbounded if size is not specified.

---

## Locks & Synchronizers

**Q27. What is `ReentrantLock`? How is it superior to `synchronized`?**

**A:** A concrete lock implementation offering manual lock/unlock operations. It is "reentrant", meaning a thread holding the lock can acquire it again without blocking.
*   *Superiors:*
    *   Supports non-blocking lock attempts via `tryLock()` and timed lock attempts.
    *   Supports interruption during lock acquisition (`lockInterruptibly()`).
    *   Allows creating **fair locks** (FIFO queue).
    *   Supports multiple condition variables (via `newCondition()`).

---

**Q28. What is the difference between a fair and an unfair lock?**

**A:**
*   **Fair Lock:** Grants access to the thread that has been waiting the longest (FIFO order). Avoids starvation but degrades throughput due to thread scheduling/wake-up overhead.
*   **Unfair Lock (Default):** Allows barge-in. If a lock becomes free and a new thread requests it, the new thread can jump ahead of waiting threads. Provides significantly higher throughput but risks thread starvation.

---

**Q29. What is `ReadWriteLock` and when does it improve performance?**

**A:** A lock containing a read lock and a write lock. Multiple threads can hold the read lock concurrently (shared), but only one thread can hold the write lock (exclusive). It improves performance for **read-heavy workloads** where writes are infrequent, avoiding thread blocking for readers.

---

**Q30. What is `StampedLock` and how does it support optimistic reads?**

**A:** Introduced in Java 8, it provides stamp-based lock control. It supports three modes: Read, Write, and **Optimistic Read**.
*   *Optimistic Read:* Does not acquire a lock. It returns a stamp. The thread reads values, then calls `validate(stamp)`. If no write lock was acquired in the meantime, the read is valid, completely avoiding read lock overhead. If validation fails, it falls back to a read lock.

---

**Q31. What is a `LockSupport` class?**

**A:** A basic thread-blocking primitive class. It uses a "permit" concept to implement thread parking. Calls to `LockSupport.park()` block the current thread until `LockSupport.unpark(thread)` is called. It is the building block for AQS (AbstractQueuedSynchronizer) and custom lock implementations.

---

**Q32. What is the `tryLock()` method and why is it useful to avoid deadlocks?**

**A:** `tryLock()` attempts to acquire a lock immediately. If the lock is free, it acquires it and returns `true`. If not, it returns `false` without blocking. It avoids deadlocks by allowing a thread to give up its held locks and back off if it cannot acquire all the locks it needs for an operation.

---

## Atomic Variables & CAS

**Q33. What are atomic variables in Java? Name three examples.**

**A:** Classes in `java.util.concurrent.atomic` that support lock-free, thread-safe operations on single variables.
*   *Examples:* `AtomicInteger`, `AtomicLong`, `AtomicReference`.

---

**Q34. What is Compare-And-Swap (CAS)? How do `AtomicInteger` operations use it internally?**

**A:** CAS is an atomic CPU instruction. It takes three arguments: a memory location, the expected old value, and the new value. It updates the memory location to the new value *only if* the current value matches the expected old value, returning true/false.
*   `AtomicInteger` runs a loop (spin-lock) calling CAS. If the update fails (another thread changed the value), it reads the new value and retries until success:

```java
// Conceptual AtomicInteger increment
int current;
do {
    current = get();
} while (!compareAndSet(current, current + 1));
```

---

**Q35. What is the ABA problem in CAS? How does `AtomicStampedReference` solve it?**

**A:**
*   **ABA Problem:** Thread 1 reads value A, decides to change it. Thread 2 preempts, changes the value from A to B, then back to A. When Thread 1 executes CAS, it sees A and succeeds, unaware that the data was modified and reverted.
*   *Solution:* Use `AtomicStampedReference`. It associates both the object reference and an integer "stamp" (version counter). The CAS only succeeds if both the reference and the stamp match expected values.

---

**Q36. When would you use `LongAdder` over `AtomicLong`?**

**A:** Use `LongAdder` in **extremely high-write contention** environments (e.g. global request counters). `AtomicLong` forces all threads to spin on a single memory address, causing CPU cache line bouncing. `LongAdder` maintains a striped array of cell variables; threads write to different cells based on thread hash. When reading the value (calling `sum()`), it aggregates all cells.

---

## Concurrent Collections

**Q37. What is `ConcurrentHashMap` and how does it differ from `HashMap` and `Hashtable`?**

**A:**
*   `Hashtable` locks the entire collection on every operation. Slow.
*   `ConcurrentHashMap` uses bucket-level synchronization (locking only the bucket head nodes) and CAS operations. It allows concurrent reads and writes, scaling performance with the number of buckets.

---

**Q38. What is `CopyOnWriteArrayList`? When is it useful?**

**A:** A thread-safe list where all mutative operations (add, set) create a fresh copy of the underlying array.
*   *When useful:* Read-heavy environments where modifications are rare (e.g. event listeners, configuration caches). Iterations bypass locks completely and use the snapshot, avoiding `ConcurrentModificationException`.

---

**Q39. What is `ConcurrentLinkedQueue`?**

**A:** An unbounded, thread-safe, non-blocking queue based on linked nodes. It uses lock-free CAS algorithms to enqueue and dequeue elements, making it highly efficient for multi-producer/multi-consumer tasks.

---

## Common Problems & Patterns

**Q40. What is a deadlock? How do you detect and prevent it?**

**A:** Deadlock occurs when threads are permanently blocked waiting for resources held by each other.
*   *Detection:* Analyze thread dumps (`jstack`) or use JVM monitoring tools.
*   *Prevention:*
    1.  Acquire locks in a strict global order.
    2.  Use `tryLock` with timeouts.
    3.  Avoid holding multiple locks simultaneously.
    4.  Keep synchronized blocks minimal.

---

**Q41. What is the producer-consumer problem? How do you implement it in Java?**

**A:** A classic concurrency pattern where producers generate data and push it to a buffer, and consumers retrieve data from the buffer.
*   *Implementation:* The easiest way is using a `BlockingQueue`:

```java
BlockingQueue<Data> queue = new ArrayBlockingQueue<>(10);
// Producer
queue.put(data); // Blocks if queue is full
// Consumer
Data data = queue.take(); // Blocks if queue is empty
```

---

**Q42. What is the dining philosophers problem and what does it illustrate?**

**A:** A classic synchronization problem where philosophers sit around a table and need two chopsticks (left and right) to eat. It illustrates the challenges of deadlock (if all pick up their left chopstick, they block forever waiting for the right) and starvation.

---

**Q43. What is double-checked locking? Is it safe in Java? How do you fix it?**

**A:** A design pattern to reduce lock synchronization overhead in lazy singleton initialization.
*   *Is it safe:* It is **unsafe** in Java without the `volatile` keyword because the compiler/CPU can reorder instructions during object creation (assigning the memory address to the reference variable *before* running the constructor), causing another thread to retrieve a partially initialized object.
*   *Fix:* Make the singleton instance variable `volatile`.

---

**Q44. What is the ThreadLocal class? When would you use it?**

**A:** `ThreadLocal` provides thread-local variables. Each thread has its own independently initialized copy of the variable.
*   *Use Cases:* Storing transaction IDs, user session details, or unsafe thread utilities (like `SimpleDateFormat`) without locks.
*   *Warning:* Always call `.remove()` in thread pool environments to prevent memory leaks from ClassLoader references.

---

**Q45. What are the four conditions necessary for a deadlock (Coffman conditions)?**

**A:**
1.  **Mutual Exclusion:** At least one resource must be held in non-shareable mode.
2.  **Hold and Wait:** A thread must hold at least one resource and wait to acquire others.
3.  **No Preemption:** Resources cannot be forcefully taken from threads holding them.
4.  **Circular Wait:** A closed chain of threads exists, where each thread waits for a resource held by the next.

---

## Performance

**Q46. What is context switching overhead and how does it affect performance?**

**A:** Context switching is the OS saving the CPU state (registers, program counter) of a running thread and loading the state of another thread. It consumes CPU cycles and invalidates CPU instruction/data caches, degrading application throughput. Too many threads run into thrashing, spending more time switching contexts than doing work.

---

**Q47. What is cache line false sharing? How do you avoid it?**

**A:** False sharing occurs when two threads running on different CPU cores modify independent variables that reside on the same CPU cache line. When one core updates its variable, it invalidates the entire cache line on the other core, forcing it to reload from main memory.
*   *Avoidance:* Use `@Contended` annotation (adds padding bytes) or write manual padding fields around variables.

---

**Q48. What is the fork/join framework in Java? How does work-stealing work?**

**A:** An implementation of the `ExecutorService` designed to run divide-and-conquer tasks.
*   **Work-stealing:** Each worker thread maintains a double-ended queue (deque) of tasks. If a thread finishes all its tasks, it steals tasks from the tail end of other active threads' deques, maximizing CPU utilization.

---

**Q49. When does adding more threads hurt performance instead of helping?**

**A:** When the number of active threads exceeds the number of physical CPU cores, particularly for CPU-bound tasks. This results in heavy context switching overhead and lock contention, degrading total throughput.

---

**Q50. What is thread confinement as a concurrency strategy?**

**A:** Thread confinement ensures that an object is only accessed by a single thread. Since no state is shared, no synchronization is required.
*   *Types:* Ad-hoc (defined by local method variables), Stack confinement (primitive local variables), or using `ThreadLocal`.

---
