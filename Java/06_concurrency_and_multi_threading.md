# Concurrency & Multi-threading — Interview Notes 🧵

## 1. Introduction
**Concurrency** is the ability of a program to handle multiple tasks at the same time. **Multi-threading** is a specific form of concurrency where a single process spawns multiple threads to perform different tasks simultaneously.

- **Objective**: Improve application performance and responsiveness.
- **Concurrency vs. Parallelism**:
    - Concurrency is about *dealing* with lots of things at once (task-switching on one or more cores).
    - Parallelism is about *doing* lots of things at once (true simultaneous execution on multiple cores).

---

## 2. Process and Threads
- **Process**: An instance of a program being executed. It has its own memory space (Heap + Stack).
- **Thread**: A lightweight unit of execution within a process.
    - Threads share the **Heap** but each has its own **Stack**.
    - Context switching between threads is cheaper than between processes.

```
Process
├── Thread 1  (own Stack, registers, program counter)
├── Thread 2
└── Shared Heap (objects created with `new`)
```

---

## 3. Creating & Starting a Thread
Three ways — all call `.start()` (never `.run()` directly!):

```java
// a) Extend Thread — avoid (ties task to mechanism, no multiple inheritance)
class MyThread extends Thread {
    public void run() { System.out.println("Running: " + getName()); }
}
new MyThread().start();

// b) Implement Runnable — preferred (separates task from thread)
Thread t = new Thread(new DownloadFileTask(status));
t.start();

// c) Lambda (anonymous Runnable — most concise)
Thread t = new Thread(() -> System.out.println("Lambda thread"));
t.start();
```

> [!TIP]
> Prefer `Runnable` / lambda because Java supports only single inheritance, and it separates the task from the execution mechanism.

---

## 4. Thread Lifecycle

```
NEW ──start()──► RUNNABLE ──────────────► TERMINATED
                    │         ▲
              sleep/join/     │
              wait/block      │
                    ▼         │
               BLOCKED / WAITING / TIMED_WAITING
```

| State | Cause |
| :--- | :--- |
| `NEW` | Thread created but not started |
| `RUNNABLE` | Running or ready to run |
| `BLOCKED` | Waiting for a monitor lock |
| `WAITING` | `wait()`, `join()` with no timeout |
| `TIMED_WAITING` | `sleep(ms)`, `join(ms)`, `wait(ms)` |
| `TERMINATED` | `run()` has finished |

---

## 5. Sleep / Join / Interrupt

### `Thread.sleep(ms)`
Pauses the **current** thread. Does **not** release held locks.
```java
try {
    Thread.sleep(2000); // Timed-Waiting for 2 seconds
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore interrupted status
}
```

### `thread.join()`
Makes the calling thread wait until the target thread finishes.
```java
Thread worker = new Thread(() -> { /* heavy task */ });
worker.start();
worker.join(); // main thread blocks here until worker completes
System.out.println("Worker done, continuing main.");
```

### `thread.interrupt()`
Sends a cooperative signal — the thread must check and react.
```java
Thread longTask = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        // do work
    }
    System.out.println("Stopped gracefully.");
});
longTask.start();
Thread.sleep(100);
longTask.interrupt(); // signal to stop
```

> [!IMPORTANT]
> Always catch `InterruptedException` and call `Thread.currentThread().interrupt()` to restore the flag so callers can detect interruption.

---

## 6. Thread Metadata

```java
Thread t = new Thread(() -> {}, "my-worker");

t.getName();                      // "my-worker"
t.getId();                        // unique long
t.getState();                     // NEW / RUNNABLE / TERMINATED …

t.setPriority(Thread.MAX_PRIORITY); // 1–10 (default 5)
t.getPriority();

t.setDaemon(true);                // JVM exits when only daemon threads remain
t.isDaemon();                     // false by default

Thread.currentThread();           // reference to running thread
```

---

## 7. Concurrency Issues

The two core problems of multi-threading:

| Problem | Description | Example |
| :--- | :--- | :--- |
| **Visibility** | Thread reads stale value from CPU cache | Thread A writes, Thread B never sees new value |
| **Atomicity** | Compound operation interrupted mid-way | `count++` → Read → Increment → Write (3 ops) |

```java
// Race condition: count++ is NOT atomic
class UnsafeCounter {
    int count = 0;
    void increment() { count++; } // Read→Increment→Write can interleave!
}
// 10 threads × 1000 increments each → expected 10000, actual < 10000
```

---

## 8. Strategies for Thread Safety

| Strategy | Mechanism | When to use |
| :--- | :--- | :--- |
| **Immutability** | Final fields, no setters | Value objects, config |
| **Confinement** | `ThreadLocal<T>` | Per-thread state (e.g., formatters) |
| **Synchronization** | `synchronized`, `Lock` | Shared mutable state |
| **Atomic classes** | `AtomicInteger`, `LongAdder` | Simple counters |
| **Concurrent collections** | `ConcurrentHashMap`, `CopyOnWriteArrayList` | Shared collections |

---

## 9. `volatile` — Fixes Visibility (NOT Atomicity)
Forces all reads/writes to go directly to **main memory**, bypassing CPU caches.

```java
class VolatileFlag {
    volatile boolean running = true; // all threads see the latest value
}

// Thread 1 (writer):
flag.running = false;

// Thread 2 (reader):
while (flag.running) { /* loop exits as soon as flag is false */ }
```

> [!WARNING]
> `volatile` does **not** make compound operations atomic. `volatile int count; count++;` is still a race condition.

---

## 10. `synchronized` — Fixes Visibility + Atomicity
Uses **intrinsic locks (monitors)**. Only one thread can hold the lock at a time.

### Synchronized Method (locks `this`)
```java
public class SafeCounter {
    private int count = 0;

    public synchronized void increment() { count++; }
    public synchronized int getCount()   { return count; }
}
```

### Synchronized Block (finer-grained — preferred)
```java
public class SharedResource {
    private int count = 0;
    private final Object lock = new Object(); // dedicated lock object

    public void increment() {
        synchronized (lock) { // only the critical section is locked
            count++;
        }
        // other non-critical work can proceed here without blocking
    }
}
```

> [!TIP]
> Use a **private final Object** as the lock instead of `this` to prevent external code from accidentally acquiring your lock.

---

## 11. `ReentrantLock` — Explicit Locking
Part of `java.util.concurrent.locks`. More flexible than `synchronized`.

```java
Lock lock = new ReentrantLock(true); // fair=true → FIFO waiting order

lock.lock();
try {
    // critical section
} finally {
    lock.unlock(); // ALWAYS in finally!
}

// tryLock — non-blocking: returns false if lock unavailable
if (lock.tryLock()) {
    try { /* critical section */ }
    finally { lock.unlock(); }
} else {
    System.out.println("Could not acquire lock, doing something else.");
}
```

| Feature | `synchronized` | `ReentrantLock` |
| :--- | :---: | :---: |
| Automatic unlock | ✅ | ❌ (manual finally) |
| `tryLock()` | ❌ | ✅ |
| Timeout on lock | ❌ | ✅ |
| Fairness option | ❌ | ✅ |
| Multiple conditions | ❌ | ✅ |

---

## 12. `AtomicInteger` / `AtomicBoolean` — Lock-free CAS
Uses **Compare-And-Swap** CPU instructions — no blocking, highly efficient.

```java
AtomicInteger count = new AtomicInteger(0);

count.incrementAndGet();       // atomic ++count
count.getAndIncrement();       // atomic count++
count.addAndGet(5);            // atomic count += 5
count.get();                   // read current value

// CAS — set to newVal only if current value == expected
boolean updated = count.compareAndSet(10_000, 0); // if(count==10000) count=0
```

> [!NOTE]
> CAS is faster than `synchronized` under **low to moderate contention**. Under very high contention, consider `LongAdder`.

---

## 13. `LongAdder` — High-Contention Counters
Maintains **a set of internal cells** to distribute contention. Sums them on `sum()`.

```java
LongAdder hitCounter = new LongAdder();

// Multiple threads call this concurrently — very low contention
hitCounter.increment();

// Read the total (more expensive, called infrequently)
long total = hitCounter.sum();
```

| | `AtomicLong` | `LongAdder` |
| :--- | :---: | :---: |
| Write performance (high contention) | ❌ Slow | ✅ Fast |
| Read (sum) performance | ✅ Instant | ❌ Slightly costly |
| Suitable for | CAS logic | Pure counters |

---

## 14. `ThreadLocal` — Thread Confinement
Each thread has its own **independent copy** of the variable — no sharing, no locking needed.

```java
// Classic use: non-thread-safe objects (e.g., SimpleDateFormat)
ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(
    () -> new SimpleDateFormat("yyyy-MM-dd")
);

// Each thread gets its own instance
String date = formatter.get().format(new Date());

// ALWAYS remove to prevent memory leaks in thread-pool scenarios
formatter.remove();
```

---

## 15. `wait()` / `notify()` / `notifyAll()` — Low-level Signalling
> [!IMPORTANT]
> These methods **MUST** be called inside a `synchronized` block on the same monitor object.

```java
final Object monitor = new Object();

// Thread A (waiter)
synchronized (monitor) {
    while (!condition) {           // ALWAYS use while (not if!) — spurious wakeups!
        monitor.wait();            // releases lock and suspends
    }
    // proceed after being notified
}

// Thread B (notifier)
synchronized (monitor) {
    condition = true;
    monitor.notify();    // wake ONE waiting thread
    // monitor.notifyAll(); // wake ALL waiting threads (usually safer)
}
```

---

## 16. Producer-Consumer Pattern

```java
final int CAPACITY = 3;
final List<Integer> buffer = new ArrayList<>();
final Object lock = new Object();

// Producer
new Thread(() -> {
    int item = 0;
    while (item < 10) {
        synchronized (lock) {
            while (buffer.size() == CAPACITY) lock.wait(); // full — wait
            buffer.add(++item);
            lock.notifyAll();
        }
    }
}).start();

// Consumer
new Thread(() -> {
    int received = 0;
    while (received < 10) {
        synchronized (lock) {
            while (buffer.isEmpty()) lock.wait(); // empty — wait
            int val = buffer.remove(0);
            received++;
            lock.notifyAll();
        }
    }
}).start();
```

---

## 17. Synchronized Collections
`Collections.synchronizedXxx()` wraps any collection with a **single global lock**.

```java
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
syncList.add("item"); // thread-safe (one lock for every method call)

// CRITICAL: iteration must be explicitly synchronized
synchronized (syncList) {
    for (String s : syncList) System.out.println(s);
}
```

> [!WARNING]
> Single global lock → low throughput under high concurrency. Prefer `ConcurrentHashMap` / `CopyOnWriteArrayList` instead.

---

## 18. Concurrent Collections
Found in `java.util.concurrent` — designed for high-throughput concurrent access.

### `ConcurrentHashMap`
Uses **lock stripping** (per-bucket locks) — multiple threads read/write different buckets simultaneously.

```java
var map = new ConcurrentHashMap<String, Integer>();
map.put("a", 1);                           // thread-safe
map.merge("a", 10, Integer::sum);          // atomic read-modify-write → a = 11
map.computeIfAbsent("b", k -> k.length()); // atomic lazy init → b = 1
```

### `CopyOnWriteArrayList`
Every **write** makes a fresh copy of the backing array. **Reads are lock-free**.

```java
var list = new CopyOnWriteArrayList<String>();
list.add("a");  // creates a new array copy
// Iteration is always safe — iterates the snapshot taken at iterator creation
list.forEach(System.out::println);
```

| | `synchronizedList` | `CopyOnWriteArrayList` |
| :--- | :---: | :---: |
| Read performance | ❌ Locked | ✅ Lock-free |
| Write performance | ✅ In-place | ❌ Array copy |
| Best for | Frequent writes | Frequent reads, rare writes |

---

## 19. Deadlock
Deadlock occurs when **Thread A** holds **Lock 1** and waits for **Lock 2**, while **Thread B** holds **Lock 2** and waits for **Lock 1**.

### Deadlock Example
```java
// Thread A: acquires lock1, then tries to acquire lock2
// Thread B: acquires lock2, then tries to acquire lock1
// → Both block forever!
```

### Prevention Strategies
```java
// 1. Consistent lock ordering — ALWAYS acquire locks in the same order
synchronized (lock1) {
    synchronized (lock2) { /* both threads follow this order */ }
}

// 2. ReentrantLock.tryLock() — back off if lock unavailable
if (lock2.tryLock(100, TimeUnit.MILLISECONDS)) {
    try { /* critical section */ } finally { lock2.unlock(); }
} else {
    System.out.println("Could not acquire lock2, backing off.");
}

// 3. Avoid nested locks (use a single coarse-grained lock)
// 4. Use higher-level concurrency utilities (Executor, CompletableFuture)
```

---

## 20. Summary Table

| Feature | Visibility ✅ | Atomicity ✅ | Blocks Thread | Use When |
| :--- | :---: | :---: | :---: | :--- |
| `volatile` | ✅ | ❌ | ❌ | Simple flag, stop signal |
| `synchronized` | ✅ | ✅ | ✅ | Shared mutable state |
| `ReentrantLock` | ✅ | ✅ | ✅ | Need `tryLock`, timeout, fairness |
| `AtomicInteger` | ✅ | ✅ | ❌ (CAS) | Simple counters, CAS logic |
| `LongAdder` | ✅ | ✅ | ❌ | High-contention pure counters |
| `ThreadLocal` | — | — | ❌ | Per-thread state, no sharing |
| `ConcurrentHashMap` | ✅ | ✅ | ❌ | Shared map, high throughput |
| `CopyOnWriteArrayList`| ✅ | ✅ | ❌ (reads) | Shared list, reads >> writes |

> [!IMPORTANT]
> **Key Interview Rules**:
> 1. Never call `.run()` directly — always `.start()`.
> 2. Never `notify()` outside a `synchronized` block.
> 3. Always use `while` (not `if`) around `wait()` — guard against spurious wakeups.
> 4. Always `unlock()` in a `finally` block with `ReentrantLock`.
> 5. Always call `threadLocal.remove()` in thread-pool threads to avoid memory leaks.
> 6. Consistent lock ordering is the simplest deadlock prevention.
