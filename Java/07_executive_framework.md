# The Executor Framework & CompletableFuture - Interview Notes 🚀

## 1. Introduction
The **Executor Framework** (introduced in Java 5) provides a high-level API for managing and controlling thread execution. It separates the **task submission** from the **task execution** details.

---

## 2. Thread Pools
Creating a new thread for every task is expensive. A **Thread Pool** manages a pool of worker threads to minimize the overhead of thread creation and destruction.
- **Benefits**: Improved performance, better resource management (prevents "OutOfMemoryError: cannot create new native thread").

---

## 3. Executors
The `Executors` utility class provides factory methods to create different types of thread pools:
- **`newFixedThreadPool(n)`**: Contains a fixed number of threads.
- **`newCachedThreadPool()`**: Creates new threads as needed, but reuses idle threads.
- **`newSingleThreadExecutor()`**: Executes tasks sequentially in a single thread.
- **`newScheduledThreadPool(n)`**: Can schedule tasks to run after a delay or periodically.

---

## 4. Callable and Future
- **`Callable<V>`**: Similar to `Runnable`, but it returns a result and can throw checked exceptions.
- **`Future<V>`**: Represents the result of an asynchronous computation. 
    - `get()`: Blocks until the result is available.
    - `isDone()`: Non-blocking check for completion.
    - `cancel()`: Attempts to cancel the task.

---

## 5. Asynchronous Programming
Asynchronous programming allows you to perform tasks in the background without blocking the main execution thread. This is essential for responsive UI and scalable backends.

---

## 6. Completable Futures (Java 8+)
`CompletableFuture` implements both `Future` and `CompletionStage`. It allows you to build complex **asynchronous pipelines** without "Callback Hell."

---

## 7. Creating a Completable Future
- **`runAsync(Runnable)`**: Runs a task that doesn't return a value.
- **`supplyAsync(Supplier<U>)`**: Runs a task that returns a value.
```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello from Async!");
```

---

## 8. Implementing an Asynchronous API
You can manually complete a future to provide a result to the caller.
```java
public CompletableFuture<String> fetchData() {
    CompletableFuture<String> future = new CompletableFuture<>();
    // Perform async logic...
    future.complete("Data fetched!"); // Manually complete
    return future;
}
```

---

## 9. Running Code on Completion
- **`thenRun(Runnable)`**: Runs a logic when the previous task completes (no access to result).
- **`thenAccept(Consumer)`**: Consumes the result of the previous task (no return value).

---

## 10. Handling Exceptions
Async code can fail. Use these methods to recover:
- **`exceptionally(fn)`**: Executes if an exception occurs. Provide a fallback value.
- **`handle(fn)`**: Executes regardless of success or failure. Access both result and exception.

---

## 11. Transforming a Completable Future
Use **`thenApply(Function)`** to transform the result of a future into another value.
```java
future.thenApply(s -> s.length()); // Returns CompletableFuture<Integer>
```

---

## 12. Composing Completable Futures (Dependent)
Use **`thenCompose()`** if your transformation function itself returns a `CompletableFuture`. (Similar to `flatMap` in Streams).
```java
// Future 1 -> result used to trigger Future 2
getUser(id).thenCompose(user -> getOrder(user)); 
```

---

## 13. Combining Completable Futures (Independent)
Use **`thenCombine()`** to combine two independent futures once both are complete.
```java
future1.thenCombine(future2, (res1, res2) -> res1 + res2);
```

---

## 14. Waiting for Many Tasks to Complete
- **`CompletableFuture.allOf(f1, f2, ...)`**: Completes when **all** the provided futures are complete. Returns `CompletableFuture<Void>`.

---

## 15. Waiting for the First Task
- **`CompletableFuture.anyOf(f1, f2, ...)`**: Completes as soon as **any** of the provided futures complete. Returns `CompletableFuture<Object>`.

---

## 16. Handling Timeouts (Java 9+)
Prevent infinite waiting with built-in timeout methods:
- **`orTimeout(time, unit)`**: Throws an exception if the future doesn't complete in time.
- **`completeOnTimeout(value, time, unit)`**: Returns a default value if the future times out.

---

## 17. Summary Table
| Method | Input | Output | Purpose |
| :--- | :--- | :--- | :--- |
| **thenApply** | `Function` | `Future<U>` | Transform result. |
| **thenAccept**| `Consumer` | `Future<Void>`| Consume result. |
| **thenCompose**| `Function` | `Future<U>` | Chain dependent futures (Flatten). |
| **thenCombine**| `BiFunction`| `Future<U>` | Combine two independent futures. |
| **allOf** | Array | `Future<Void>`| Wait for all to finish. |
| **anyOf** | Array | `Future<Object>`| Wait for first to finish. |
