# Modern React and Ecosystem

React 18 and beyond brought significant architectural shifts, focusing on performance, data fetching, and server-side integration.

## 1. Concurrent Rendering
React can now pause and resume rendering to keep the main thread responsive.
- **`useTransition`**: Allows you to mark updates as "non-urgent" (e.g., filtering a long list) so the UI stays interactive during the calculation.
- **`useDeferredValue`**: Defers updating a part of the UI that is expensive to render.

## 2. Suspense for Data Fetching
Wait for something (like data) to load before rendering a component, while showing a fallback UI.
- **Benefits**: Eliminates "loading spinners everywhere" (waterfall effect) and simplifies component logic.

## 3. React Server Components (RSC)
A new type of component that executes **only on the server**.
- **Pros**:
    - zero bundle size impact (code stays on server).
    - Direct access to server-side resources (databases, file systems).
    - Reduced client-side JavaScript.
- **Cons**: Cannot use hooks (`useState`, `useEffect`) or event listeners.

## 4. Strict Mode
A tool for highlighting potential problems in an application. It activates additional checks and warnings for its descendants.
- **In Dev**: It double-invokes certain functions (like constructor, render, and hooks) to help find side effects that should be pure.

## 5. The Fiber Architecture
The internal engine of React. It's a re-implementation of the core algorithm that allows React to "slice" the rendering work into small chunks and assign priorities to them.

## 6. Popular Ecosystem Tools
- **Frameworks**: Next.js (SSG, SSR, RSC), Remix.
- **Styling**: Tailwind CSS, Styled Components, CSS Modules.
- **Testing**: Jest, React Testing Library, Vitest.
- **State Management**: Redux Toolkit, Zustand, React Query.

> [!IMPORTANT]
> **RSC vs. SSR**: 
> - **SSR (Server-Side Rendering)**: Generates HTML on the server and sends it to the client, where it must be hydrated (JavaScript attached).
> - **RSC (Server Components)**: Sends a serialized description of the component to the client, which can be rendered without hydration.
