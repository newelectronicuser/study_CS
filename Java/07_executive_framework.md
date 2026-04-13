# The Executor Framework & CompletableFuture — Interview Notes 🚀

## 1. Introduction
The **Executor Framework** (Java 5) provides a high-level API for managing thread execution. It separates **task submission** from **task execution** details, eliminating the need to manually create and manage threads.

**Why not raw threads?**
- Creating a `new Thread()` per task is expensive (OS-level resources).
- Uncontrolled thread creation can exhaust memory (`OutOfMemoryError`).
- Thread pools **reuse** idle threads and bound concurrency.

---

## 2. Thread Pools — Types

| Factory Method | Threads | Queue | Use Case |
| :--- | :--- | :--- | :--- |
| `newFixedThreadPool(n)` | Fixed n | Unbounded | CPU-bound, predictable load |
| `newCachedThreadPool()` | Elastic (0–∞) | None | Many short I/O tasks |
| `newSingleThreadExecutor()` | 1 | Unbounded | Sequential ordered tasks |
| `newScheduledThreadPool(n)` | Fixed n | Delay queue | Delay / periodic tasks |

```java
// Fixed — good default for CPU-bound work (≈ number of cores)
int cores = Runtime.getRuntime().availableProcessors();
ExecutorService executor = Executors.newFixedThreadPool(cores);

// Cached — elastic; use for many short-lived I/O tasks
ExecutorService cached = Executors.newCachedThreadPool();

// Single — guaranteed sequential execution
ExecutorService single = Executors.newSingleThreadExecutor();

// Scheduled
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
```

> [!WARNING]
> `newCachedThreadPool()` can spawn thousands of threads under heavy load. Always prefer `newFixedThreadPool` for predictable production workloads.

---

## 3. execute() vs submit()

| Method | Accepts | Returns | Exception |
| :--- | :--- | :--- | :--- |
| `execute(Runnable)` | `Runnable` | `void` | Swallowed (goes to `UncaughtExceptionHandler`) |
| `submit(Runnable)` | `Runnable` | `Future<?>` | Wrapped in `ExecutionException` |
| `submit(Callable<V>)` | `Callable<V>` | `Future<V>` | Wrapped in `ExecutionException` |
| `submit(Runnable, T)` | `Runnable` | `Future<T>` | Returns provided value on success |

```java
// execute — fire and forget
executor.execute(() -> System.out.println("no return value"));

// submit(Callable) — carry a result
Future<Integer> future = executor.submit(() -> {
    Thread.sleep(1000);
    return 42;
});
System.out.println("Do other work...");
Integer result = future.get(); // blocks until done
```

---

## 4. Callable\<V\> + Future\<V\>

`Callable<V>` is like `Runnable` but **returns a value** and can throw checked exceptions.

```java
Callable<Integer> task = () -> {
    Thread.sleep(1000);
    return 42;
};
Future<Integer> future = executor.submit(task);
```

### Future API

| Method | Description |
| :--- | :--- |
| `get()` | Blocks until result is ready; throws `ExecutionException` / `InterruptedException` |
| `get(timeout, unit)` | Blocks with timeout; throws `TimeoutException` |
| `isDone()` | Non-blocking check — true if complete (success, cancel, or exception) |
| `cancel(mayInterrupt)` | Attempts to cancel; `true` to interrupt if running |
| `isCancelled()` | True if successfully cancelled |

```java
System.out.println("isDone: " + future.isDone()); // false (still running)

// get() with timeout — don't block forever
try {
    String result = future.get(2, TimeUnit.SECONDS);
} catch (TimeoutException e) {
    future.cancel(true); // interrupt the running thread
}

// cancel + isCancelled
boolean cancelled = future.cancel(true);
System.out.println("isCancelled: " + future.isCancelled());
```

---

## 5. invokeAll() and invokeAny()

```java
List<Callable<Quote>> tasks = List.of(
    () -> getQuote("site1"),
    () -> getQuote("site2"),
    () -> getQuote("site3")
);

// invokeAll — blocks until ALL finish; returns in submission order
List<Future<Quote>> results = executor.invokeAll(tasks);
for (Future<Quote> f : results) System.out.println(f.get());

// invokeAll with timeout — cancelled tasks have f.isCancelled() == true
executor.invokeAll(tasks, 500, TimeUnit.MILLISECONDS);

// invokeAny — blocks until FIRST succeeds; cancels the rest
Quote fastest = executor.invokeAny(tasks); // returns value directly (no Future)
```

---

## 6. Graceful Shutdown

```java
// 1. Stop accepting new tasks; let queued/running tasks finish
executor.shutdown();

// 2. Wait for completion
if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
    // 3. Force-cancel running tasks if timeout exceeded
    List<Runnable> notStarted = executor.shutdownNow();
    System.out.println("Tasks not started: " + notStarted.size());
}
System.out.println("Terminated: " + executor.isTerminated());
```

> [!IMPORTANT]
> Always shut down an `ExecutorService`. An idle pool keeps JVM alive, causing resource leaks.

---

## 7. Custom ThreadPoolExecutor

```java
// Fine-grained control beyond the Executors factory methods
ThreadPoolExecutor pool = new ThreadPoolExecutor(
    2,                              // corePoolSize   — always-alive threads
    4,                              // maximumPoolSize — max under load
    60L, TimeUnit.SECONDS,          // keepAliveTime   — idle non-core thread lifetime
    new ArrayBlockingQueue<>(10),   // bounded work queue — prevents OOM
    new ThreadPoolExecutor.CallerRunsPolicy() // rejection: caller runs the task
);
```

### Rejection Policies

| Policy | Behaviour |
| :--- | :--- |
| `AbortPolicy` | Throws `RejectedExecutionException` (default) |
| `CallerRunsPolicy` | Caller thread runs the task (back-pressure) |
| `DiscardPolicy` | Silently drops the task |
| `DiscardOldestPolicy` | Drops oldest queued task, retries |

---

## 8. Scheduled Tasks

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

// One-shot with delay
scheduler.schedule(() -> System.out.println("fired"), 500, TimeUnit.MILLISECONDS);

// Fixed-rate: period starts from START of previous run
scheduler.scheduleAtFixedRate(task, 0L, 300L, TimeUnit.MILLISECONDS);

// Fixed-delay: period starts AFTER previous run completes
scheduler.scheduleWithFixedDelay(task, 0L, 300L, TimeUnit.MILLISECONDS);
```

---

## 9. CompletableFuture (Java 8+)
`CompletableFuture<T>` implements both `Future<T>` and `CompletionStage<T>`. It enables building **non-blocking async pipelines** without callback hell.

---

## 10. Creating a CompletableFuture

```java
// runAsync — no return value (uses ForkJoinPool.commonPool())
CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> doWork());

// supplyAsync — returns a value
CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> "result");

// With custom executor (avoid commonPool saturation in production)
CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> "result", myPool);

// Manually — complete it yourself
CompletableFuture<String> manual = new CompletableFuture<>();
new Thread(() -> manual.complete("done")).start();
manual.completeExceptionally(new RuntimeException("error")); // fail it
```

---

## 11. Callbacks on Completion

| Method | Argument | Returns | Description |
| :--- | :--- | :--- | :--- |
| `thenRun(Runnable)` | none | `CF<Void>` | Run action, no access to result |
| `thenAccept(Consumer)` | result | `CF<Void>` | Consume result, no transform |
| `thenApply(Function)` | result | `CF<R>` | Transform result |

```java
CompletableFuture
    .supplyAsync(() -> "  java  ")
    .thenApply(String::trim)            // transform
    .thenApply(String::toUpperCase)     // chain transforms
    .thenAccept(System.out::println)    // consume — "JAVA"
    .thenRun(() -> System.out.println("Done.")); // side effect

// *Async variants submit callback to pool, freeing the producer thread
.thenApplyAsync(s -> s.toUpperCase(), myPool)
```

---

## 12. thenCompose — Dependent Chaining (flatMap)
Use when the **transformation itself returns a `CompletableFuture`**. Prevents `CF<CF<T>>` nesting.

```java
// Without thenCompose → CompletableFuture<CompletableFuture<Order>>  ← wrong!
// With thenCompose    → CompletableFuture<Order>                      ← correct

getUserAsync(userId)                    // CF<User>
    .thenCompose(user ->
        getOrderAsync(user.getId()))    // User → CF<Order>  (flatMap)
    .thenAccept(order ->
        System.out.println(order));
```

---

## 13. thenCombine — Independent Combination
Use when two futures run **independently** and you need **both results**.

```java
CompletableFuture<Integer> priceTask = CompletableFuture.supplyAsync(() -> 100);
CompletableFuture<Integer> taxTask   = CompletableFuture.supplyAsync(() -> 18);

// Both run in PARALLEL; BiFunction called when BOTH complete
CompletableFuture<Integer> total = priceTask.thenCombine(
    taxTask,
    (price, tax) -> price + tax   // 118
);

// thenAcceptBoth — same but no return value
priceTask.thenAcceptBoth(taxTask, (p, t) -> System.out.println(p + t));
```

---

## 14. Exception Handling

| Method | Runs when | Transforms? | Use for |
| :--- | :--- | :--- | :--- |
| `exceptionally(fn)` | Exception only | ✅ Yes | Fallback value |
| `handle(fn)` | Always | ✅ Yes | Recover + transform |
| `whenComplete(fn)` | Always | ❌ No | Logging, side effects |

```java
// exceptionally — fallback on error
cf.exceptionally(ex -> {
    System.out.println("Error: " + ex.getMessage());
    return "fallback";
});

// handle — always runs; access both result and error
cf.handle((result, error) -> {
    if (error != null) return "error fallback";
    return result.toUpperCase();
});

// whenComplete — side-effect only; does NOT change the value
cf.whenComplete((result, error) -> {
    log(result, error); // logging
});
```

---

## 15. allOf and anyOf

```java
CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2, f3);

// Collect results after allOf (returns CF<Void>, so join each manually)
CompletableFuture<List<Quote>> combined = all.thenApply(__ ->
    List.of(f1.join(), f2.join(), f3.join())
);

// anyOf — first to complete wins; cast required (returns CF<Object>)
CompletableFuture<Object> first = CompletableFuture.anyOf(f1, f2, f3);
Quote winner = (Quote) first.get();
```

---

## 16. Timeout (Java 9+)

```java
// completeOnTimeout — graceful default value
cf.completeOnTimeout("offline default", 500, TimeUnit.MILLISECONDS);

// orTimeout — strict; throws TimeoutException wrapped in ExecutionException
cf.orTimeout(500, TimeUnit.MILLISECONDS);
```

---

## 17. join() vs get()

| | `get()` | `join()` |
| :--- | :--- | :--- |
| Checked exceptions | ✅ Yes (`ExecutionException`, `InterruptedException`) | ❌ No |
| Unchecked exception | `ExecutionException` | `CompletionException` |
| Use in lambda / stream | ❌ Verbose (try-catch) | ✅ Clean |

```java
// get() — good in main/test code with try-catch
Integer result = future.get();

// join() — preferred inside stream pipelines
List<Quote> quotes = futures.stream()
    .map(CompletableFuture::join) // no checked exception
    .collect(Collectors.toList());
```

---

## 18. Real-world Patterns

### Parallel Data Aggregation (allOf + join)
```java
// Kick off all requests in parallel
List<CompletableFuture<Quote>> futureList = sites.stream()
    .map(site -> CompletableFuture.supplyAsync(() -> getQuote(site)))
    .collect(Collectors.toList());

// Wait for all, then collect
CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]))
    .thenApply(__ -> futureList.stream().map(CompletableFuture::join).collect(Collectors.toList()))
    .thenAccept(quotes -> quotes.forEach(System.out::println))
    .join();
```

### Fire-and-Forget (sendAsync)
```java
mailService.sendAsync()                                   // CF<Void>
    .thenRun(() -> System.out.println("Mail sent."))
    .exceptionally(e -> { log(e); return null; });
// Main thread is NOT blocked
```

### Async API (manual completion)
```java
public CompletableFuture<String> fetchDataAsync() {
    CompletableFuture<String> future = new CompletableFuture<>();
    new Thread(() -> {
        // ... do async work ...
        future.complete("result");          // or completeExceptionally(e)
    }).start();
    return future; // caller gets a handle to subscribe callbacks
}
```

---

## 19. Summary Table

| Method | Argument | Returns | Purpose |
| :--- | :--- | :--- | :--- |
| `runAsync` | `Runnable` | `CF<Void>` | Fire async task, no result |
| `supplyAsync` | `Supplier<T>` | `CF<T>` | Fire async task with result |
| `thenRun` | `Runnable` | `CF<Void>` | Action after completion |
| `thenAccept` | `Consumer<T>` | `CF<Void>` | Consume result |
| `thenApply` | `Function<T,R>` | `CF<R>` | Transform result |
| `thenCompose` | `Function<T,CF<R>>` | `CF<R>` | Dependent chain (flatMap) |
| `thenCombine` | `CF<U>`, `BiFunction` | `CF<V>` | Combine two independent CFs |
| `thenAcceptBoth` | `CF<U>`, `BiConsumer` | `CF<Void>` | Consume two results |
| `exceptionally` | `Function<Throwable,T>` | `CF<T>` | Fallback on error only |
| `handle` | `BiFunction<T,Throwable,R>` | `CF<R>` | Always; transform or recover |
| `whenComplete` | `BiConsumer<T,Throwable>` | `CF<T>` | Always; side effect only |
| `allOf` | `CF<?>...` | `CF<Void>` | Wait for ALL to complete |
| `anyOf` | `CF<?>...` | `CF<Object>` | Wait for FIRST to complete |
| `orTimeout` | `long, TimeUnit` | `CF<T>` | Fail with `TimeoutException` |
| `completeOnTimeout` | `T, long, TimeUnit` | `CF<T>` | Default value on timeout |

> [!IMPORTANT]
> **Key Interview Rules**:
> 1. Always `shutdown()` + `awaitTermination()` your `ExecutorService`.
> 2. `thenCompose` = flatMap (prevents `CF<CF<T>>`); `thenApply` = map.
> 3. `thenCombine` runs two futures **in parallel** and combines results.
> 4. `get()` throws checked exceptions; `join()` throws unchecked — prefer `join()` in streams.
> 5. `exceptionally` handles errors; `handle` handles both success AND failure.
> 6. `allOf` returns `CF<Void>` — collect individual results via `f.join()` inside `thenApply`.
> 7. Use `completeOnTimeout` for graceful degradation; `orTimeout` for strict SLAs.
> 8. Avoid `ForkJoinPool.commonPool()` for blocking I/O — provide a custom `ExecutorService`.
