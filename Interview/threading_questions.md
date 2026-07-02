# Threading Concepts — Interview Questions

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

1. What is the difference between a process and a thread?
2. What is a race condition? Give an example.
3. What is thread safety? How do you achieve it in Java?
4. What is the difference between `synchronized` methods and `synchronized` blocks?
5. What is a monitor in Java? How does it relate to `synchronized`?
6. What is the `volatile` keyword? What guarantees does it provide?
7. What is the difference between `volatile` and `synchronized`?
8. What is the happens-before relationship in Java's memory model?
9. What is thread starvation and how is it different from a deadlock?
10. What is a livelock? Give an example.

---

## Thread Lifecycle & Management

11. What are the states in a Java thread's lifecycle?
12. What is the difference between `sleep()`, `wait()`, `yield()`, and `join()`?
13. What is the difference between `notify()` and `notifyAll()`?
14. What is a daemon thread? How do you create one?
15. How do you stop a thread safely in Java?
16. What is thread interruption? How does `isInterrupted()` differ from `interrupted()`?

---

## Java Concurrency Utilities (`java.util.concurrent`)

17. What is an `ExecutorService`? How is it different from manually creating threads?
18. What are the different types of thread pools provided by `Executors`? When do you use each?
19. What is a `Future` and a `Callable`? How do they differ from `Runnable`?
20. What is `CompletableFuture`? How does it support non-blocking async programming?
21. What is a `CountDownLatch`? Give a real-world use case.
22. What is a `CyclicBarrier`? How is it different from a `CountDownLatch`?
23. What is a `Semaphore` and when would you use it?
24. What is a `Phaser` and how does it generalize `CyclicBarrier`?
25. What is `BlockingQueue`? Name three implementations and their differences.
26. What is the difference between `LinkedBlockingQueue` and `ArrayBlockingQueue`?

---

## Locks & Synchronizers

27. What is `ReentrantLock`? How is it superior to `synchronized`?
28. What is the difference between a fair and an unfair lock?
29. What is `ReadWriteLock` and when does it improve performance?
30. What is `StampedLock` and how does it support optimistic reads?
31. What is a `LockSupport` class?
32. What is the `tryLock()` method and why is it useful to avoid deadlocks?

---

## Atomic Variables & CAS

33. What are atomic variables in Java? Name three examples.
34. What is Compare-And-Swap (CAS)? How do `AtomicInteger` operations use it internally?
35. What is the ABA problem in CAS? How does `AtomicStampedReference` solve it?
36. When would you use `LongAdder` over `AtomicLong`?

---

## Concurrent Collections

37. What is `ConcurrentHashMap` and how does it differ from `HashMap` and `Hashtable`?
38. What is `CopyOnWriteArrayList`? When is it useful?
39. What is `ConcurrentLinkedQueue`?

---

## Common Problems & Patterns

40. What is a deadlock? How do you detect and prevent it?
41. What is the producer-consumer problem? How do you implement it in Java?
42. What is the dining philosophers problem and what does it illustrate?
43. What is double-checked locking? Is it safe in Java? How do you fix it?
44. What is the ThreadLocal class? When would you use it?
45. What are the four conditions necessary for a deadlock (Coffman conditions)?

---

## Performance

46. What is context switching overhead and how does it affect multi-threaded performance?
47. What is cache line false sharing? How do you avoid it?
48. What is the fork/join framework in Java? How does work-stealing work?
49. When does adding more threads hurt performance instead of helping?
50. What is thread confinement as a concurrency strategy?

---
