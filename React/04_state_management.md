# State Management

State management is the process of managing data as it flows through the application.

## 1. Local State (`useState`)
Managed within a single component. Best for data that is only needed by that component and its immediate children (via props).

## 2. Global State
Needed when multiple, deeply nested components need access to the same data.

### Context API (Built-in)
Provides a way to pass data through the component tree without having to pass props down manually at every level (**Prop Drilling**).
- **Cons**: Every component that subscribes to the context will re-render whenever the context value changes, even if it only uses a small part of that value.

### Redux (External)
A centralized state container. Uses **Slices**, **Reducers**, and **Actions**.
- **Pros**: Predictable state, excellent debugging tools (Redux DevTools), handles complex logic well.
- **Cons**: High boilerplate, steep learning curve.

### Zustand (External - Modern)
A small, fast, and scalable bearbones state-management solution. No boilerplate, uses hooks natively.
```javascript
const useStore = create((set) => ({
  count: 0,
  inc: () => set((state) => ({ count: state.count + 1 })),
}));
```

## 3. Server State vs. Client State
Distinguishing between data that lives on the server (API results) and data that lives on the client (UI state).

### React Query (TanStack Query)
A library specifically for managing server state.
- **Features**: Caching, background fetching, stale-while-revalidate, automatic retries.
- **Why?**: Redux/Zustand were designed for Client state. Using them for Server state leads to messy logic for loading/error handling.

## 4. Lifting State Up
The process of moving state from a child component to its closest common ancestor so multiple children can share the data.

> [!IMPORTANT]
> **Don't Over-engineer State**: 
> Start with local state. If you hit prop-drilling or need shared data, use Context. Only reach for Redux/Zustand if the application complexity warrants a truly centralized store.
