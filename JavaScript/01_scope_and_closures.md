# Scope and Closures

## 1. Scope
Scope determines the accessibility (visibility) of variables.

- **Global Scope**: Variables defined outside any function or block.
- **Function (Local) Scope**: Variables defined inside a function.
- **Block Scope (ES6)**: Variables defined with `let` and `const` inside `{}` blocks.
- **Lexical Scope**: JavaScript uses lexical scoping, meaning the scope of a variable is determined by its position in the source code.

## 2. Hoisting
Hoisting is a JavaScript mechanism where variables and function declarations are moved to the top of their containing scope during the compilation phase.

- **Function Declarations**: Fully hoisted. You can call them before they are defined.
- **`var`**: Hoisted but initialized with `undefined`.
- **`let` and `const`**: Hoisted but remain in the **Temporal Dead Zone (TDZ)** until the declaration is reached. Accessing them before declaration results in a `ReferenceError`.

## 3. Closures
A closure is the combination of a function bundled together (enclosed) with references to its surrounding state (the lexical environment).

### How it works
Even after an outer function has finished executing, its inner function still has access to the outer function's variables.

```javascript
function outer() {
  let count = 0;
  return function inner() {
    count++;
    console.log(count);
  };
}

const counter = outer();
counter(); // 1
counter(); // 2
```

### Use Cases
- **Data Privacy (Encapsulation)**: Creating private variables.
- **Partial Application and Currying**.
- **Event Handlers and Callbacks**.

### Pitfalls
- **Memory Leaks**: Closures keep references to outer variables, preventing them from being garbage collected if the closure itself is still reachable.

## 4. IIFE (Immediately Invoked Function Expression)
A function that runs as soon as it is defined.
```javascript
(function() {
  console.log("I am an IIFE");
})();
```
**Why?** To create a private scope and avoid polluting the global namespace (less common now with ES6 modules).

> [!IMPORTANT]
> **Closure Definition hack**: A closure is when a function "remembers" its lexical scope even when it is executed outside that lexical scope.
