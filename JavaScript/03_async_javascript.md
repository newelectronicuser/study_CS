# Asynchronous JavaScript

JavaScript is single-threaded, but it handles asynchronous tasks via the **Event Loop**.

## 1. The Event Loop Architecture
1.  **Call Stack**: Where your synchronous code is executed.
2.  **Web APIs**: Provided by the browser (e.g., `setTimeout`, Fetch, DOM events). Tasks are offloaded here.
3.  **Task Queue (Macrotask Queue)**: Where callbacks from `setTimeout`, `setInterval`, and I/O are put.
4.  **Microtask Queue**: Where callbacks from **Promises** (`.then`, `.catch`, `.finally`) and `MutationObserver` are put.

### Execution Priority
- The Call Stack must be empty.
- **The Event Loop always drains the entire Microtask Queue before moving to the next task in the Macrotask Queue.**

## 2. Promises
A Promise is an object representing the eventual completion (or failure) of an asynchronous operation.

### States
- **Pending**: Initial state.
- **Fulfilled**: Operation completed successfully.
- **Rejected**: Operation failed.

### Static Methods
- `Promise.all([p1, p2])`: Resolves when all resolve, or rejects if any rejects.
- `Promise.race([p1, p2])`: Resolves/rejects as soon as the *first* one does.
- `Promise.allSettled([p1, p2])`: Resolves when all have settled (either success or failure).
- `Promise.any([p1, p2])`: Resolves as soon as any one resolves (ignores rejections unless all reject).

## 3. Async / Await
Syntactic sugar on top of Promises that makes asynchronous code look synchronous.

```javascript
async function fetchData() {
  try {
    const response = await fetch(url);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Fetch failed", error);
  }
}
```

## 4. Callbacks vs Promises
- **Callback Hell**: Deeply nested callbacks making code unreadable and hard to manage.
- **Promises**: Solve nesting via chaining.
- **Async/Await**: Solve readability and error handling (try/catch).

> [!IMPORTANT]
> **Microtask vs Macrotask**:
> If a `setTimeout(fn, 0)` and a `Promise.resolve().then(fn)` are both ready, the **Promise callback (Microtask) will ALWAYS run first**, even if the `setTimeout` was called before it.
