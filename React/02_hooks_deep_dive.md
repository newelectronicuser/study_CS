# Hooks Deep Dive

Hooks were introduced in React 16.8. They let you use state and other React features without writing a class.

## 1. Basic Hooks

### `useState`
Allows functional components to have state.
```javascript
const [count, setCount] = useState(0);
```

### `useEffect`
Used for handling side effects (Data fetching, DOM manipulation, timers).
- **No dependencies**: Runs after every render.
- **Empty array `[]`**: Runs once after initial mount.
- **With dependencies `[prop, state]`**: Runs only when those values change.
- **Cleanup function**: Return a function from `useEffect` to clean up resources (e.g., `clearInterval`).

## 2. Additional Performance/Reference Hooks

### `useMemo`
Returns a memoized **value**. Used to avoid expensive calculations on every render.
```javascript
const result = useMemo(() => expensiveCalculation(num), [num]);
```

### `useCallback`
Returns a memoized **function**. Used to prevent unnecessary re-renders of child components that depend on function equality.

### `useRef`
- **Accessing DOM**: Directly interacting with a DOM element.
- **Persistence**: Storing variable values that persist between renders but **do not trigger a re-render** when changed.

## 3. Advanced State Hooks

### `useReducer`
An alternative to `useState` for complex state logic (similar to Redux).

### `useContext`
Subscribes to React Context without nesting multiple components.

## 4. Rules of Hooks
1.  **Only Call Hooks at the Top Level**: Don't call hooks inside loops, conditions, or nested functions.
2.  **Only Call Hooks from React Functions**: Only call them from functional components or custom hooks.

## 5. Custom Hooks
User-defined functions starting with `use` that encapsulate reusable stateful logic.
```javascript
function useWindowWidth() {
  const [width, setWidth] = useState(window.innerWidth);
  useEffect(() => {
    const handleResize = () => setWidth(window.innerWidth);
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);
  return width;
}
```

> [!TIP]
> **Why `useMemo` vs `useCallback`?**
> - `useMemo` caches a result of a function.
> - `useCallback` caches the function itself.
> Use them when you are passing results/functions down to `React.memo`ed children.
