package com.codewithmosh.executors;

import java.util.List;
import java.util.concurrent.*;

/**
 * ================================================================
 *  ExecutorsDemo — Interview-Ready Executor Framework Cheat-Sheet
 * ================================================================
 *
 *  Topics covered:
 *   1.  Why ExecutorService? (vs raw threads)
 *   2.  newFixedThreadPool    — bounded worker pool
 *   3.  newCachedThreadPool   — elastic pool
 *   4.  newSingleThreadExecutor — serialised tasks
 *   5.  newScheduledThreadPool — delay / periodic tasks
 *   6.  execute() vs submit()
 *   7.  Callable<V> + Future<V> — result-bearing tasks
 *   8.  Future API — get / isDone / cancel / isCancelled
 *   9.  invokeAll() — run many Callables, collect all results
 *  10.  invokeAny() — run many Callables, take fastest result
 *  11.  Graceful shutdown — shutdown / shutdownNow / awaitTermination
 *  12.  ThreadPoolExecutor — custom pool configuration
 */
public class ExecutorsDemo {

    // =============================================================
    //  MAIN entry point
    // =============================================================
    public static void show() throws InterruptedException, ExecutionException {
        System.out.println("\n--- 1. Fixed Thread Pool ---");
        fixedThreadPoolDemo();

        System.out.println("\n--- 2. Cached Thread Pool ---");
        cachedThreadPoolDemo();

        System.out.println("\n--- 3. Single-Thread Executor ---");
        singleThreadDemo();

        System.out.println("\n--- 4. Scheduled Thread Pool ---");
        scheduledPoolDemo();

        System.out.println("\n--- 5. execute() vs submit() ---");
        executeVsSubmitDemo();

        System.out.println("\n--- 6. Callable + Future ---");
        callableFutureDemo();

        System.out.println("\n--- 7. Future API (isDone / cancel) ---");
        futureApiDemo();

        System.out.println("\n--- 8. invokeAll() ---");
        invokeAllDemo();

        System.out.println("\n--- 9. invokeAny() ---");
        invokeAnyDemo();

        System.out.println("\n--- 10. Graceful Shutdown ---");
        shutdownDemo();

        System.out.println("\n--- 11. Custom ThreadPoolExecutor ---");
        customThreadPoolDemo();
    }

    // =============================================================
    //  1. Fixed Thread Pool
    //     Bounded pool — at most N threads run concurrently.
    //     Excess tasks wait in a LinkedBlockingQueue.
    //     Use when: predictable, CPU-bound workloads.
    // =============================================================
    private static void fixedThreadPoolDemo() throws InterruptedException {
        // Rule: pool size ≈ number of CPU cores for CPU-bound tasks
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        try {
            for (int i = 1; i <= 5; i++) {
                final int taskId = i;
                executor.execute(() -> {
                    System.out.println("FixedPool task " + taskId
                        + " on " + Thread.currentThread().getName());
                    LongTask.simulate(200);
                });
            }
        } finally {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        }
    }

    // =============================================================
    //  2. Cached Thread Pool
    //     Grows on demand, reuses idle threads (60 s idle timeout).
    //     Use when: many SHORT-LIVED tasks / I/O-bound work.
    //     WARNING: can create thousands of threads under load.
    // =============================================================
    private static void cachedThreadPoolDemo() throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            for (int i = 1; i <= 5; i++) {
                final int taskId = i;
                executor.execute(() ->
                    System.out.println("CachedPool task " + taskId
                        + " on " + Thread.currentThread().getName()));
            }
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // =============================================================
    //  3. Single-Thread Executor
    //     Exactly ONE worker thread; tasks run sequentially in order.
    //     Use when: you need guaranteed ordering without synchronization.
    // =============================================================
    private static void singleThreadDemo() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            for (int i = 1; i <= 4; i++) {
                final int id = i;
                executor.execute(() ->
                    System.out.println("SingleThread task " + id
                        + " on " + Thread.currentThread().getName()));
            }
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // =============================================================
    //  4. Scheduled Thread Pool
    //     Runs tasks after a delay OR at a fixed rate/interval.
    // =============================================================
    private static void scheduledPoolDemo() throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        try {
            // a) One-shot with delay
            scheduler.schedule(
                () -> System.out.println("Delayed task fired after 500 ms"),
                500, TimeUnit.MILLISECONDS
            );

            // b) Fixed-rate: fires every 300 ms regardless of task duration
            ScheduledFuture<?> fixedRate = scheduler.scheduleAtFixedRate(
                () -> System.out.println("Fixed-rate tick  @ " + System.currentTimeMillis()),
                0L, 300L, TimeUnit.MILLISECONDS
            );

            // c) Fixed-delay: waits 300 ms AFTER each task finishes
            ScheduledFuture<?> fixedDelay = scheduler.scheduleWithFixedDelay(
                () -> System.out.println("Fixed-delay tick @ " + System.currentTimeMillis()),
                0L, 300L, TimeUnit.MILLISECONDS
            );

            Thread.sleep(1_000); // let it fire a few times
            fixedRate.cancel(false);
            fixedDelay.cancel(false);
        } finally {
            scheduler.shutdown();
            scheduler.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    // =============================================================
    //  5. execute() vs submit()
    //     execute(Runnable)        → void, no result, swallows exceptions
    //     submit(Runnable)         → Future<?>, result is null on success
    //     submit(Callable<V>)      → Future<V>, carries the return value
    //     submit(Runnable, result) → Future<T>, returns the given result
    // =============================================================
    private static void executeVsSubmitDemo() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            // execute — fire and forget; uncaught exceptions go to UncaughtExceptionHandler
            executor.execute(() -> System.out.println("execute: no return value"));

            // submit(Runnable) — Future<?> but result is always null
            Future<?> f1 = executor.submit(() -> System.out.println("submit(Runnable)"));
            System.out.println("submit(Runnable) future.get(): " + f1.get()); // null

            // submit(Callable) — Future carries the actual return value
            Future<Integer> f2 = executor.submit(() -> {
                LongTask.simulate(100);
                return 42;
            });
            System.out.println("Callable result: " + f2.get()); // 42

            // submit(Runnable, T result) — return the provided value on success
            Future<String> f3 = executor.submit(() -> {}, "done");
            System.out.println("submit(Runnable, result): " + f3.get()); // done
        } finally {
            executor.shutdown();
        }
    }

    // =============================================================
    //  6. Callable<V> + Future<V>   — result-bearing async tasks
    //     Uses existing LongTask.simulate() to mimic real work
    // =============================================================
    private static void callableFutureDemo() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            // Submit a Callable backed by LongTask
            Future<Integer> future = executor.submit(() -> {
                LongTask.simulate(500); // simulate 0.5 s work
                return 99;
            });

            // Main thread can do other work while task runs in background
            System.out.println("Main: doing other work while Callable runs...");

            // future.get() blocks until result is ready
            Integer result = future.get(); // may throw ExecutionException | InterruptedException
            System.out.println("Callable result: " + result); // 99

            // get() with timeout — don't block forever
            Future<String> shortTask = executor.submit(() -> {
                LongTask.simulate(100);
                return "quick";
            });
            try {
                String val = shortTask.get(2, TimeUnit.SECONDS);
                System.out.println("Timed get: " + val);
            } catch (TimeoutException e) {
                System.out.println("Task timed out!");
                shortTask.cancel(true);
            }
        } finally {
            executor.shutdown();
        }
    }

    // =============================================================
    //  7. Future API
    // =============================================================
    private static void futureApiDemo() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Integer> future = executor.submit(() -> {
                LongTask.simulate(300);
                return 7;
            });

            // isDone() — non-blocking poll
            System.out.println("isDone (immediately): " + future.isDone()); // false
            Thread.sleep(400);
            System.out.println("isDone (after 400ms): " + future.isDone()); // true
            System.out.println("Result: " + future.get());

            // cancel() — true to interrupt if running
            Future<Integer> longFuture = executor.submit(() -> {
                LongTask.simulate(5_000);
                return 0;
            });
            boolean cancelled = longFuture.cancel(true); // interrupt the thread
            System.out.println("Cancelled: " + cancelled);
            System.out.println("isCancelled: " + longFuture.isCancelled());
        } finally {
            executor.shutdown();
        }
    }

    // =============================================================
    //  8. invokeAll() — submit a batch; block until ALL finish
    //     Returns List<Future<T>> in the same order as input.
    // =============================================================
    private static void invokeAllDemo() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            List<Callable<Quote>> tasks = List.of(
                () -> { LongTask.simulate(300); return new Quote("site1", 110); },
                () -> { LongTask.simulate(200); return new Quote("site2", 105); },
                () -> { LongTask.simulate(400); return new Quote("site3", 115); }
            );

            // invokeAll blocks until every task completes (or times out)
            List<Future<Quote>> futures = executor.invokeAll(tasks);
            for (Future<Quote> f : futures) {
                System.out.println("invokeAll: " + f.get()); // already done — get() won't block
            }

            // invokeAll with timeout — any task that doesn't finish is cancelled
            List<Future<Quote>> timedFutures = executor.invokeAll(tasks, 350, TimeUnit.MILLISECONDS);
            for (Future<Quote> f : timedFutures) {
                if (f.isCancelled()) System.out.println("Task was cancelled (timeout)");
                else                 System.out.println("Timed invokeAll: " + f.get());
            }
        } finally {
            executor.shutdown();
        }
    }

    // =============================================================
    //  9. invokeAny() — submit a batch; return the FIRST result
    //     Remaining tasks are cancelled. Good for "fastest wins" pattern.
    // =============================================================
    private static void invokeAnyDemo() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            List<Callable<Quote>> tasks = List.of(
                () -> { LongTask.simulate(500); return new Quote("slow", 100); },
                () -> { LongTask.simulate(100); return new Quote("fast", 99);  }, // wins
                () -> { LongTask.simulate(300); return new Quote("mid",  101); }
            );

            // Blocks until one task succeeds; cancels the rest
            Quote fastest = executor.invokeAny(tasks);
            System.out.println("Fastest quote: " + fastest); // likely "fast"
        } finally {
            executor.shutdown();
        }
    }

    // =============================================================
    //  10. Graceful Shutdown
    // =============================================================
    private static void shutdownDemo() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> { LongTask.simulate(200); System.out.println("Task A done."); });
        executor.submit(() -> { LongTask.simulate(200); System.out.println("Task B done."); });

        // shutdown() — no new tasks accepted; queued/running tasks finish normally
        executor.shutdown();
        System.out.println("shutdown() called — waiting...");

        // awaitTermination — block until all tasks are done OR timeout
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            // Force cancel running tasks if they didn't finish in time
            List<Runnable> notStarted = executor.shutdownNow();
            System.out.println("shutdownNow() — tasks not started: " + notStarted.size());
        }
        System.out.println("Executor terminated: " + executor.isTerminated());
    }

    // =============================================================
    //  11. Custom ThreadPoolExecutor (fine-grained control)
    //      Useful when Executors factory methods are not flexible enough.
    // =============================================================
    private static void customThreadPoolDemo() throws InterruptedException {
        // ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive, unit, workQueue)
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2,                             // corePoolSize   — always alive threads
            4,                             // maximumPoolSize — max threads under load
            60L, TimeUnit.SECONDS,         // keepAliveTime  — idle non-core thread lifetime
            new ArrayBlockingQueue<>(10),  // bounded work queue
            new ThreadPoolExecutor.CallerRunsPolicy() // rejection policy when queue full
        );

        try {
            for (int i = 1; i <= 6; i++) {
                final int id = i;
                pool.execute(() -> {
                    System.out.printf("Custom pool: task %d | active=%d queue=%d%n",
                        id, pool.getActiveCount(), pool.getQueue().size());
                    LongTask.simulate(200);
                });
            }
        } finally {
            pool.shutdown();
            pool.awaitTermination(10, TimeUnit.SECONDS);
        }

        // Rejection policies:
        // AbortPolicy        — throws RejectedExecutionException (default)
        // CallerRunsPolicy   — caller thread runs the task (back-pressure)
        // DiscardPolicy      — silently drops the task
        // DiscardOldestPolicy— drops oldest queued task, retries
    }
}
