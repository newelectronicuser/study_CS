# Threading Concepts — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

**Q1. What is the difference between a process and a thread?**

**A:** A **process** is an independent program with its own memory space, file descriptors, and OS resources. A **thread** is a lightweight unit of execution within a process, sharing the process's memory and resources. Context switching between threads is faster than between processes. Java applications run in a single process but can have many threads.

---

**Q2. What is a race condition?**

**A:** A race condition occurs when the correctness of a program depends on the relative timing of thread execution. Two threads read-modify-write a shared variable without synchronization, leading to unpredictable results.

```java
// count++ is NOT atomic: it's read + increment + write
// Two threads doing this simultaneously can lose updates
private int count = 0;
public void increment() { count++; } // race condition
```

---

**Q3. What is the `volatile` keyword and what guarantees does it provide?**

**A:** `volatile` ensures that reads and writes to a variable go directly to main memory (bypassing CPU cache), providing **visibility** — changes made by one thread are immediately visible to others. However, it does NOT provide atomicity for compound operations like `count++`. Use `volatile` for simple flags or state variables where you need visibility but not atomicity.

---

**Q4. What is the difference between `volatile` and `synchronized`?**

**A:**
- `volatile`: Guarantees visibility only. No mutual exclusion. No atomicity for compound operations.
- `synchronized`: Guarantees both visibility AND mutual exclusion (atomicity). Only one thread can hold the monitor lock at a time.

Use `volatile` for single read/write operations where atomicity isn't needed. Use `synchronized` (or `ReentrantLock`) when you need atomic compound operations.

---

**Q5. What is the happens-before relationship?**

**A:** In Java's memory model, "happens-before" defines a partial ordering of actions. If action A happens-before action B, then all memory writes in A are visible to B. Established by:
- Thread start: `thread.start()` happens-before any action in the started thread.
- Thread join: all actions in a thread happen-before `thread.join()` returns.
- Monitor release: unlock happens-before subsequent lock by another thread.
- `volatile` write happens-before subsequent volatile read of the same variable.

---

**Q6. What is thread starvation vs deadlock vs livelock?**

**A:**
- **Deadlock**: Two or more threads each waiting for a lock held by the other — permanently blocked.
- **Starvation**: A thread is perpetually denied CPU time because other threads always get priority.
- **Livelock**: Threads are not blocked but keep responding to each other's actions and make no progress (like two people stepping aside for each other in a hallway).

---

## Thread Lifecycle & Management

**Q7. What are the states in a Java thread's lifecycle?**

**A:** `NEW` → `RUNNABLE` → `BLOCKED` / `WAITING` / `TIMED_WAITING` → `TERMINATED`
- **NEW**: Created, not yet started.
- **RUNNABLE**: Running or ready to run.
- **BLOCKED**: Waiting to acquire a monitor lock.
- **WAITING**: Waiting indefinitely for another thread (via `wait()`, `join()`, `park()`).
- **TIMED_WAITING**: Waiting for a specified time (`sleep()`, `wait(timeout)`, `join(timeout)`).
- **TERMINATED**: Finished execution.

---

**Q8. What is the difference between `sleep()`, `wait()`, `yield()`, and `join()`?**

**A:**
- `sleep(ms)`: Pauses the current thread for a time. Does NOT release locks. Static method on Thread.
- `wait()`: Releases the monitor lock and waits until `notify()`/`notifyAll()` is called. Must be in a `synchronized` block.
- `yield()`: Hints to the scheduler to let other threads run. May be ignored. Does not release locks.
- `join()`: The calling thread waits for the target thread to terminate.

---

## Java Concurrency Utilities

**Q9. What is an `ExecutorService` and what types of thread pools does `Executors` provide?**

**A:** `ExecutorService` manages a pool of threads, decoupling task submission from execution.

| Factory | Behavior |
|---|---|
| `newFixedThreadPool(n)` | Fixed number of threads |
| `newCachedThreadPool()` | Creates threads on demand; reuses idle ones |
| `newSingleThreadExecutor()` | Single thread; tasks are queued |
| `newScheduledThreadPool(n)` | For scheduling/delayed tasks |
| `newWorkStealingPool()` | ForkJoin-based, uses all CPUs |

---

**Q10. What is a `CountDownLatch` and when do you use it?**

**A:** A `CountDownLatch` is initialized with a count. Threads call `await()` to block until the count reaches zero. Other threads call `countDown()` to decrement. One-time use (cannot be reset).

Use case: "start all service workers simultaneously" or "wait for N tasks to finish before proceeding."

```java
CountDownLatch ready = new CountDownLatch(3);
// Each worker: ready.countDown();
// Main thread: ready.await(); // blocks until count = 0
```

---

**Q11. What is a `CyclicBarrier` and how is it different from `CountDownLatch`?**

**A:** `CyclicBarrier` makes a group of threads wait for each other to reach a common barrier point. Once all threads arrive, they're all released simultaneously (and optionally a barrier action runs). It can be **reset and reused** — unlike `CountDownLatch`.

Use case: multi-threaded simulations where each phase must complete before the next starts.

---

**Q12. What is a `Semaphore`?**

**A:** A `Semaphore` controls access to a shared resource by maintaining a count of permits. `acquire()` blocks if no permits are available; `release()` returns a permit. Used to implement resource pools, rate limiting, or bounded parallelism (e.g., max 5 DB connections).

---

**Q13. What is `BlockingQueue` and what implementations exist?**

**A:** `BlockingQueue` is a thread-safe queue where `put()` blocks if full and `take()` blocks if empty. Ideal for producer-consumer.

- `ArrayBlockingQueue`: Bounded, backed by array.
- `LinkedBlockingQueue`: Optionally bounded, backed by linked nodes. Higher throughput due to separate head/tail locks.
- `PriorityBlockingQueue`: Unbounded, elements ordered by priority.
- `SynchronousQueue`: No capacity. Each `put` must wait for a `take`.

---

## Locks

**Q14. What is `ReentrantLock` and how is it superior to `synchronized`?**

**A:** `ReentrantLock` provides the same mutual exclusion as `synchronized` but with more control:
- `tryLock()` — non-blocking attempt to acquire
- `tryLock(timeout)` — attempt with timeout (avoids deadlock)
- `lockInterruptibly()` — can be interrupted while waiting
- Fair locking option (prevents starvation)
- Multiple `Condition` objects on one lock

Always use in a try-finally to guarantee unlock.

---

**Q15. What is `ReadWriteLock`?**

**A:** `ReadWriteLock` maintains two locks: a read lock and a write lock. Multiple threads can hold the read lock simultaneously (no writers). Only one thread can hold the write lock (exclusive, blocks all readers). Improves throughput for read-heavy workloads.

---

## Atomic Variables

**Q16. What is Compare-And-Swap (CAS) and how do atomic variables use it?**

**A:** CAS is a CPU-level atomic instruction: "if the current value equals expected, set it to new value; otherwise fail." `AtomicInteger.incrementAndGet()` uses CAS in a loop:
1. Read current value.
2. Attempt CAS with expected=current, new=current+1.
3. If another thread changed it, retry.

Non-blocking and avoids lock overhead. Forms the basis of all `java.util.concurrent.atomic` classes.

---

**Q17. What is the ABA problem?**

**A:** CAS sees value A, then another thread changes A→B→A. When our thread does CAS expecting A, it succeeds even though the value changed in between. Use `AtomicStampedReference<T>` which compares both value and a version stamp, preventing false positives.

---

## Common Patterns

**Q18. What are the four Coffman conditions for deadlock?**

**A:**
1. **Mutual Exclusion**: Resources can't be shared (only one thread at a time).
2. **Hold and Wait**: A thread holds a resource while waiting for another.
3. **No Preemption**: Resources cannot be forcibly taken from a thread.
4. **Circular Wait**: A circular chain of threads each waiting for a resource held by the next.

Break any one to prevent deadlock. Common fix: acquire locks in a consistent global order.

---

**Q19. What is double-checked locking and is it safe in Java?**

**A:** Double-checked locking reduces synchronization overhead for lazy initialization:

```java
private volatile Singleton instance; // volatile is REQUIRED
public Singleton getInstance() {
    if (instance == null) {              // first check (no sync)
        synchronized(Singleton.class) {
            if (instance == null) {      // second check (with sync)
                instance = new Singleton();
            }
        }
    }
    return instance;
}
```

It's safe **only** with `volatile` (Java 5+). Without `volatile`, the JIT can reorder object construction, causing other threads to see a partially initialized instance.

---

**Q20. What is `ThreadLocal` and when do you use it?**

**A:** `ThreadLocal` provides a thread-specific variable — each thread gets its own independent copy. Common uses: per-thread database connections, user session data (in web frameworks), `SimpleDateFormat` (not thread-safe) instances.

**Important**: In thread pools, always call `ThreadLocal.remove()` after use, or values leak to the next task that reuses the thread.

---

**Q21. What is the fork/join framework?**

**A:** `ForkJoinPool` is designed for recursive, divide-and-conquer tasks. Tasks `fork()` (split) into sub-tasks and `join()` (merge) results. Uses **work stealing**: idle threads steal tasks from busy threads' queues. `parallelStream()` uses the common `ForkJoinPool` internally.

```java
class SumTask extends RecursiveTask<Long> {
    protected Long compute() {
        if (size <= THRESHOLD) return computeDirectly();
        SumTask left = new SumTask(array, start, mid);
        left.fork();
        SumTask right = new SumTask(array, mid, end);
        return right.compute() + left.join();
    }
}
```

---
