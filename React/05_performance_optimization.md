# Performance Optimization

Performance in React is primarily about reducing the work needed to calculate and apply changes to the DOM.

## 1. Memoization
### `React.memo` (High Order Component)
Prevents a functional component from re-rendering if its props haven't changed.
- **Note**: It only performs a shallow comparison of props.

### `useMemo` and `useCallback`
Caches values and function references between renders. Essential when passing data or functions to `React.memo`ed child components.

## 2. Preventing "Zombie" Re-renders
A component re-renders if:
1. Its state changes.
2. Its props change.
3. Its parent re-renders (even if props are identical, unless `React.memo` is used).

## 3. Code Splitting
Loading only the parts of the application the user needs *right now*.
- **`React.lazy`**: Allows you to render a dynamic import as a regular component.
- **`Suspense`**: A component that wraps lazy components and shows a fallback (e.g., a loading spinner) while they load.
```javascript
const LazyComponent = React.lazy(() => import('./LazyComponent'));

function App() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <LazyComponent />
    </Suspense>
  );
}
```

## 4. Virtualization (Windowing)
If you have a list of thousands of items, rendering all of them will crash the browser. Use libraries like `react-window` or `react-virtualized` to render only the items currently visible in the viewport.

## 5. Identifying Performance Issues
- **React DevTools Profiler**: Record a session to see exactly which components rendered and why.
- **"Why Did You Render?" (WDYR)**: A library that logs to the console when a component re-renders unnecessarily.

## 6. Optimization Best Practices
- **Lighthouse/Web Vitals**: Measure LCP, FID, and CLS.
- **Throttling/Debouncing**: Limit how often expensive functions (like search or scroll handlers) are called.
- **Avoid Anonymous Functions in Props**: They are re-created on every render, breaking prop equality.

> [!CAUTION]
> **Premature Optimization**: 
> Don't wrap every component in `memo` or every function in `useCallback`. Memoization has its own cost (memory and comparison time). Add it only after identifying a performance bottleneck.
