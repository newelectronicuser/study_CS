# Lifecycle and Side Effects

Understanding how and when React components update is crucial for performance and bug prevention.

## 1. Class Component Lifecycle Phases
### Mounting (Created and inserted into DOM)
1.  **`constructor()`**: Initialize state.
2.  **`render()`**: Returns JSX.
3.  **`componentDidMount()`**: Perfect for API calls and setting up subscriptions.

### Updating (State or props change)
1.  **`render()`**
2.  **`componentDidUpdate(prevProps, prevState)`**: Used for side effects that depend on changes.

### Unmounting (Removed from DOM)
1.  **`componentWillUnmount()`**: Cleanup (Timers, listeners).

## 2. Functional Components with `useEffect`
Functional components don't have these lifecycle methods, but `useEffect` can mimic all of them.

- **`componentDidMount` equivalent**:
  ```javascript
  useEffect(() => {
    // runs once on mount
  }, []);
  ```
- **`componentDidUpdate` equivalent**:
  ```javascript
  useEffect(() => {
    // runs on mount AND whenever dependencies change
  }, [dependency]);
  ```
- **`componentWillUnmount` equivalent**:
  ```javascript
  useEffect(() => {
    return () => {
      // Cleanup logic here
    };
  }, []);
  ```

## 3. Handling Side Effects
Side effects are any operations that affect something outside the scope of the function being executed (e.g., fetching data, manual DOM changes, subscriptions).

### Best Practices:
- Always clean up subscriptions and timers.
- Use dependency arrays correctly to avoid infinite loops.
- Move logic out of `render` and into `useEffect`.

## 4. `useLayoutEffect` vs `useEffect`
- **`useEffect`**: Runs **asynchronously** after the browser has painted the screen. This is standard and preferred for most side effects.
- **`useLayoutEffect`**: Runs **synchronously** after all DOM mutations but *before* the browser paints. Use only when you need to measure DOM elements and update the UI before the user sees it (to prevent flickering).

> [!WARNING]
> Only use `useLayoutEffect` for visual updates (like calculating position/size). For everything else, use `useEffect` to avoid blocking the visual thread.
