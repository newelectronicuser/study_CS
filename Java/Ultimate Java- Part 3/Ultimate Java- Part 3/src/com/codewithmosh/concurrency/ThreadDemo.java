package com.codewithmosh.concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * =================================================================
 *  ThreadDemo — Interview-Ready Java Concurrency Cheat-Sheet
 * =================================================================
 *
 *  Topics covered:
 *   1.  Creating & starting threads (extend Thread vs Runnable vs lambda)
 *   2.  Thread lifecycle — sleep / join / interrupt
 *   3.  Thread metadata (name, id, priority, daemon)
 *   4.  Race condition demo (Visibility + Atomicity problems)
 *   5.  volatile — fixes Visibility, NOT Atomicity
 *   6.  synchronized method — fixes both
 *   7.  synchronized block — finer-grained locking
 *   8.  ReentrantLock — explicit locking with tryLock / fair locking
 *   9.  AtomicInteger / AtomicBoolean — lock-free CAS operations
 *  10.  LongAdder — best for high-contention counters
 *  11.  ThreadLocal — thread confinement
 *  12.  wait() / notify() / notifyAll() — low-level signalling
 *  13.  Producer-Consumer pattern (using wait/notifyAll)
 *  14.  Synchronized collections (Collections.synchronizedList)
 *  15.  Concurrent collections (ConcurrentHashMap, CopyOnWriteArrayList)
 *  16.  Deadlock demo + prevention tip
 */
public class ThreadDemo {

    // =================================================================
    //  MAIN entry point
    // =================================================================
    public static void show() throws InterruptedException {
        System.out.println("\n=== 1. CREATING & STARTING THREADS ===");
        creatingThreads();

        System.out.println("\n=== 2. THREAD LIFECYCLE (sleep / join / interrupt) ===");
        threadLifecycle();

        System.out.println("\n=== 3. THREAD METADATA ===");
        threadMetadata();

        System.out.println("\n=== 4. RACE CONDITION (Atomicity failure) ===");
        raceConditionDemo();

        System.out.println("\n=== 5. volatile (Visibility fix) ===");
        volatileDemo();

        System.out.println("\n=== 6. synchronized METHOD ===");
        synchronizedMethodDemo();

        System.out.println("\n=== 7. synchronized BLOCK ===");
        synchronizedBlockDemo();

        System.out.println("\n=== 8. ReentrantLock ===");
        reentrantLockDemo();

        System.out.println("\n=== 9. AtomicInteger / AtomicBoolean ===");
        atomicDemo();

        System.out.println("\n=== 10. LongAdder ===");
        longAdderDemo();

        System.out.println("\n=== 11. ThreadLocal (Thread Confinement) ===");
        threadLocalDemo();

        System.out.println("\n=== 12. wait() / notify() ===");
        waitNotifyDemo();

        System.out.println("\n=== 13. PRODUCER-CONSUMER ===");
        producerConsumerDemo();

        System.out.println("\n=== 14. Synchronized Collections ===");
        synchronizedCollectionsDemo();

        System.out.println("\n=== 15. Concurrent Collections ===");
        concurrentCollectionsDemo();

        System.out.println("\n=== 16. DEADLOCK demo ===");
        deadlockPreventionDemo();
    }

    // =================================================================
    //  1. CREATING & STARTING THREADS
    // =================================================================
    private static void creatingThreads() throws InterruptedException {
        // a) Extending Thread class (avoid — ties task to mechanism)
        class MyThread extends Thread {
            @Override public void run() {
                System.out.println("MyThread (extend) — " + getName());
            }
        }
        Thread t1 = new MyThread();
        t1.start();

        // b) Implementing Runnable (preferred — separates task from thread)
        var status = new DownloadStatus();
        Thread t2 = new Thread(new DownloadFileTask(status));
        t2.start();

        // c) Lambda (anonymous Runnable — most concise)
        Thread t3 = new Thread(() -> System.out.println("Lambda thread — " + Thread.currentThread().getName()));
        t3.start();

        t1.join(); t2.join(); t3.join(); // wait for all to finish
    }

    // =================================================================
    //  2. THREAD LIFECYCLE — sleep / join / interrupt
    // =================================================================
    private static void threadLifecycle() throws InterruptedException {
        // ----- sleep() -----
        Thread sleeper = new Thread(() -> {
            System.out.println("Sleeper: going to sleep...");
            try {
                Thread.sleep(500); // does NOT release locks
            } catch (InterruptedException e) {
                System.out.println("Sleeper: interrupted during sleep!");
                // Restore the interrupted status so callers can detect it
                Thread.currentThread().interrupt();
            }
            System.out.println("Sleeper: awoke!");
        });
        sleeper.start();
        sleeper.join(); // main waits until sleeper finishes

        // ----- join() -----
        Thread worker = new Thread(() -> {
            System.out.println("Worker: doing heavy task...");
            try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println("Worker: done.");
        });
        worker.start();
        worker.join(); // blocks main thread until worker finishes
        System.out.println("Main: worker has finished — continuing.");

        // ----- interrupt() -----
        Thread longTask = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                // simulate work
            }
            System.out.println("LongTask: stopped gracefully via interrupt.");
        });
        longTask.start();
        Thread.sleep(10); // let it run briefly
        longTask.interrupt(); // signal to stop
        longTask.join();
    }

    // =================================================================
    //  3. THREAD METADATA
    // =================================================================
    private static void threadMetadata() {
        Thread t = new Thread(() -> {}, "my-worker");

        // Name & ID
        System.out.println("Name: " + t.getName());
        System.out.println("ID:   " + t.getId());

        // Priority (1 = MIN, 5 = NORM, 10 = MAX)
        t.setPriority(Thread.MAX_PRIORITY);
        System.out.println("Priority: " + t.getPriority());

        // Daemon threads — JVM exits when only daemons remain
        t.setDaemon(true);
        System.out.println("Is daemon: " + t.isDaemon());

        // Current thread
        Thread current = Thread.currentThread();
        System.out.println("Current thread: " + current.getName() + " (id=" + current.getId() + ")");

        // State (before starting it is NEW)
        System.out.println("State: " + t.getState()); // NEW
        t.start();
        System.out.println("State: " + t.getState()); // RUNNABLE or TERMINATED (it's an empty task)
    }

    // =================================================================
    //  4. RACE CONDITION — Atomicity failure
    // =================================================================
    private static void raceConditionDemo() throws InterruptedException {
        // UnsafeCounter: count++ is not atomic (Read → Increment → Write)
        // Under concurrent access, increments can be lost.
        class UnsafeCounter {
            int count = 0;
            void increment() { count++; } // NOT thread-safe!
        }

        var counter = new UnsafeCounter();
        var threads = new ArrayList<Thread>();

        for (int i = 0; i < 10; i++) {
            var t = new Thread(() -> {
                for (int j = 0; j < 1_000; j++) counter.increment();
            });
            threads.add(t);
            t.start();
        }
        for (var t : threads) t.join();

        // Expected: 10_000 — Actual: likely LESS due to lost updates
        System.out.println("Unsafe count (expected 10000): " + counter.count);
    }

    // =================================================================
    //  5. volatile — Fixes VISIBILITY, NOT Atomicity
    // =================================================================
    private static void volatileDemo() throws InterruptedException {
        // volatile ensures reads/writes go directly to main memory (no CPU-cache stale reads)
        class VolatileFlag {
            volatile boolean running = true; // visible across threads
        }

        var flag = new VolatileFlag();

        Thread worker = new Thread(() -> {
            System.out.println("Worker: started.");
            while (flag.running) { /* busy loop */ }
            System.out.println("Worker: stopped (saw flag=false).");
        });
        worker.start();
        Thread.sleep(20);
        flag.running = false; // visible immediately to the worker thread
        worker.join();

        // NOTE: volatile does NOT make count++ atomic!
    }

    // =================================================================
    //  6. synchronized METHOD — Fixes Visibility + Atomicity
    // =================================================================
    private static void synchronizedMethodDemo() throws InterruptedException {
        class SafeCounter {
            private int count = 0;

            // Locks `this` object — only ONE thread inside at a time
            public synchronized void increment() { count++; }
            public synchronized int getCount()   { return count; }
        }

        var counter = new SafeCounter();
        var threads = new ArrayList<Thread>();

        for (int i = 0; i < 10; i++) {
            var t = new Thread(() -> {
                for (int j = 0; j < 1_000; j++) counter.increment();
            });
            threads.add(t);
            t.start();
        }
        for (var t : threads) t.join();
        System.out.println("Synchronized count (expected 10000): " + counter.getCount());
    }

    // =================================================================
    //  7. synchronized BLOCK — Finer-grained locking
    // =================================================================
    private static void synchronizedBlockDemo() throws InterruptedException {
        class SharedResource {
            private int count = 0;
            private final Object lock = new Object(); // dedicated lock object

            public void increment() {
                // Only the critical section is locked — threads can do other work outside
                synchronized (lock) {
                    count++;
                }
            }

            public int getCount() { return count; }
        }

        var res = new SharedResource();
        var threads = new ArrayList<Thread>();

        for (int i = 0; i < 5; i++) {
            var t = new Thread(() -> {
                for (int j = 0; j < 2_000; j++) res.increment();
            });
            threads.add(t);
            t.start();
        }
        for (var t : threads) t.join();
        System.out.println("Sync-block count (expected 10000): " + res.getCount());
    }

    // =================================================================
    //  8. ReentrantLock — Explicit lock with tryLock / timeout / fair
    // =================================================================
    private static void reentrantLockDemo() throws InterruptedException {
        class LockCounter {
            private int count = 0;
            // fair=true → waiting threads served in FIFO order
            private final Lock lock = new ReentrantLock(true);

            public void increment() {
                lock.lock();
                try {
                    count++;
                } finally {
                    lock.unlock(); // ALWAYS unlock in finally!
                }
            }

            // tryLock — non-blocking: returns false if lock is unavailable
            public boolean tryIncrement() {
                if (lock.tryLock()) {
                    try { count++; return true; }
                    finally { lock.unlock(); }
                }
                return false;
            }

            public int getCount() { return count; }
        }

        var lc = new LockCounter();
        var threads = new ArrayList<Thread>();

        for (int i = 0; i < 10; i++) {
            var t = new Thread(() -> {
                for (int j = 0; j < 1_000; j++) lc.increment();
            });
            threads.add(t);
            t.start();
        }
        for (var t : threads) t.join();
        System.out.println("ReentrantLock count (expected 10000): " + lc.getCount());
        System.out.println("tryLock succeeded: " + lc.tryIncrement());
    }

    // =================================================================
    //  9. AtomicInteger / AtomicBoolean — CAS (lock-free)
    // =================================================================
    private static void atomicDemo() throws InterruptedException {
        AtomicInteger atomicCount = new AtomicInteger(0);

        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            var t = new Thread(() -> {
                for (int j = 0; j < 1_000; j++) {
                    atomicCount.incrementAndGet();     // atomic i++
                }
            });
            threads.add(t);
            t.start();
        }
        for (var t : threads) t.join();
        System.out.println("AtomicInteger count (expected 10000): " + atomicCount.get());

        // compareAndSet — set only if current value matches expected
        boolean updated = atomicCount.compareAndSet(10_000, 0);
        System.out.println("CAS reset to 0: " + updated + " → value=" + atomicCount.get());

        // Other useful methods
        System.out.println("getAndIncrement: " + atomicCount.getAndIncrement()); // post-increment
        System.out.println("addAndGet(5):    " + atomicCount.addAndGet(5));
    }

    // =================================================================
    //  10. LongAdder — Best for high-contention counters
    // =================================================================
    private static void longAdderDemo() throws InterruptedException {
        // Under high contention, LongAdder outperforms AtomicLong by distributing
        // updates across internal cells and summing them only on sum()/longValue().
        LongAdder adder = new LongAdder();

        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            var t = new Thread(() -> {
                for (int j = 0; j < 1_000; j++) adder.increment();
            });
            threads.add(t);
            t.start();
        }
        for (var t : threads) t.join();
        System.out.println("LongAdder sum (expected 10000): " + adder.sum());

        // Also used in DownloadStatus (LongAdder totalBytes)
        var status = new DownloadStatus();
        status.incrementTotalBytes();
        status.incrementTotalBytes();
        System.out.println("DownloadStatus totalBytes: " + status.getTotalBytes());
    }

    // =================================================================
    //  11. ThreadLocal — Thread Confinement
    // =================================================================
    private static void threadLocalDemo() throws InterruptedException {
        // Each thread gets its own independent copy of the variable
        ThreadLocal<Integer> threadId = ThreadLocal.withInitial(() -> (int)(Math.random() * 100));

        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName()
                + " → threadLocal value = " + threadId.get());
            threadId.remove(); // Always remove to prevent memory leaks in thread-pool scenarios
        };

        Thread t1 = new Thread(task, "worker-1");
        Thread t2 = new Thread(task, "worker-2");
        t1.start(); t2.start();
        t1.join();  t2.join();
        // Each thread prints a different random value — they don't share state
    }

    // =================================================================
    //  12. wait() / notify() — Low-level thread signalling
    // =================================================================
    private static void waitNotifyDemo() throws InterruptedException {
        // RULES: wait/notify/notifyAll MUST be called inside synchronized block
        final Object monitor = new Object();
        final boolean[] ready = {false};

        Thread producer = new Thread(() -> {
            synchronized (monitor) {
                System.out.println("Producer: preparing data...");
                ready[0] = true;
                monitor.notify(); // Wake up the consumer
                System.out.println("Producer: notified consumer.");
            }
        });

        Thread consumer = new Thread(() -> {
            synchronized (monitor) {
                while (!ready[0]) {       // Use while-loop (not if!) to guard against spurious wakeups
                    try {
                        System.out.println("Consumer: waiting...");
                        monitor.wait(); // releases the lock and waits
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("Consumer: data is ready, processing...");
            }
        });

        consumer.start(); // consumer starts first and goes into wait
        Thread.sleep(50); // ensure consumer is waiting before producer runs
        producer.start();
        consumer.join(); producer.join();
    }

    // =================================================================
    //  13. PRODUCER-CONSUMER — Classic pattern with wait/notifyAll
    // =================================================================
    private static void producerConsumerDemo() throws InterruptedException {
        final int CAPACITY = 3;
        final List<Integer> buffer = new ArrayList<>();
        final Object lock = new Object();

        Thread producer = new Thread(() -> {
            int item = 0;
            while (item < 6) {
                synchronized (lock) {
                    while (buffer.size() == CAPACITY) {
                        try { lock.wait(); } // buffer full — wait
                        catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                    }
                    buffer.add(++item);
                    System.out.println("Produced: " + item + " | buffer=" + buffer);
                    lock.notifyAll(); // wake up consumers
                }
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            int received = 0;
            while (received < 6) {
                synchronized (lock) {
                    while (buffer.isEmpty()) {
                        try { lock.wait(); } // buffer empty — wait
                        catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                    }
                    int item = buffer.remove(0);
                    received++;
                    System.out.println("Consumed: " + item + " | buffer=" + buffer);
                    lock.notifyAll(); // wake up producer
                }
            }
        }, "Consumer");

        producer.start(); consumer.start();
        producer.join();  consumer.join();
    }

    // =================================================================
    //  14. Synchronized Collections
    // =================================================================
    private static void synchronizedCollectionsDemo() throws InterruptedException {
        // Collections.synchronizedList wraps any list with a mutex on every method.
        // Drawback: one global lock → low throughput under high concurrency.
        List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());

        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 5; i++) {
            final int val = i;
            threads.add(new Thread(() -> syncList.add(val)));
        }
        threads.forEach(Thread::start);
        for (var t : threads) t.join();

        // IMPORTANT: iteration MUST be manually synchronized
        synchronized (syncList) {
            System.out.println("SynchronizedList: " + syncList);
        }
    }

    // =================================================================
    //  15. Concurrent Collections
    // =================================================================
    private static void concurrentCollectionsDemo() throws InterruptedException {
        // ----- ConcurrentHashMap -----
        // Uses lock-stripping (bucket-level locking) — high throughput
        var map = new ConcurrentHashMap<String, Integer>();
        var mapThreads = new ArrayList<Thread>();

        for (int i = 0; i < 5; i++) {
            final int idx = i;
            mapThreads.add(new Thread(() -> map.put("key" + idx, idx)));
        }
        mapThreads.forEach(Thread::start);
        for (var t : mapThreads) t.join();
        System.out.println("ConcurrentHashMap: " + map);

        // merge — atomically compute or update a key
        map.merge("key0", 10, Integer::sum);
        System.out.println("After merge key0: " + map.get("key0")); // 0 + 10 = 10

        // computeIfAbsent — thread-safe lazy init
        map.computeIfAbsent("newKey", k -> k.length());
        System.out.println("computeIfAbsent newKey: " + map.get("newKey")); // 6

        // ----- CopyOnWriteArrayList -----
        // Writes make a fresh copy of the array → lock-free reads
        // Best when reads >> writes (e.g., event-listener lists)
        var cowList = new CopyOnWriteArrayList<String>();
        Thread writer = new Thread(() -> cowList.add("written-by-thread"));
        Thread reader = new Thread(() -> cowList.forEach(System.out::println));

        cowList.add("initial");
        writer.start(); reader.start();
        writer.join();  reader.join();
        System.out.println("CopyOnWriteArrayList: " + cowList);
    }

    // =================================================================
    //  16. DEADLOCK — demo + prevention strategy
    // =================================================================
    private static void deadlockPreventionDemo() {
        // Deadlock occurs when Thread-A holds Lock-1 and waits for Lock-2,
        // while Thread-B holds Lock-2 and waits for Lock-1.

        final Object lock1 = new Object();
        final Object lock2 = new Object();

        // DEADLOCK-SAFE version: always acquire locks in the SAME order
        // (lock1 first, then lock2 — in BOTH threads)
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("T1: acquired lock1");
                synchronized (lock2) { // consistent order — no deadlock
                    System.out.println("T1: acquired lock2");
                }
            }
        }, "safe-t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock1) { // same order as T1 — T2 blocks until T1 releases lock1
                System.out.println("T2: acquired lock1");
                synchronized (lock2) {
                    System.out.println("T2: acquired lock2");
                }
            }
        }, "safe-t2");

        t1.start(); t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println("No deadlock — consistent lock-ordering worked!");

        // Other prevention strategies:
        // 1. Use ReentrantLock.tryLock(timeout) — back off if lock unavailable
        // 2. Use a single coarse-grained lock (simpler but less parallel)
        // 3. Avoid nested locks wherever possible
    }
}
