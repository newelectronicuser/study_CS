# Lifecycle Hooks

Lifecycle hooks are methods that Angular calls on components and directives as it creates, updates, and destroys them.

## 1. Execution Order
The order in which these hooks are called is critical:

1.  **`ngOnChanges()`**: Called when an input property (`@Input`) changes. Receives a `SimpleChanges` object.
2.  **`ngOnInit()`**: Called once after the first `ngOnChanges`. Good for initialization logic.
3.  **`ngDoCheck()`**: Called during every change detection cycle. Use it for custom change detection.
4.  **`ngAfterContentInit()`**: Called after content (`ng-content`) is projected into the component.
5.  **`ngAfterContentChecked()`**: Called after every check of projected content.
6.  **`ngAfterViewInit()`**: Called after the component's views and child views are initialized. Excellent for interacting with the DOM via `ViewChild`.
7.  **`ngAfterViewChecked()`**: Called after every check of the component's views.
8.  **`ngOnDestroy()`**: Called just before Angular destroys the component. Use it to unsubscribe from Observables and detach event handlers to prevent memory leaks.

## 2. Common Interview Questions
### Constructor vs. ngOnInit
- **Constructor**: A standard JavaScript feature. Used for dependency injection and initializing class properties. The component isn't fully initialized (Inputs are not yet available).
- **ngOnInit**: An Angular hook. Called when the component is ready and all `@Input()` properties are initialized. Use this for your actual logic (like API calls).

### ngOnChanges vs. ngOnInit
- `ngOnChanges` runs *before* `ngOnInit` and every time an input changes.
- `ngOnInit` runs only *once*.

## 3. Interfaces
It is best practice to implement the corresponding interface for each hook (e.g., `implements OnInit`).
```typescript
class MyComponent implements OnInit, OnDestroy {
  ngOnInit() { ... }
  ngOnDestroy() { ... }
}
```

> [!IMPORTANT]
> **ViewChild Availability**: 
> Elements accessed via `@ViewChild` are **not** available in `ngOnInit`. They only become available starting from `ngAfterViewInit`.
