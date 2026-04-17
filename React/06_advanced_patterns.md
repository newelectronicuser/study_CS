# Advanced Design Patterns

Design patterns in React are proven ways to solve recurring problems while maintaining code cleanliness and reusability.

## 1. Higher-Order Components (HOC)
A pattern where a function takes a component and returns a new component with additional props or behavior.
- **Example**: `withAuth(MyComponent)` or `connect(mapStateToProps)(MyComponent)`.
- **Note**: With the rise of Hooks, HOCs are used less frequently but are still found in many established codebases.

## 2. Render Props
A pattern where a component's prop is a function that the component calls to render its content.
- **Why?**: Allows a component to share its internal state or logic with any rendering logic you provide.
```javascript
<DataFetcher render={data => <MyComponent data={data} />} />
```

## 3. Component Composition
Instead of using inheritance (which React strongly discourages), use composition.
- **Props Children**: Passing components as children to another component (creating "layout" or "container" components).
```javascript
function Card({ children }) {
  return <div className="card">{children}</div>;
}
```

## 4. Custom Hooks (Logic Reuse)
The modern way to share non-visual logic between components.
- **Example**: Moving data fetching logic into `useFetch(url)` so multiple components can use it without repeating code.

## 5. Controlled vs. Uncontrolled Components
- **Controlled**: Component state is handled by React (via `value` and `onChange`). Preferred for forms.
- **Uncontrolled**: Component state is handled by the DOM (via `useRef`). Used for simple cases or integrating non-React libraries.

## 6. Portals
A way to render children into a DOM node that exists outside the hierarchy of the parent component.
- **Use case**: Modals, tooltips, and floating menus that need to ignore `overflow: hidden` or `z-index` of their parent.

> [!TIP]
> **Composition is better than Props**: 
> If you find yourself passing many props through a component just to reach a child ("Prop Drilling"), consider using component composition or Context instead.
