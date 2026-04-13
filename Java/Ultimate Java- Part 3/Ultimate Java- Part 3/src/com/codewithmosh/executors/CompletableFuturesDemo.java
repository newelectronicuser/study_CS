package com.codewithmosh.executors;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * ================================================================
 *  CompletableFuturesDemo — Interview-Ready CompletableFuture Sheet
 * ================================================================
 *
 *  Topics covered:
 *   1.  runAsync / supplyAsync  — create a CF
 *   2.  Manually completing a CF (async API pattern)
 *   3.  thenRun / thenAccept / thenApply — callbacks on completion
 *   4.  Async callbacks — thenApplyAsync / thenAcceptAsync (custom pool)
 *   5.  thenCompose  — chaining dependent futures (flatMap equivalent)
 *   6.  thenCombine  — combining two independent futures
 *   7.  Exception handling — exceptionally / handle / whenComplete
 *   8.  allOf        — wait for ALL futures
 *   9.  anyOf        — race, take the FIRST result
 *  10.  orTimeout / completeOnTimeout (Java 9+)
 *  11.  join() vs get()
 *  12.  Real-world: parallel flight-price aggregation (FlightService)
 *  13.  Real-world: fire-and-forget (MailService.sendAsync)
 */
public class CompletableFuturesDemo {

    // =============================================================
    //  MAIN entry point
    // =============================================================
    public static void show() throws ExecutionException, InterruptedException {
        System.out.println("\n--- 1. runAsync / supplyAsync ---");
        createFuturesDemo();

        System.out.println("\n--- 2. Manually completing a CF ---");
        manualCompleteDemo();

        System.out.println("\n--- 3. thenRun / thenAccept / thenApply ---");
        callbacksDemo();

        System.out.println("\n--- 4. Async callbacks (custom pool) ---");
        asyncCallbacksDemo();

        System.out.println("\n--- 5. thenCompose (dependent chaining) ---");
        thenComposeDemo();

        System.out.println("\n--- 6. thenCombine (independent combination) ---");
        thenCombineDemo();

        System.out.println("\n--- 7. Exception Handling ---");
        exceptionHandlingDemo();

        System.out.println("\n--- 8. allOf (wait for ALL) ---");
        allOfDemo();

        System.out.println("\n--- 9. anyOf (first wins) ---");
        anyOfDemo();

        System.out.println("\n--- 10. Timeout (orTimeout / completeOnTimeout) ---");
        timeoutDemo();

        System.out.println("\n--- 11. join() vs get() ---");
        joinVsGetDemo();

        System.out.println("\n--- 12. Real-world: Flight Price Aggregation ---");
        flightAggregationDemo();

        System.out.println("\n--- 13. Real-world: Fire-and-Forget (MailService) ---");
        fireAndForgetDemo();
    }

    // =============================================================
    //  1. runAsync / supplyAsync
    //     runAsync(Runnable)     → CompletableFuture<Void>  (no result)
    //     supplyAsync(Supplier)  → CompletableFuture<T>     (has result)
    //     Both use ForkJoinPool.commonPool() by default
    // =============================================================
    private static void createFuturesDemo() throws ExecutionException, InterruptedException {
        // runAsync — fire and forget, no return value
        CompletableFuture<Void> run = CompletableFuture.runAsync(() -> {
            System.out.println("runAsync on " + Thread.currentThread().getName());
        });
        run.get(); // wait for completion

        // supplyAsync — returns a value
        CompletableFuture<Integer> supply = CompletableFuture.supplyAsync(() -> {
            LongTask.simulate(200);
            return 42;
        });
        System.out.println("supplyAsync result: " + supply.get());

        // Custom executor instead of commonPool
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CompletableFuture<String> withPool = CompletableFuture.supplyAsync(
            () -> "custom pool: " + Thread.currentThread().getName(), pool
        );
        System.out.println(withPool.get());
        pool.shutdown();
    }

    // =============================================================
    //  2. Manually completing a CF
    //     Pattern: expose a CompletableFuture to caller;
    //     complete it when the async work finishes.
    // =============================================================
    private static void manualCompleteDemo() throws ExecutionException, InterruptedException {
        // Service method returns a CF and completes it internally
        CompletableFuture<String> future = fetchDataAsync();
        System.out.println("Manual complete result: " + future.get());

        // completeExceptionally — complete with an error
        CompletableFuture<String> failFuture = new CompletableFuture<>();
        failFuture.completeExceptionally(new RuntimeException("service unavailable"));
        try {
            failFuture.get();
        } catch (ExecutionException e) {
            System.out.println("Exception from manual fail: " + e.getCause().getMessage());
        }
    }

    /** Simulates an async service that wraps legacy/blocking code in a CF. */
    private static CompletableFuture<String> fetchDataAsync() {
        CompletableFuture<String> future = new CompletableFuture<>();
        new Thread(() -> {
            LongTask.simulate(200);
            future.complete("Data fetched!"); // manually complete the future
        }).start();
        return future;
    }

    // =============================================================
    //  3. Callbacks on completion
    //     thenRun(Runnable)             — no access to result; returns CF<Void>
    //     thenAccept(Consumer<T>)       — consumes result;   returns CF<Void>
    //     thenApply(Function<T,R>)      — transforms result; returns CF<R>
    // =============================================================
    private static void callbacksDemo() throws ExecutionException, InterruptedException {
        // thenRun — know when it's done, don't need the value
        CompletableFuture<Void> run = CompletableFuture
            .supplyAsync(() -> "result")
            .thenRun(() -> System.out.println("thenRun: pipeline finished"));
        run.get();

        // thenAccept — consume the result (side effect, no transform)
        CompletableFuture<Void> accept = CompletableFuture
            .supplyAsync(() -> "Hello")
            .thenAccept(s -> System.out.println("thenAccept: " + s));
        accept.get();

        // thenApply — transform (like map); result flows downstream
        CompletableFuture<Integer> apply = CompletableFuture
            .supplyAsync(() -> "Hello World")
            .thenApply(String::length)          // String → Integer
            .thenApply(len -> len * 2);         // Integer → Integer
        System.out.println("thenApply chain: " + apply.get()); // 22

        // Chaining all three together
        CompletableFuture
            .supplyAsync(() -> "  java  ")
            .thenApply(String::trim)
            .thenApply(String::toUpperCase)
            .thenAccept(s -> System.out.println("Pipeline result: " + s))
            .thenRun(() -> System.out.println("Pipeline done."))
            .get();
    }

    // =============================================================
    //  4. Async callbacks — thenApplyAsync / thenAcceptAsync
    //     By default, callbacks run on the thread that completed the CF
    //     (often the common ForkJoinPool worker).
    //     *Async variants submit the callback to the pool, freeing the producer thread.
    // =============================================================
    private static void asyncCallbacksDemo() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            CompletableFuture<String> result = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("Producer: " + Thread.currentThread().getName());
                    return "data";
                }, pool)
                .thenApplyAsync(s -> {
                    System.out.println("thenApplyAsync: " + Thread.currentThread().getName());
                    return s.toUpperCase();
                }, pool); // callback runs on pool, not on producer thread

            System.out.println("Async callback result: " + result.get());
        } finally {
            pool.shutdown();
        }
    }

    // =============================================================
    //  5. thenCompose — chaining DEPENDENT futures
    //     When transformation itself returns a CompletableFuture.
    //     Prevents CompletableFuture<CompletableFuture<T>> nesting.
    //     Equivalent to flatMap in Streams.
    // =============================================================
    private static void thenComposeDemo() throws ExecutionException, InterruptedException {
        CompletableFuture<Quote> result = CompletableFuture
            .supplyAsync(() -> "site1")           // CF<String>
            .thenCompose(site ->                   // String → CF<Quote>
                new FlightService().getQuote(site) // already returns CF<Quote>
            );

        System.out.println("thenCompose result: " + result.get());

        // Pattern: step1 result drives step2 request
        // getUserAsync(id)
        //   .thenCompose(user -> getOrderAsync(user.getId()))
        //   .thenAccept(order -> System.out.println(order));
    }

    // =============================================================
    //  6. thenCombine — combining TWO INDEPENDENT futures
    //     Both run in parallel; BiFunction is called when BOTH complete.
    // =============================================================
    private static void thenCombineDemo() throws ExecutionException, InterruptedException {
        // Two independent async tasks
        CompletableFuture<Integer> priceTask = CompletableFuture
            .supplyAsync(() -> { LongTask.simulate(200); return 100; }); // price

        CompletableFuture<Integer> taxTask = CompletableFuture
            .supplyAsync(() -> { LongTask.simulate(150); return 18; }); // tax

        // Run in PARALLEL; combine results when both are done
        CompletableFuture<Integer> total = priceTask.thenCombine(
            taxTask,
            (price, tax) -> price + tax // BiFunction called with both results
        );
        System.out.println("thenCombine total (price + tax): " + total.get()); // 118

        // thenAcceptBoth — same as thenCombine but no return value
        priceTask.thenAcceptBoth(taxTask,
            (p, t) -> System.out.println("thenAcceptBoth: " + p + " + " + t + " = " + (p + t))
        ).get();
    }

    // =============================================================
    //  7. Exception Handling
    //     exceptionally(fn)    — triggered ONLY on exception; provide fallback
    //     handle(fn)           — always runs; access both result AND exception
    //     whenComplete(fn)     — always runs; does NOT transform the result
    // =============================================================
    private static void exceptionHandlingDemo() throws ExecutionException, InterruptedException {
        // exceptionally — fallback value on error
        CompletableFuture<String> ex1 = CompletableFuture
            .<String>supplyAsync(() -> { throw new RuntimeException("API down"); })
            .exceptionally(e -> {
                System.out.println("exceptionally caught: " + e.getMessage());
                return "fallback data";
            });
        System.out.println("exceptionally result: " + ex1.get());

        // handle — runs on BOTH success and failure; can transform
        CompletableFuture<String> ex2 = CompletableFuture
            .<String>supplyAsync(() -> { throw new RuntimeException("Network error"); })
            .handle((result, error) -> {
                if (error != null) {
                    System.out.println("handle caught: " + error.getMessage());
                    return "handled fallback";
                }
                return result.toUpperCase();
            });
        System.out.println("handle result: " + ex2.get());

        // whenComplete — side-effect only (logging), does NOT change the result
        CompletableFuture<String> ex3 = CompletableFuture
            .<String>supplyAsync(() -> { throw new RuntimeException("Transient error"); })
            .whenComplete((result, error) -> {
                if (error != null) System.out.println("whenComplete log: " + error.getMessage());
                else               System.out.println("whenComplete log: success=" + result);
            })
            .exceptionally(e -> "recovered"); // still needs recovery after whenComplete
        System.out.println("whenComplete result: " + ex3.get());
    }

    // =============================================================
    //  8. allOf — wait for ALL futures to complete
    //     Returns CF<Void>; individual results must be retrieved via join()
    // =============================================================
    private static void allOfDemo() throws ExecutionException, InterruptedException {
        CompletableFuture<Quote> f1 = new FlightService().getQuote("site1");
        CompletableFuture<Quote> f2 = new FlightService().getQuote("site2");
        CompletableFuture<Quote> f3 = new FlightService().getQuote("site3");

        // All run in PARALLEL; allOf waits for the slowest one
        CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2, f3);

        // Attach a callback that collects all results
        CompletableFuture<List<Quote>> combined = all.thenApply(__ ->
            List.of(f1.join(), f2.join(), f3.join()) // join() — no checked exception
        );

        combined.thenAccept(quotes -> {
            System.out.println("All quotes received:");
            quotes.forEach(q -> System.out.println("  " + q));
        }).get();
    }

    // =============================================================
    //  9. anyOf — first future to complete wins
    //     Returns CF<Object> — cast required
    // =============================================================
    private static void anyOfDemo() throws ExecutionException, InterruptedException {
        CompletableFuture<Quote> fast   = CompletableFuture.supplyAsync(() -> {
            LongTask.simulate(100); return new Quote("fast",   99); });
        CompletableFuture<Quote> medium = CompletableFuture.supplyAsync(() -> {
            LongTask.simulate(300); return new Quote("medium", 105); });
        CompletableFuture<Quote> slow   = CompletableFuture.supplyAsync(() -> {
            LongTask.simulate(500); return new Quote("slow",   110); });

        CompletableFuture<Object> first = CompletableFuture.anyOf(fast, medium, slow);
        Quote winner = (Quote) first.get(); // safe cast — all are Quote
        System.out.println("First quote arrived: " + winner);
    }

    // =============================================================
    //  10. Timeout (Java 9+)
    //      orTimeout(time, unit)            — completes with TimeoutException
    //      completeOnTimeout(value, t, unit) — completes with default value
    // =============================================================
    private static void timeoutDemo() throws ExecutionException, InterruptedException {
        // completeOnTimeout — graceful: returns a default when slow
        CompletableFuture<String> withDefault = CompletableFuture
            .supplyAsync(() -> { LongTask.simulate(2_000); return "slow result"; })
            .completeOnTimeout("offline default", 500, TimeUnit.MILLISECONDS);
        System.out.println("completeOnTimeout: " + withDefault.get()); // "offline default"

        // orTimeout — strict: throws TimeoutException
        CompletableFuture<String> withTimeout = CompletableFuture
            .supplyAsync(() -> { LongTask.simulate(2_000); return "slow"; })
            .orTimeout(500, TimeUnit.MILLISECONDS);
        try {
            withTimeout.get();
        } catch (ExecutionException e) {
            System.out.println("orTimeout threw: " + e.getCause().getClass().getSimpleName());
            // TimeoutException
        }
    }

    // =============================================================
    //  11. join() vs get()
    //      get()  — throws checked ExecutionException + InterruptedException
    //      join() — throws unchecked CompletionException (easier in streams)
    // =============================================================
    private static void joinVsGetDemo() {
        CompletableFuture<Integer> f = CompletableFuture.supplyAsync(() -> 10);

        // get() — must handle checked exceptions (good in main/try-catch contexts)
        try {
            System.out.println("get():  " + f.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // join() — unchecked; preferred inside stream/lambda pipelines
        int result = CompletableFuture.supplyAsync(() -> 20).join();
        System.out.println("join(): " + result);

        // Practical: collect results from a List<CF<T>> using join()
        List<CompletableFuture<Integer>> futures = List.of(
            CompletableFuture.supplyAsync(() -> 1),
            CompletableFuture.supplyAsync(() -> 2),
            CompletableFuture.supplyAsync(() -> 3)
        );
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(__ -> futures.stream()
                .map(CompletableFuture::join) // no checked exception in stream
                .collect(Collectors.toList()))
            .thenAccept(values -> System.out.println("Collected via join: " + values))
            .join();
    }

    // =============================================================
    //  12. Real-world: Parallel Flight Price Aggregation
    //      Uses FlightService.getQuotes() — returns Stream<CF<Quote>>
    //      All quotes fetched in PARALLEL, then aggregated
    // =============================================================
    private static void flightAggregationDemo() throws ExecutionException, InterruptedException {
        var start = LocalTime.now();
        var service = new FlightService();

        // Kick off all quote requests in parallel
        List<CompletableFuture<Quote>> futureList = service.getQuotes()
            .collect(Collectors.toList());

        // Wait for all; collect results
        CompletableFuture<List<Quote>> allQuotes = CompletableFuture
            .allOf(futureList.toArray(new CompletableFuture[0]))
            .thenApply(__ -> futureList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));

        allQuotes.thenAccept(quotes -> {
            var end = LocalTime.now();
            System.out.println("Retrieved " + quotes.size()
                + " quotes in " + Duration.between(start, end).toMillis() + " ms");
            quotes.stream()
                .min(java.util.Comparator.comparingInt(Quote::getPrice))
                .ifPresent(q -> System.out.println("Cheapest: " + q));
        }).get();
    }

    // =============================================================
    //  13. Real-world: Fire-and-Forget (MailService)
    //      Send email asynchronously; main thread keeps working
    // =============================================================
    private static void fireAndForgetDemo() throws InterruptedException {
        var mailService = new MailService();

        System.out.println("Main: sending mail asynchronously...");

        // sendAsync() wraps the blocking send() in a supplyAsync
        CompletableFuture<Void> mailFuture = mailService.sendAsync();

        // Main thread continues immediately
        System.out.println("Main: doing other work while mail is being sent...");

        // Optional: attach a callback rather than blocking
        mailFuture
            .thenRun(() -> System.out.println("Callback: mail confirmed sent."))
            .exceptionally(e -> {
                System.out.println("Callback: mail failed — " + e.getMessage());
                return null;
            });

        // Give the async task time to finish (in a real app, keep app alive differently)
        Thread.sleep(4_000);
    }
}
