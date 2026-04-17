# Memory and Performance

Understanding how JavaScript manages memory is crucial for building large-scale, high-performance applications.

## 1. Memory Lifecycle
1.  **Allocate**: Request the memory you need.
2.  **Use**: Read/Write to the allocated memory.
3.  **Release**: Clear the memory when it's no longer needed.

## 2. Stack vs. Heap
- **Stack**: Used for static data (fixed size) like primitives (`number`, `string`, `boolean`) and function calls (execution context). Super fast allocation.
- **Heap**: Used for dynamic data (objects, arrays, functions) where the size might change. Allocation is slower but can store large amounts of data.

## 3. Garbage Collection (GC)
JavaScript is a garbage-collected language. The Engine automatically identifies and removes objects that are no longer reachable.

### Mark-and-Sweep Algorithm (Main algorithm)
1.  **Mark**: The GC starts from "roots" (global object, local variables in stack) and "marks" everything it can reach.
2.  **Sweep**: Any object that was *not* marked is considered unreachable and its memory is cleared.

## 4. Common Memory Leaks
Memory leaks happen when the GC cannot collect objects that are no longer needed because they are still reachable.

- **Accidental Global Variables**: Defining variables without `let`/`const` inside functions.
- **Forgotten Timers/Callbacks**: A `setInterval` that keeps running even after the data it uses is no longer needed.
- **Closures**: Keeping references to large objects in inner functions that are still reachable.
- **Out of DOM References**: Storing a reference to a DOM node in a JS object after the node has been removed from the document.

## 5. Performance Tips
- **Avoid Global Scope**: It's harder for GC to determine if global variables are still needed.
- **Nullify References**: If you have a large object that is no longer needed, set it to `null`.
- **Use Web Workers**: For CPU-intensive tasks to keep the main thread (and UI) responsive.
- **Minimize DOM Access**: DOM operations are expensive. Batch them or use DocumentFragments.

> [!WARNING]
> While `null`ing a reference helps, it is **not** a manual delete. It simply makes the object unreachable, allowing the Garbage Collector to clean it up in its next cycle.
