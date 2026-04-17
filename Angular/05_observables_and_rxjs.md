# Observables and RxJS

Angular uses **RxJS (Reactive Extensions for JavaScript)** for asynchronous and event-based programming.

## 1. Observable vs. Promise
- **Promise**: Handles a single event. It starts executing immediately upon creation. It cannot be canceled.
- **Observable**: Handles a stream of events (0 to many). It is "lazy" (doesn't start until you subscribe). It can be canceled (unsubscribed).

## 2. Key Concepts
- **Subscriber**: A consumer that listens for data from an Observable.
- **Operators**: Functions that transform the stream of data (e.g., `map`, `filter`).
- **Subscription**: Represents the execution of an Observable. You must call `.unsubscribe()` on it to stop receiving data and free resources.

## 3. Common RxJS Operators
- **`map`**: Transforms items emitted by an Observable by applying a function.
- **`filter`**: Only emits items that satisfy a condition.
- **`switchMap`**: Switches to a new observable and cancels the previous one (Best for search-as-you-type).
- **`mergeMap`**: Handles multiple observables in parallel.
- **`tap`**: Used for side effects (like logging) without changing the data.
- **`catchError`**: Catches errors in the stream.

## 4. Subjects
A Subject is both an **Observable** and an **Observer**.
- **`Subject`**: No initial value. Subscribers only receive values emitted *after* they subscribe.
- **`BehaviorSubject`**: Requires an initial value. Subscribers receive the *last* emitted value immediately upon subscription.
- **`ReplaySubject`**: Can "buffer" a specified number of values and replay them to new subscribers.

## 5. Async Pipe
Automatically subscribes to an Observable in the template and unsubscribes when the component is destroyed.
```html
<p>{{ data$ | async }}</p>
```
**Benefits**:
- Cleaner code.
- Automatic memory management (unsubscribing).
- Works perfectly with `ChangeDetectionStrategy.OnPush`.

> [!IMPORTANT]
> **Subscribing in Components**: 
> If you manually subscribe using `.subscribe()`, you **must** call `.unsubscribe()` in `ngOnDestroy` to prevent memory leaks. Alternatively, use a "take until" pattern or the `AsyncPipe`.
