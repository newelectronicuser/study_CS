# Fundamentals and Virtual DOM

React is a declarative, efficient, and flexible JavaScript library for building user interfaces.

## 1. JSX (JavaScript XML)
JSX is a syntax extension for JavaScript. It allows us to write HTML-like code inside JavaScript.
- **Behind the scenes**: JSX is transpiled (usually by Babel) into `React.createElement()` calls.
- **Rules**: Must have a single root element, use `className` instead of `class`, and use camelCase for attributes.

## 2. Components
Components are independent and reusable bits of code. They serve the same purpose as JavaScript functions but work in isolation and return HTML.
- **Functional Components**: Simple JavaScript functions that return JSX. Now the standard in React.
- **Class Components**: ES6 classes that extend `React.Component`. (Less common today).

## 3. Props vs. State
- **Props (Properties)**: Read-only data passed from parent to child. They represent the "configuration" of a component.
- **State**: Private data managed within the component. When state changes, the component re-renders.
    - **Immutability**: State must *never* be mutated directly. Always use the setter function (e.g., `setState` or `setCount`).

## 4. Virtual DOM and Reconciliation
The Virtual DOM (VDM) is a lightweight, in-memory representation of the real DOM.

### Why use a Virtual DOM?
Updating the real DOM is slow because it triggers layout and repaint.

### The Process (Reconciliation)
1.  **Render**: When state changes, a new Virtual DOM tree is created.
2.  **Diffing**: React compares the new VDOM tree with the previous one.
3.  **Patching**: React calculates the minimum number of changes needed and applies them to the real DOM.

### Diffing Algorithm Rules
- **Keys**: Help React identify which items have changed, been added, or been removed. Never use array indices as keys if the list can change order.
- **Element types**: If an element type changes (e.g., from `<div>` to `<span>`), React will tear down the whole tree and rebuild it.

> [!IMPORTANT]
> **React is Unidirectional**: Data flows downward from parent to child (One-way data binding). This makes debugging easier as the source of truth is clearer.
